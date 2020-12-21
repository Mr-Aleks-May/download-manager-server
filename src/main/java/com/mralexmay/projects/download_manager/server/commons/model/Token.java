package com.mralexmay.projects.download_manager.server.commons.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "tokens")
public class Token {
    /**
     * Token identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Token value.
     */
    @Column(name = "token")
    private String value;
    /**
     * Time when token expires.
     */
    @Column(name = "expires")
    private LocalDateTime expires;


    public Token() {
    }

    public static Token generateNew() {
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


    public Long getId() {
        return id;
    }

    public Token setId(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Token setValue(String accessToken) {
        this.value = accessToken;
        return this;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public Token setExpires(LocalDateTime expires) {
        this.expires = expires;
        return this;
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
