package com.thegame.security.jwt;

import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
class JwtConfig {

    public byte[] getSecretKey(){
        return "404E635266556A586E3272357538782F413F4428472B4B6250645367566B59701231BZCZWQWRXCVXQWEQWREYGCX34234VXCVERW".getBytes(StandardCharsets.UTF_8);
    }

    public long getJwtExpiration(){
        return 1000000L;
    }

    public long getRefreshTokenExpiration(){
        return 1000000L;
    }

}
