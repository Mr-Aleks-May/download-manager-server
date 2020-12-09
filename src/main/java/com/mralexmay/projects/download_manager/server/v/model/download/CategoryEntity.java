package com.mralexmay.projects.download_manager.server.v.model.download;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * Category id
     */
    private long id;
}
