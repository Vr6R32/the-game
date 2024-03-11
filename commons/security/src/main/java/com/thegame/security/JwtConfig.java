package com.thegame.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B59701231BZCZWQWRXCVXQWEQWREYGCX34234VXCVERW";
    private final long jwtExpiration = 660000L;
    private final long refreshExpiration = 604800000L;
    private final String keyString = "Udh5YQlFnpMuEwltT7m4q8Dcsvz+G28Iqru3kk3vJx8=";
    private final String ivString = "anjbBT/W+R6ycBK2Akx1Ug==";


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
