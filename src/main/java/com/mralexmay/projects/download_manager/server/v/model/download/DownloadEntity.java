package com.mralexmay.projects.download_manager.server.v.model.download;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.model.download.DownloadFormatter;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Entity
@Table(name = "downloads")
public class DownloadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * Download id
     */
    private long id;
    @Expose
    @Column(name = "serializableId")
    private String serializableId = "null";
    /**
     * Download file name.
     */
    @Expose
    @NotNull
    @Column(name = "fileName")
    private String fileName;
    /**
     * File extension.
     */
    @Expose
    @NotNull
    @Column(name = "extension")
    private String extension;
    /**
     * A folder for temporary storage of data during download.
     */
    @Expose
    @NotNull
    @Column(name = "tmpDirStr")
    private String tmpDirStr;
    /**
     * The downloaded file will be moved to this directory.
     */
    @Expose
    @NotNull
    @Column(name = "outputDirStr")
    private String outputDirStr;
    /**
     * Download url.
     */
    @Expose
    @NotNull
    @Column(name = "url")
    private String url;
    /**
     * Download category.
     */
    @Expose
    @NotNull
    @Column(name = "category")
    private String categoryId;
    /**
     * The class of a specific implementation of this download formatter. Used when deserializing.
     */
    @Expose
    private String formatterClassStr = "null";
    /**
     * Time when the download was added.
     */
    @Expose
    @Column(name = "addTime")
    private long addTime;
    /**
     * The class of a specific implementation of this download. Used when deserializing.
     */
    @Expose
    @Column(name = "classStr")
    private String classStr;


    public DownloadEntity() {
    }

    public void deserialize(Map<String, Object> dmap) throws Exception {
        final DownloadEntity download = this;

        final String serializableId = (String) dmap.getOrDefault("serializableId", "null");
        final String fileName = (String) dmap.getOrDefault("fileName", "null");
        final String url = (String) dmap.getOrDefault("url", "null");
        final String outputDirStr = (String) dmap.getOrDefault("outputDirStr", "null");
        final String tmpDirStr = (String) dmap.getOrDefault("tmpDirStr", "null");
        final String classStr = (String) dmap.getOrDefault("classStr", "null");
        final double addTime = (Double) dmap.getOrDefault("addTime", 0);
        final String formatterClassStr = (String) dmap.getOrDefault("formatterClassStr", "null");

        final Map<String, Object> cmap = (Map<String, Object>) dmap.getOrDefault("category", new HashMap());
        final String categoryId = (String) cmap.getOrDefault("serializableId", "null");


        download.setCategoryId(categoryId);
        download.setSerializableId(serializableId);
        download.setFileName(fileName);
        download.setUrl(new URL(url));
        download.setOutputDir(new File(outputDirStr));
        download.setTmpDir(new File(tmpDirStr));
        download.setClassStr(classStr);
        download.setAddTime((long) addTime);
        download.setFormatterClassStr(formatterClassStr);
    }


    public String getSerializableId() {
        return serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static String suggestNameFrom(@NotNull String url) {
        String name = "";
        String urlStr = url + "";
        urlStr = urlStr.trim();

        if (urlStr.charAt(url.length() - 1) == '/') {
            urlStr = urlStr.substring(0, urlStr.length() - 1);
        }

        int lIndex = urlStr.lastIndexOf('/');

        if (lIndex >= 0) {
            name = urlStr.substring(lIndex + 1);
        } else {
            name = new Random().nextInt() + "";
        }

        return name.trim();
    }

    public static String getFileName(String fullName) {
        String fileName = fullName.replaceAll("[^\\w\\d\\.\\- ]", "");

        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public File getTmpDir() {
        return new File(tmpDirStr);
    }

    public void setTmpDir(@NotNull final File tmpDir) throws IOException {
        this.tmpDirStr = tmpDir.getCanonicalPath();
    }

    public File getOutputDir() throws IOException {
        return new File(outputDirStr);
    }

    public void setOutputDir(@NotNull final File outputDir) throws IOException {
        this.outputDirStr = outputDir.getCanonicalPath();
    }

    public File getFullTempPathToFile() throws IOException {
        return new File(getTmpDir() + "/" + fileName).getCanonicalFile();
    }

    public File getFullPathToFile() throws IOException {
        String fullPath = getOutputDir() + "/" + getFileName().trim() + "." + getExtension();
        return new File(fullPath).getCanonicalFile();
    }

    public URL getUrl() throws MalformedURLException {
        return new URL(url.toString());
    }

    public void setUrl(URL url) {
        this.url = url.toString();
    }

    public long getAddTime() {
        return addTime;
    }

    protected void setTime(long millis) {
        this.addTime = millis;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public String getFormatterClassStr() {
        return formatterClassStr;
    }

    public void setFormatterClassStr(String formatterClassStr) {
        this.formatterClassStr = formatterClassStr;
    }

    @Override
    public String toString() {
        return this.fileName;
    }
}
