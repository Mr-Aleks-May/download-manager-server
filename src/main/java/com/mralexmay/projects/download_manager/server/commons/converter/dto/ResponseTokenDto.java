package com.mralexmay.projects.download_manager.server.commons.converter.dto;


public class ResponseTokenDto {
    /**
     * Token value.
     */
    private String token;


    public ResponseTokenDto() {
    }


    public String getToken() {
        return token;
    }

    public ResponseTokenDto setToken(String accessToken) {
        this.token = accessToken;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseTokenDto{" +
                "value='" + token + '\'' +
                '}';
    }
}
