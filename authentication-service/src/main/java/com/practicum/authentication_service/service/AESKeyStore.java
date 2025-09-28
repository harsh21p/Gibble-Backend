package com.practicum.authentication_service.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AESKeyStore {
    private final Map<String, KeyIvPair> sessionKeys = new ConcurrentHashMap<>();

    public void store(String sessionId, String aesKey, String aesIV) {
        sessionKeys.put(sessionId, new KeyIvPair(aesKey, aesIV));
    }

    public KeyIvPair get(String sessionId) {
        return sessionKeys.get(sessionId);
    }

    public static class KeyIvPair {
        private final String key;
        private final String iv;

        public KeyIvPair(String key, String iv) {
            this.key = key;
            this.iv = iv;
        }

        public String getKey() {
            return key;
        }

        public String getIv() {
            return iv;
        }
    }
}
