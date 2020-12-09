package com.mralexmay.projects.download_manager.server.v4.model;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.category.Category;

import javax.persistence.*;
import java.net.URL;

@Entity
@Table(name = "downloads")
public class DownloadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * Download id
     */
    private long id;
    private String path;
    @Expose
    private String serializableId = "null";
    /**
     * Download file name.
     */
    @Expose
    @NotNull
    private String fileName;
    /**
     * File extension.
     */
    @Expose
    @NotNull
    private String extension;
    /**
     * Total size.
     */
    @Expose
    private long fullSize;
    /**
     * Now loaded bytes.
     */
    @Expose
    private long currentSize;
    /**
     * A folder for temporary storage of data during download.
     */
    @Expose
    @NotNull
    private String tmpDirStr;
    /**
     * The downloaded file will be moved to this directory.
     */
    @Expose
    @NotNull
    private String outputDirStr;
    /**
     * Download url.
     */
    @Expose
    @NotNull
    private URL url;
    /**
     * The class of a specific implementation of this authorizationData. Used when deserializing.
     */
    @Expose
    private String authorizationDataClassStr = "null";
    /**
     * Download category.
     */
    @Expose
    @NotNull
    private Category category;
    /**
     * The class of a specific implementation of this download formatter. Used when deserializing.
     */
    @Expose
    private String formatterClassStr = "null";
    /**
     * Time when the download was added.
     */
    @Expose
    private long addTime;
    /**
     * The class of a specific implementation of this download. Used when deserializing.
     */
    @Expose
    private String classStr;

    public DownloadEntity() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
