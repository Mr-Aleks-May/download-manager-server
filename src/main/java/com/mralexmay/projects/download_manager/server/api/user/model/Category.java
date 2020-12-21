package com.mralexmay.projects.download_manager.server.api.user.model;


import com.mralexmay.projects.download_manager.server.commons.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    /**
     * Category id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /**
     * Category unique identifier.
     */
    @NotNull
    @Column(name = "csid")
    private String categoryCSID;
    /**
     * Category name.
     */
    @NotNull
    @Column(name = "name")
    private String name;
    /**
     * Category is part of plugin with plugin_csid identifier.
     */
    @NotNull
    @Column(name = "plugin_psid")
    private String pluginPSID;
    /**
     * Downloads count belonging to this category.
     */
    @NotNull
    @Column(name = "downloads_count")
    private Integer count = 0;


    public Category() {
    }


    public long getId() {
        return id;
    }

    public Category setId(long id) {
        this.id = id;
        return this;
    }

    public String getCategoryCSID() {
        return categoryCSID;
    }

    public Category setCategoryCSID(String categoryId) {
        this.categoryCSID = categoryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getPluginPSID() {
        return pluginPSID;
    }

    public Category setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public Category setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Category increaseCount() {
        this.count++;
        return this;
    }

    public Category decreaseCount() {
        this.count--;
        return this;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
