package com.mralexmay.projects.download_manager.server.v.model;


import com.mralexmay.projects.download_manager.server.v.model.download.DownloadEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @OneToOne
    private Token token;
    @OneToOne
    private Settings settings;
    @ElementCollection
    private List<DownloadEntity> downloadsList;


    public User() {
    }

    public User(String login, String password, Token token, Settings settings, List<DownloadEntity> downloadsList) {
        this.login = login;
        this.password = password;
        this.token = token;
        this.settings = settings;
    }


    public void add(DownloadEntity downloadEntity) {
        downloadsList.add(downloadEntity);
    }

    public void addAll(List<DownloadEntity> downloadEntityList) {
        downloadsList.addAll(downloadEntityList);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token accessToken) {
        this.token = accessToken;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<DownloadEntity> getDownloadsList() {
        return downloadsList;
    }

    public void setDownloadsList(List<DownloadEntity> downloadsList) {
        this.downloadsList = downloadsList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", accessToken=" + token +
                ", settings=" + settings +
                ", downloadsList=" + downloadsList +
                '}';
    }
}
