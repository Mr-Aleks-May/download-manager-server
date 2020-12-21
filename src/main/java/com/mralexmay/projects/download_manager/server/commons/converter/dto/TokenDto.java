package com.mralexmay.projects.download_manager.server.commons.converter.dto;


import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TokenDto {
    /**
     * Token identifier.
     */
    private long id;
    /**
     * Token value.
     */
    @NotBlank(message = "{NotBlank.tokenDto.value}")
    private String token;
    /**
     * Time when token expires.
     */
    private LocalDateTime expires;


    public TokenDto() {
    }


    public long getId() {
        return id;
    }

    public TokenDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public TokenDto setToken(String accessToken) {
        this.token = accessToken;
        return this;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public TokenDto setExpires(LocalDateTime expires) {
        this.expires = expires;
        return this;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", accessToken='" + token + '\'' +
                ", expires=" + expires +
                '}';
    }
}
