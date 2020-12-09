package com.mralexmay.projects.download_manager.server.v4.model;

import javax.persistence.*;

@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    public Settings() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
