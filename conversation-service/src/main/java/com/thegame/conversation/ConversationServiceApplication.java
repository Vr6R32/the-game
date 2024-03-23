package com.thegame.conversation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.thegame")
@EnableFeignClients(basePackageClasses=
        {com.thegame.clients.UserServiceClient.class, com.thegame.clients.WebSocketSessionClient.class}
)
public class ConversationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConversationServiceApplication.class, args);
    }
}