package com.thegame.config;


import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
public class FeignClientConfig {

    private static final int CONNECT_TIMEOUT_MILLIS = 1000;
    private static final int READ_TIMEOUT_MILLIS = 1000;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS, READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS, true);
    }
}