package com.practicum.authentication_service.service;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AESUtil {
    public static String encrypt(String plainText, String base64Key, String base64IV) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] iv = Base64.getDecoder().decode(base64IV);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // CBC mode with padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedBase64, String base64Key, String base64IV) throws Exception {
        byte[] key = Base64.getDecoder().decode(base64Key);
        byte[] iv = Base64.getDecoder().decode(base64IV);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
