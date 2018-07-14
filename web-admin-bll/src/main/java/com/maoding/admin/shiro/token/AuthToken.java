package com.maoding.admin.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by Wuwq on 2017/07/18.
 */
public class AuthToken implements AuthenticationToken {
    private String account;
    private String password;
    private String token;

    public AuthToken(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
