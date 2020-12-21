package com.mralexmay.projects.download_manager.server.api.user.model;


import com.mralexmay.projects.download_manager.server.commons.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_plugins")
public class UserPlugin {
    /**
     * User plugin identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Plugin unique identifier.
     */
    @NotNull
    @Column(name = "psid")
    private String pluginPSID;
    /**
     * Categories belonging to plugin.
     */
    @NotNull
    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Category> categories;


    public UserPlugin() {
    }

    public UserPlugin(List<Category> categories) {
        this.categories = categories;
    }

    public boolean add(Category category) {
        return categories.add(category);
    }

    public boolean remove(Category category) {
        return categories.remove(category);
    }


    public Long getId() {
        return id;
    }

    public UserPlugin setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPluginPSID() {
        return pluginPSID;
    }

    public UserPlugin setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public UserPlugin setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public int getCategoriesCount() {
        return categories.size();
    }


    @Override
    public String toString() {
        return "UserPluginInfo{" + "pluginPSID=" + pluginPSID + '}';
    }
}
