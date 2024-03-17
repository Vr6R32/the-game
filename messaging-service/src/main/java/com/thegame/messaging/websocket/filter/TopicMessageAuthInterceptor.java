package com.thegame.messaging.websocket.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.dto.AuthenticationUserObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicMessageAuthInterceptor implements ChannelInterceptor {

    private final ObjectMapper objectMapper;


    @Lazy
    private final SimpMessagingTemplate messagingTemplate;

    public void handleAuthenticationError(StompHeaderAccessor accessor){
        Principal principal = Objects.requireNonNull(accessor.getUser());
        WebsocketUserPrincipal user = (WebsocketUserPrincipal) principal;
        messagingTemplate.convertAndSendToUser(
                user.getUserId().toString(), "/errors", "403 FORBIDDEN"
        );
    }


        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (accessor != null) {
                String xAuthUserJsonObject = getAuthUserHeader(accessor);
                if (xAuthUserJsonObject != null) {
                    AuthenticationUserObject authUserObject = getAuthenticationUserObject(accessor, xAuthUserJsonObject);
                    if (authUserObject != null && authUserObject.accessTokenExpiration() != null) {

                        if (authUserObject.accessTokenExpiration().before(new Date())) {
                            log.info("TOKEN HAS EXPIRED FOR {}", authUserObject.username());
                            handleAuthenticationError(accessor);
                            return null;
                        } else {
                            log.info("TOKEN IS VALID FOR {}", authUserObject.username());
                        }
                    } else {
                        log.info("AuthenticationUserObject is null or accessTokenExpiration is null");
                        handleAuthenticationError(accessor);
                        return null;
                    }
                } else {
                    handleAuthenticationError(accessor);
                    log.info("NO X-AUTH-OBJECT DOESNT SEND MESSAGE {}", accessor.getHost());
                    return null;
                }
            }
            return message;
        }

    private String getAuthUserHeader(StompHeaderAccessor accessor) {
        Object attributes = accessor.toMap().get("simpSessionAttributes");

        if (attributes instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> headersMap = (Map<String, Object>) attributes;
            return (String) headersMap.get("X-USER-AUTH");
        }
        return null;
    }

    private AuthenticationUserObject getAuthenticationUserObject(StompHeaderAccessor accessor, String jsonValue) {
        try {
            AuthenticationUserObject authUserObject = objectMapper.readValue(jsonValue, AuthenticationUserObject.class);
            Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());

            Principal userPrincipal = new WebsocketUserPrincipal(authUserObject.username(),authUserObject.id());
            sessionAttributes.put("user", authUserObject);
            accessor.setUser(userPrincipal);
            return authUserObject;
        } catch (JsonProcessingException e) {
            log.info("JSON OBJECT MAPPING EXCEPTION {} FOR {}", e.getMessage(),jsonValue);
            return null;
        }
    }
}
