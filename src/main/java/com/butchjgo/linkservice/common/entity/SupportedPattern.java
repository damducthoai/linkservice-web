package com.butchjgo.linkservice.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "supported_pattern")
public class SupportedPattern implements Serializable {
    @Id
    @Column(name = "pattern")
    String pattern;

    public SupportedPattern() {
    }

    public SupportedPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public int hashCode() {
        return this.pattern.hashCode();
    }
}
