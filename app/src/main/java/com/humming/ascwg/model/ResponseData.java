package com.humming.ascwg.model;

/**
 * Created by Zhtq on 16/8/11.
 */
public class ResponseData {
    private int statusCode;

    public ResponseData() {
    }

    public ResponseData(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
