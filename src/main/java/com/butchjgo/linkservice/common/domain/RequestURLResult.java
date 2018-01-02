package com.butchjgo.linkservice.common.domain;

public class RequestURLResult {

    private boolean success = true;
    private String id;
    private String msg;

    public RequestURLResult() {
    }

    public RequestURLResult(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
