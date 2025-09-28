package com.practicum.authentication_service.service;

import java.security.SecureRandom;
import java.util.Base64;

public class AESKeyGenerator {
    public static String generateKey() {
        byte[] key = new byte[32]; // 256-bit AES
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static String generateIV() {
        byte[] iv = new byte[16]; // 128-bit IV
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }
}
