package com.butchjgo.linkservice.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "supported_pattern")
public class SupportedPattern implements Serializable {
    @Id
    @Column(name = "pattern")
    String pattern;

    @NotNull
    @Column(name = "chanel")
    String chanel;

    public SupportedPattern() {
    }

    public SupportedPattern(String pattern, String chanel) {
        this.pattern = pattern;
        this.chanel = chanel;
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

    public String getChanel() {
        return chanel;
    }

    public void setChanel(String chanel) {
        this.chanel = chanel;
    }
}
