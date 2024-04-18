package com.thegame.authservice.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
class JwtConfig {


    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;


    @Value("${application.security.jwt.token-encryption.key-value}")
    private String tokenEncryptionKeyValue;

    @Value("${application.security.jwt.token-encryption.ivParameter}")
    private String tokenEncryptionIvParameter;

    @Value("${application.security.jwt.token-encryption.algorithm}")
    private String tokenEncryptionAlgorithm;

    @Value("${application.security.jwt.token-encryption.transformation}")
    private String tokenEncryptionTransformation;

    @Value("${application.security.jwt.token-encryption.key-size}")
    private int tokenEncryptionKeySize;

    public byte[] getSecretKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    public long getJwtExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public byte[] getTokenEncryptionKey() {
        return Base64.getDecoder().decode(tokenEncryptionKeyValue);
    }

    public byte[] getTokenEncryptionIvParameter() {
        return Base64.getDecoder().decode(tokenEncryptionIvParameter);
    }

    public String getTokenEncryptionAlgorithm() {
        return tokenEncryptionAlgorithm;
    }

    public String getTokenEncryptionTransformation() {
        return tokenEncryptionTransformation;
    }

    public int getTokenEncryptionKeySize() {
        return tokenEncryptionKeySize;
    }
}
