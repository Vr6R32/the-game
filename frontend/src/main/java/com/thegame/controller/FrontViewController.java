package com.thegame.controller;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontViewController {

    private final EurekaInstanceConfig eurekaInstanceConfig;

    public FrontViewController(EurekaInstanceConfig eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }

    @GetMapping
    public String getIndex(Model model){
        String instanceId = eurekaInstanceConfig.getInstanceId();
        model.addAttribute("instanceId", instanceId);
        return "index.html";
    }

}
