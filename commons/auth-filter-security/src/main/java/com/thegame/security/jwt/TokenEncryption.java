package com.thegame.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class TokenEncryption {

    private final String ALGORITHM = "AES";
    private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final String keyString = "Udh5YQlFnpMuEwltT7m4q8Dcsvz+G28Iqru3kk3vJx8=";
    private final String ivString = "anjbBT/W+R6ycBK2Akx1Ug==";
    private final int KEY_SIZE = 256;




//    @PostConstruct
//    public void init(){
//        System.out.println("Generated Key: " + jwtConfig.getKeyString());
//        System.out.println("Generated IV: " + jwtConfig.getIvString());
//    }

    public String encrypt(String token) {
        SecretKey key = decodeKeyFromString(keyString);
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivString));
        byte[] cipherText = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            cipherText = cipher.doFinal(token.getBytes());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String token) {
        SecretKey key = decodeKeyFromString(keyString);
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivString));
        byte[] plainText = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            plainText = cipher.doFinal(Base64.getDecoder().decode(token));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return new String(plainText);
    }

    public String generateKeyString() throws Exception {
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

    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }
    private SecretKey decodeKeyFromString(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
}
