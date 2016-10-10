package com.humming.ascwg.service;

/**
 * Created by Zhtq on 8/5/16.
 */
public class RequestData {
    private String cmd;

    private String token;

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

    @Override
    public String toString() {
        if (parameters != null) {
            return "?cmd=" + cmd + "&token=" + token + "&" + parameters.toString();
        } else {
            return "?cmd=" + cmd + "&token=" + token;
        }
    }
}
