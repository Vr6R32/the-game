package com.thegame.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FrontViewController {

    @Value("${app.websocketUrl}")
    private String websocketUrl;

    @Value("${app.websocketConnectionRefreshInterval}")
    private String websocketRefreshInterval;


    @GetMapping("/")
    public String getIndex(Model model){
        model.addAttribute("websocketUrl", websocketUrl);
        model.addAttribute("websocketConnectionRefreshInterval", websocketRefreshInterval);
        return "chat.html";
    }

}
