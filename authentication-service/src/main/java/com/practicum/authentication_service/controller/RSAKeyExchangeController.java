package com.practicum.authentication_service.controller;

import com.practicum.authentication_service.Model.KeyExchangeRequest;
import com.practicum.authentication_service.service.AESKeyGenerator;
import com.practicum.authentication_service.service.AESKeyStore;
import com.practicum.authentication_service.service.AESUtil;
import com.practicum.authentication_service.service.RSAEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/authentication/keys")
public class RSAKeyExchangeController {

    @Autowired
    private AESKeyStore aesKeyStore;

    @PostMapping("/exchange-key")
    public ResponseEntity<Map<String, String>> exchangeKey(@RequestBody KeyExchangeRequest request) throws Exception {
        String aesKey = AESKeyGenerator.generateKey();
        String aesIV = AESKeyGenerator.generateIV();
        String sessionId = UUID.randomUUID().toString();
        aesKeyStore.store(sessionId, aesKey, aesIV);

        try {
            String encryptedKey = RSAEncryptor.encrypt(aesKey, request.getClientPublicKey());
            String encryptedIV = RSAEncryptor.encrypt(aesIV, request.getClientPublicKey());
            Map<String, String> response = new HashMap<>();
            response.put("encryptedKey", encryptedKey);
            response.put("encryptedIV", encryptedIV);
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    // This implementation will be the client side code for testing purpose we implemented this on server
    @PostMapping("/exchange-key-rsa-test")
    public ResponseEntity<Map<String, String>> exchangeKeyTest(@RequestBody KeyExchangeRequest request) throws Exception {
        try {
            String data = RSAEncryptor.decrypt(request.getData(),request.getPrivateKey());
            Map<String, String> response = new HashMap<>();
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    // This implementation will be the client side code for testing purpose we implemented this on server
    @PostMapping("/exchange-key-aes-enc-test")
    public ResponseEntity<Map<String, String>> exchangeKeyAESEncTest(@RequestBody KeyExchangeRequest request) throws Exception {
        try {
            String data = AESUtil.encrypt(request.getData(),aesKeyStore.get(request.getSessionId()).getKey(),aesKeyStore.get(request.getSessionId()).getIv());
            Map<String, String> response = new HashMap<>();
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    // This implementation will be the client side code for testing purpose we implemented this on server
    @PostMapping("/exchange-key-aes-dec-test")
    public ResponseEntity<Map<String, String>> exchangeKeyAESDecTest(@RequestBody KeyExchangeRequest request) throws Exception {
        try {
            String data = AESUtil.decrypt(request.getData(),aesKeyStore.get(request.getSessionId()).getKey(),aesKeyStore.get(request.getSessionId()).getIv());
            Map<String, String> response = new HashMap<>();
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }
}
