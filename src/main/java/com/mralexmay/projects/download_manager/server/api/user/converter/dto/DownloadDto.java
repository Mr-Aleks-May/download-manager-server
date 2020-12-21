package com.mralexmay.projects.download_manager.server.api.user.converter.dto;

public class DownloadDto {
    /**
     * Download unique identifier.
     */
    private String dsid = "null";
    /**
     * Download file name.
     */
    private String fileName;
    /**
     * File extension.
     */
    private String extension;
    /**
     * A folder for temporary storage of data during download.
     */
    private String tmpDir;
    /**
     * The downloaded file will be moved to this directory.
     */
    private String outputDir;
    /**
     * Download url.
     */
    private String url;
    /**
     * Download category.
     */
    private CategoryDto categoryDto;
    /**
     * Time when the download was added.
     */
    private long creationTime;


    public DownloadDto() {
    }


    public String getDSID() {
        return dsid;
    }

    public DownloadDto setDSID(String DSID) {
        this.dsid = DSID;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DownloadDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public DownloadDto setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public DownloadDto setTmpDir(String tmpDirStr) {
        this.tmpDir = tmpDirStr;
        return this;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public DownloadDto setOutputDir(String outputDirStr) {
        this.outputDir = outputDirStr;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DownloadDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public CategoryDto getCategoryDto() {
        return categoryDto;
    }

    public DownloadDto setCategoryDto(CategoryDto category) {
        this.categoryDto = category;
        return this;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public DownloadDto setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }


    @Override
    public String toString() {
        return this.fileName;
    }
}
