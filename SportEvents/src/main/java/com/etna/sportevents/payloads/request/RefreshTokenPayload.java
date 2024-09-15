package com.etna.sportevents.payloads.request;

import javax.validation.constraints.NotBlank;
public class RefreshTokenPayload {
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;

    public RefreshTokenPayload(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenPayload() {
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
