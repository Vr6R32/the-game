package com.thegame.controller;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

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

    @GetMapping("/messages")
    public String getChat(Model model){
//        AuthenticationUserObject user = (AuthenticationUserObject) authentication.getPrincipal();
        model.addAttribute("user", new AuthenticationUserObject(1L,"karacz","Karacz@htimna.pl", Role.ROLE_USER,new Date()));
        model.addAttribute("websocketUrl", websocketUrl);
        return "chat.html";
    }


}
