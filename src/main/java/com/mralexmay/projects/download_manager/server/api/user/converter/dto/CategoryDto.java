package com.mralexmay.projects.download_manager.server.api.user.converter.dto;


import com.mralexmay.projects.download_manager.server.commons.annotations.NotNull;

public class CategoryDto {
    /**
     * Category unique identifier.
     */
    @NotNull
    private String categoryCSID;
    /**
     * Category name.
     */
    @NotNull
    private String name;
    /**
     * Category is part of plugin with plugin_csid identifier.
     */
    @NotNull
    private String pluginPSID;


    public CategoryDto() {
    }


    public String getCategoryCSID() {
        return categoryCSID;
    }

    public CategoryDto setCategoryCSID(String categoryCSID) {
        this.categoryCSID = categoryCSID;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPluginPSID() {
        return pluginPSID;
    }

    public CategoryDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
