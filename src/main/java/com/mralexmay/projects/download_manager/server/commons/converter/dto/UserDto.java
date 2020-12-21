package com.mralexmay.projects.download_manager.server.commons.converter.dto;

import com.mralexmay.projects.download_manager.server.commons.model.Token;
import com.mralexmay.projects.download_manager.server.api.user.model.Download;

import java.util.List;

public class UserDto {
    /**
     * User identifer.
     */
    private Long id;
    /**
     * User login.
     */
    private String login;
    /**
     * User password. Hash md5.
     */
    private String password;
    /**
     * Access token assigned to current user.
     */
    private TokenDto token;
    /**
     * User downloads list.
     */
    private List<Download> downloads;


    public UserDto() {
    }



    /**
     * Add one download to user downloads list.
     *
     * @param download
     */
    public void add(Download download) {
        downloads.add(download);
    }

    /**
     * Add multiple downloads to user downloads list.
     *
     * @param download
     */
    public void addAll(List<Download> download) {
        downloads.addAll(download);
    }


    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public TokenDto getToken() {
        return token;
    }

    public UserDto setToken(TokenDto token) {
        this.token = token;
        return this;
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    public UserDto setDownloads(List<Download> downloads) {
        this.downloads = downloads;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", accessToken=" + token +
                ", downloadsList=" + downloads +
                '}';
    }
}
