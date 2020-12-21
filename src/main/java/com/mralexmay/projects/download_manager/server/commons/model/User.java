package com.mralexmay.projects.download_manager.server.commons.model;

import com.mralexmay.projects.download_manager.server.api.user.model.Download;
import com.mralexmay.projects.download_manager.server.api.user.model.UserPlugin;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    /**
     * User identifer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /**
     * User login.
     */
    @Column(name = "login")
    private String login;
    /**
     * User first name.
     */
    @Column(name = "first_name")
    private String firstName;
    /**
     * User last name.
     */
    @Column(name = "last_name")
    private String lastName;
    /**
     * User password. Hash md5.
     */
    @Column(name = "password")
    private String password;
    /**
     * Access token assigned to current user.
     */
    @OneToOne(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Token token;
    /**
     * User downloads list.
     */
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Download> downloads;
    /**
     * Information about plugins, which use customer in client app.
     */
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserPlugin> userPlugins;


    public User() {
    }

    public User(String login, String password, Token token, List<Download> downloads, List<UserPlugin> userPlugins) {
        this.login = login;
        this.password = password;
        this.token = token;
        this.downloads = downloads;
        this.userPlugins = userPlugins;
    }


    /**
     * Add one download to user downloads list.
     *
     * @param download
     * @return true if download successfully added; otherwise false.
     */
    public boolean add(Download download) {
        return downloads.add(download);
    }

    /**
     * Add multiple downloads to user downloads list.
     *
     * @param download
     * @return true if all downloads successfully removed from user downloads list; otherwise false.
     */
    public boolean addAll(List<Download> download) {
        return downloads.addAll(download);
    }

    /**
     * Remove download from user downloads list.
     *
     * @param download
     * @return true if download successfully removed; otherwise false.
     */
    public boolean remove(Download download) {
        return downloads.remove(download);
    }

    /**
     * Add information about plugin installed in client app.
     *
     * @param plugin information plugin.
     * @return true if plugin successfully dded; otherwise false.
     */
    public boolean add(UserPlugin plugin) {
        return userPlugins.add(plugin);
    }

    /**
     * Remove plugin from user plugins list.
     * Plugin removes if he had not any category in it.
     *
     * @param plugin plugin to remove.
     * @return true if plugin successfully removed; otherwise false.
     */
    public boolean remove(UserPlugin plugin) {
        return userPlugins.remove(plugin);
    }


    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Token getToken() {
        return token;
    }

    public User setToken(Token token) {
        this.token = token;
        return this;
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    public User setDownloads(List<Download> downloads) {
        this.downloads = downloads;
        return this;
    }

    public List<UserPlugin> getUserPlugins() {
        return userPlugins;
    }

    public void setUserPlugins(List<UserPlugin> userPlugins) {
        this.userPlugins = userPlugins;
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
