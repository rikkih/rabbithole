package com.hoderick.rabbithole.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketSecurityConfig webSocketSecurityConfig;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.issuer}")
    private String issuer;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/api");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        try {
            registry
                    .addEndpoint("/ws") // Native WebSocket only
                    .setAllowedOrigins("http://localhost:5173")
                    .addInterceptors(new Auth0HandshakeInterceptor())
                    .setHandshakeHandler(new Auth0HandshakeHandler(issuer, audience));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize handshake handler", e);
        }
    }
}
