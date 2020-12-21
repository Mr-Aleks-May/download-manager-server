package com.mralexmay.projects.download_manager.server.commons.model;

public class AuthenticationUserData {
    private String login;
    private String password;


    public String getLogin() {
        return login;
    }

    public AuthenticationUserData setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthenticationUserData setPassword(String password) {
        this.password = password;
        return this;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
