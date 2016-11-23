package com.humming.ascwg.service;

/**
 * Created by Zhtq on 8/5/16.
 */
public class RequestData {
    private String cmd;

    private String token;

    private String language;
    private String appDomain;
    private String appPushToken;
    private String appVersion;
    private String clientIdentifierCode;
    private String locationXy;
    private String resolution;


    private IRequestMainData parameters;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public IRequestMainData getParameters() {
        return parameters;
    }

    public void setParameters(IRequestMainData parameters) {
        this.parameters = parameters;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        if (parameters != null) {
            return "?cmd=" + cmd + "&token=" + token + "&" + parameters.toString();
        } else {
            return "?cmd=" + cmd + "&token=" + token;
        }
    }
}
