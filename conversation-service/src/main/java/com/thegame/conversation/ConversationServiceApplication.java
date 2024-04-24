package com.thegame.conversation;

import com.thegame.clients.WebSocketServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.thegame")
@EnableFeignClients(basePackageClasses=
        {com.thegame.clients.UserServiceClient.class, WebSocketServiceClient.class}
)
public class ConversationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConversationServiceApplication.class, args);
    }
}