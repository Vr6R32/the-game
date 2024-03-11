package com.thegame.security.jwt;

import org.springframework.context.annotation.Configuration;

@Configuration
class JwtConfig {

    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B59701231BZCZWQWRXCVXQWEQWREYGCX34234VXCVERW";
    private final long jwtExpiration = 3600000L;
    private final long refreshExpiration = 604800000L;

    public String getSecretKey(){
        return this.secretKey;
    }

    public long getJwtExpiration(){
        return this.jwtExpiration;
    }

    public long getRefreshExpiration(){
        return this.refreshExpiration;
    }

}
