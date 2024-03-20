package com.thegame.websocket;


import com.thegame.websocket.filter.HandshakeInterceptorImpl;
import com.thegame.websocket.filter.TopicMessageAuthInterceptor;
import com.thegame.websocket.filter.TopicSubscriptionAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;



@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final HandshakeInterceptorImpl handshakeInterceptor;

    @Lazy
    @Autowired
    private TopicMessageAuthInterceptor topicMessageAuthInterceptor;

    @Lazy
    @Autowired
    private TopicSubscriptionAuthInterceptor topicSubscriptionAuthInterceptor;



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/conversation","/user","/private");
        registry.setApplicationDestinationPrefixes("/chat");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                .addInterceptors(handshakeInterceptor)
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(1000);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(topicMessageAuthInterceptor, topicSubscriptionAuthInterceptor);
    }
}
