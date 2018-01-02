package com.butchjgo.linkservice.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "bad_url")
public class BadURL implements Serializable {
    @Id
    @Column(name = "url")
    private String url;

    public BadURL() {
    }

    public BadURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
}
