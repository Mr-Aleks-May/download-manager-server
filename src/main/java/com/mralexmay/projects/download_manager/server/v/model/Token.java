package com.mralexmay.projects.download_manager.server.v.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Expose
    @Column(name = "access_token")
    private String value;
    private LocalDateTime expires;


    public Token() {
    }

    public static Token createNewToken() {
        Token token = new Token();

        final String accessToken = String.format("%s-%s-%s-%s",
                System.currentTimeMillis(),
                System.nanoTime(),
                new Random().nextInt(),
                new Random().nextLong());

        token.setValue(accessToken);
        token.setExpires(LocalDateTime.now().plusDays(1));

        return token;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String accessToken) {
        this.value = accessToken;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", accessToken='" + value + '\'' +
                ", expires=" + expires +
                '}';
    }
}
