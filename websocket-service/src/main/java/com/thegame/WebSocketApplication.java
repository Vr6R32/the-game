package com.thegame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(scanBasePackages = {"com.thegame.clients","com.thegame","com.thegame.config"})
@EnableFeignClients(basePackageClasses=com.thegame.clients.ConversationServiceClient.class)
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
        configureSecurityContextInheritance();
    }
    public static void configureSecurityContextInheritance() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}