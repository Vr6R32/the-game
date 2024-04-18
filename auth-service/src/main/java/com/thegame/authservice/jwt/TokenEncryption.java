package com.thegame.authservice.jwt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class TokenEncryption {

    public TokenEncryption(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private final JwtConfig jwtConfig;

    public String encrypt(String token) {
        SecretKey key = decodeKeyFromString();
        IvParameterSpec iv = new IvParameterSpec(jwtConfig.getTokenEncryptionIvParameter());
        byte[] cipherText = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(jwtConfig.getTokenEncryptionTransformation());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            cipherText = cipher.doFinal(token.getBytes());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String token) {
        SecretKey key = decodeKeyFromString();
        IvParameterSpec iv = new IvParameterSpec(jwtConfig.getTokenEncryptionIvParameter());
        byte[] plainText = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(jwtConfig.getTokenEncryptionTransformation());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            plainText = cipher.doFinal(Base64.getDecoder().decode(token));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return new String(plainText);
    }

    public String generateKeyString() throws NoSuchAlgorithmException {
        SecretKey key = generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String generateIvString() {
        IvParameterSpec iv = generateIv();
        return Base64.getEncoder().encodeToString(iv.getIV());
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(jwtConfig.getTokenEncryptionAlgorithm());
        keyGenerator.init(jwtConfig.getTokenEncryptionKeySize(), new SecureRandom());
        return keyGenerator.generateKey();
    }
    private SecretKey decodeKeyFromString() {
        byte[] decodedKey = jwtConfig.getTokenEncryptionKey();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, jwtConfig.getTokenEncryptionAlgorithm());
    }
}
