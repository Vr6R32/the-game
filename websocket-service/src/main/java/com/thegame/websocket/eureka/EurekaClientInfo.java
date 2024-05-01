package com.thegame.websocket.eureka;

import com.netflix.discovery.EurekaClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaClientInfo {

    private final EurekaClient eurekaClient;

    public EurekaClientInfo(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    public String getInstanceId() {
        String instanceId = eurekaClient.getApplicationInfoManager().getInfo().getInstanceId();
        instanceId = instanceId.replace(':', '-');
        return instanceId;
    }


}
