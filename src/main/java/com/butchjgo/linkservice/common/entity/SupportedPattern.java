package com.butchjgo.linkservice.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
@Table(name = "supported_pattern")
@Getter
@Setter
public class SupportedPattern implements Serializable {

    @Id
    @Column(name = "pattern")
    String pattern;

    @NotNull
    @Column(name = "chanel")
    String chanel;

    @Transient
    Pattern compiledPattern;

    public SupportedPattern(String pattern, String chanel) {
        this.pattern = pattern;
        this.chanel = chanel;
        compiledPattern = Pattern.compile(pattern);
    }

    @Override
    public int hashCode() {
        return this.pattern.hashCode();
    }
}
