package com.butchjgo.linkservice.common.domain;

public class ResultData {
    private String id, result;

    public ResultData() {
    }

    public ResultData(String id, String result) {
        this.id = id;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
