package com.butchjgo.linkservice.common.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "bad_url")
@NoArgsConstructor
@Getter
@Setter
public class BadURL implements Serializable {
    @Id
    @Column(name = "url")
    private String url;

    public BadURL(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
}
