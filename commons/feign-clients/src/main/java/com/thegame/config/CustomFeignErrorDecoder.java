package com.thegame.config;

import feign.Response;
import feign.codec.ErrorDecoder;

import java.net.SocketTimeoutException;


public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 500) {
            return new SocketTimeoutException();
        }
        return defaultDecoder.decode(methodKey, response);
    }
}