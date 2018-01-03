package com.butchjgo.linkservice.common.domain;

import java.io.Serializable;

public class RequestData implements Serializable {

    private String id, url, password;
    ;

    public RequestData() {
    }

    public RequestData(String url, String password) {
        this.url = url;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
