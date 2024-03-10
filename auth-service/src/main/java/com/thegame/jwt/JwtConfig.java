package com.thegame.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class JwtConfig {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Value("${token.key.value}")
    private String keyString;

    @Value("${token.ivParameter.value}")
    private String ivString;


    public String getSecretKey(){
        return this.secretKey;
    }

    public long getJwtExpiration(){
        return this.jwtExpiration;
    }

    public long getRefreshExpiration(){
        return this.refreshExpiration;
    }

    public String getKeyString(){
        return this.keyString;
    }

    public String getIvString(){
        return this.ivString;
    }

}
