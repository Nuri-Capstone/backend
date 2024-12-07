package com.nuri.nuribackend.config;

import com.nuri.nuribackend.controller.JwtTokenProvider;
import com.nuri.nuribackend.utils.AuthHandshakeInterceptor;
import com.nuri.nuribackend.controller.SocketVoiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final SocketVoiceHandler voiceHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("Registering Websocket handlers...");
        registry.addHandler(voiceHandler, "/ws/voice")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .addInterceptors(new AuthHandshakeInterceptor(jwtTokenProvider));
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(16 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(16 * 1024 * 1024);
        return container;
    }
}
