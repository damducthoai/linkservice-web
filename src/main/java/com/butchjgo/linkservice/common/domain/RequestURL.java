package com.butchjgo.linkservice.common.domain;

public class RequestURL extends RequestData {

    private String id;

    public RequestURL() {
        super();
    }

    public RequestURL(String url) {
        super(url);
    }

    public RequestURL(String url, String password) {
        super(url, password);
    }

    public RequestURL(String url, String password, String id) {
        super(url, password);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public void setUrl(String url) {
        super.setUrl(url);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public int hashCode() {
        return this.id.concat(super.url).concat(super.password).hashCode();
    }

}
