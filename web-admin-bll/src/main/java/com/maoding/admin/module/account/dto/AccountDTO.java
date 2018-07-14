package com.maoding.admin.module.account.dto;

/**
 * Created by Wuwq on 2017/07/18.
 */
public class AccountDTO {

    private String account;


    private String name;

    public AccountDTO() {

    }

    public AccountDTO(String name, String account) {
        this.name = name;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
