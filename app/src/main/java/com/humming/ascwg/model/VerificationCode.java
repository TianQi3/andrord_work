package com.humming.ascwg.model;

/**
 * Created by Zhtq on 16/5/23.
 */
public class VerificationCode {
    private String verifyCode;
    private String userId;
    private String name;
    private String token;
    private String tokenInvalidTime;
    private String accountType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenInvalidTime() {
        return tokenInvalidTime;
    }

    public void setTokenInvalidTime(String tokenInvalidTime) {
        this.tokenInvalidTime = tokenInvalidTime;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public VerificationCode() {
    }

    public VerificationCode(String verifyCode, String userId) {
        this.verifyCode = verifyCode;
        this.userId = userId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
