package com.thegame.controller;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.thegame.dto.AuthenticationUserObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FrontViewController {

    @Value("${app.websocketUrl}")
    private String websocketUrl;

    private final EurekaInstanceConfig eurekaInstanceConfig;

    public FrontViewController(EurekaInstanceConfig eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }

    @GetMapping("/")
    public String getIndex(Model model){
        String instanceId = eurekaInstanceConfig.getInstanceId();
        model.addAttribute("instanceId", instanceId);
        return "index.html";
    }


    @GetMapping("/login")
    public String getLoginPage(Model model){
        String instanceId = eurekaInstanceConfig.getInstanceId();
        model.addAttribute("instanceId", instanceId);
        return "login.html";
    }

    @GetMapping("/messages")
    public String getChat(Model model, Authentication authentication){
        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("websocketUrl", websocketUrl);
        return "chat.html";
    }


}
