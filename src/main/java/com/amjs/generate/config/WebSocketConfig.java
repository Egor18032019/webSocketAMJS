package com.amjs.generate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
//        В первой строке мы говорим, что сообщения, чей адрес (куда отправлены) начинается с  «/app«,
//        должны быть направлены в методы, занимающиеся обработкой сообщений.
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker
//        Во второй строке мы говорим, что  сообщения, чей адрес начинается с  «/topic«, должны быть направлены в брокер сообщений.
//        Брокер перенаправляет сообщения всем клиентам, подписанным на тему.

    }
}
