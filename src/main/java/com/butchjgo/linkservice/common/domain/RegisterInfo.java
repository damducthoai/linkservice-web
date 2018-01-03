package com.butchjgo.linkservice.common.domain;

public class RegisterInfo {
    private String server,pattern,chanel;

    public RegisterInfo() {
    }

    public RegisterInfo(String server, String pattern, String chanel) {
        this.server = server;
        this.pattern = pattern;
        this.chanel = chanel;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getChanel() {
        return chanel;
    }

    public void setChanel(String chanel) {
        this.chanel = chanel;
    }

    @Override
    public int hashCode() {
        return server.concat(pattern).concat(chanel).hashCode();
    }
}
