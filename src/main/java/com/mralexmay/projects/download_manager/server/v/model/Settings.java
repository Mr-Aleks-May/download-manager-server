package com.mralexmay.projects.download_manager.server.v.model;

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

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                '}';
    }
}
