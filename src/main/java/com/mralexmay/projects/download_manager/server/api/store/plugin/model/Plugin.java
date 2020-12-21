package com.mralexmay.projects.download_manager.server.api.store.plugin.model;

import com.mralexmay.projects.download_manager.server.commons.converter.StringToSetConverter;
import com.mralexmay.projects.download_manager.server.commons.converter.dto.UserDto;
import com.mralexmay.projects.download_manager.server.commons.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plugins")
public class Plugin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Plugin unique identifier.
     */
    @Column(name = "psid")
    private String pluginPSID;
    /**
     * Plugin name.
     */
    @Column(name = "name")
    private String name;
    /**
     * Plugin version.
     */
    @Column(name = "version")
    private String version;
    /**
     * Load user.
     */
    @Column(name = "author")
    private String author;
    /**
     * Digital sign.
     */
    @Column(name = "digital_sign")
    private String digitalSign;
    /**
     * Check sum (hash MD5).
     */
    @Column(name = "check_sum")
    private Long checkSum = 0L;
    /**
     * Full path to plugin jar on server.
     */
    @Column(name = "full_path")
    private String fullPath;
    /**
     * Plugin description.
     */
    @Column(name = "description")
    private String description;
    /**
     * Tags.
     */
    @Column(name = "tags")
    @Convert(converter = StringToSetConverter.class)
    private Set<String> tags;


    public Plugin() {
        tags = new HashSet<>();
    }


    public String getPluginPSID() {
        return pluginPSID;
    }

    public Plugin setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Plugin setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Plugin setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Plugin setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public Plugin setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
        return this;
    }

    public Long getCheckSum() {
        return checkSum;
    }

    public Plugin setCheckSum(long checkSum) {
        this.checkSum = checkSum;
        return this;
    }

    public String getFullPath() {
        return fullPath;
    }

    public Plugin setFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Plugin setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Plugin setTags(Set<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return "{" + "pluginPSID='" + pluginPSID + ", name='" + name + '}';
    }
}
