package com.nuri.nuribackend.utils;
import com.nuri.nuribackend.controller.JwtTokenProvider;
import com.nuri.nuribackend.dto.User.UserDto;
import com.nuri.nuribackend.exception.AuthenticationException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake called");
        // Sec-WebSocket-Protocol 헤더에서 토큰 읽기
        List<String> protocols = request.getHeaders().get("Sec-WebSocket-Protocol");
        System.out.println(protocols.get(0));
        if (protocols != null && !protocols.isEmpty()) {
            String token = resolveToken(protocols.get(0)); // 클라이언트에서 보낸 토큰
            try {
                // 토큰 검증
                if (!jwtTokenProvider.validateToken(token)) {
                    throw new RuntimeException("Invalid JWT Token");
                }

                // 검증된 사용자 정보 저장 (예: userId를 attributes에 추가)
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                System.out.println(auth);
                attributes.put("user_name", auth.getName()); // 사용자 이름을 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return false; // 핸드셰이크 중단
            }
        } else {
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false; // 토큰이 없을 경우 핸드셰이크 중단
        }

        return true; // 핸드셰이크 성공
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        // TODO Auto-generated method stub

    }

    private String resolveToken(String request) {
        if (request != null && request.startsWith("Bearer ")) {
            return request.substring(7); // "Bearer " 이후의 부분을 리턴
        }
        throw new IllegalArgumentException("Invalid token format");
    }
}
