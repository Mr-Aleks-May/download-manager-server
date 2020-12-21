package com.mralexmay.projects.download_manager.server.api.user.model;


import com.mralexmay.projects.download_manager.server.commons.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "downloads")
public class Download {
    /**
     * Download id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Download unique identifier.
     */
    @Column(name = "dsid")
    private String downloadDSID = "null";
    /**
     * Download file name.
     */
    @NotNull
    @Column(name = "fileName")
    private String fileName;
    /**
     * File extension.
     */
    @NotNull
    @Column(name = "extension")
    private String extension;
    /**
     * A folder for temporary storage of data during download.
     */
    @NotNull
    @Column(name = "tmpDirStr",  length = 1000)
    private String tmpDir;
    /**
     * The downloaded file will be moved to this directory.
     */
    @NotNull
    @Column(name = "outputDirStr", length = 1000)
    private String outputDir;
    /**
     * Download url.
     */
    @NotNull
    @Column(name = "url", length = 4000)
    private String url;
    /**
     * Download category.
     */
    @NotNull
    @OneToOne()
    private Category category;
    /**
     * Time when the download was added.
     */
    @Column(name = "creationTime")
    private long creationTime;


    public Download() {
    }


    public Long getId() {
        return id;
    }

    public Download setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDownloadDSID() {
        return downloadDSID;
    }

    public Download setDownloadDSID(String serializableId) {
        this.downloadDSID = serializableId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Download setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public Download setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public Download setTmpDir(String tmpDirStr) {
        this.tmpDir = tmpDirStr;
        return this;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public Download setOutputDir(String outputDirStr) {
        this.outputDir = outputDirStr;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Download setUrl(String url) {
        this.url = url;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Download setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public Download setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }


    @Override
    public String toString() {
        return this.fileName;
    }
}
