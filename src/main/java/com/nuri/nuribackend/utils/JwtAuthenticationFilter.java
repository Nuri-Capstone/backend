package com.nuri.nuribackend.utils;

import com.nuri.nuribackend.controller.JwtTokenProvider;
import com.nuri.nuribackend.exception.AuthenticationException;
import com.nuri.nuribackend.exception.CustomException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, java.io.IOException {
        try {
            // 1. Request Header에서 JWT 토큰 추출
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            // 1. Request Header에서 JWT 토큰 추출

            if (httpRequest.getRequestURI().startsWith("/users") || httpRequest.getRequestURI().startsWith("/ws/voice") || httpRequest.getRequestURI().startsWith("/api/msg")) {
                chain.doFilter(request, response);
                return;
            }
            // 2. validateToken으로 토큰 유효성 검사
            String token = resolveToken(httpRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 유효하지 않은 경우 응답 처리
                throw new AuthenticationException("INVALID_TOKEN", "Invalid or missing token. Please log in.");
            }

            chain.doFilter(request, response);
        } catch (AuthenticationException ex){
            handleCustomException((HttpServletResponse) response, ex);
        }
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleCustomException(HttpServletResponse httpResponse, CustomException ex) throws IOException, java.io.IOException {
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 상태 코드 설정
        httpResponse.setContentType("application/json"); // JSON 응답 설정
        httpResponse.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

        // JSON 형식으로 메시지 작성
        String jsonResponse = String.format(
                "{\"message\": \"%s\", \"data\": \"%s\"}",
                ex.getErrorCode(),
                null
        );
        httpResponse.getWriter().write(jsonResponse); // JSON 데이터 작성
    }
}