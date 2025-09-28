package com.practicum.authentication_service.Model;

import lombok.Getter;
import lombok.Setter;

public class KeyExchangeRequest {
    private String userId; // optional, if you want to map keys to user
    private String clientPublicKey;

    @Getter
    @Setter
    private String privateKey;

    @Getter
    @Setter
    private String data;

    @Getter
    @Setter
    private String sessionId;

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
