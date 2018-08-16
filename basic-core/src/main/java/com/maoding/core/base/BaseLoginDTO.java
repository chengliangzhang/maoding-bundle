package com.maoding.core.base;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 登录用户的账号及基本信息
 */
public class BaseLoginDTO implements Serializable{
    /** 登录用户名 **/
    private String cellphone;

    /** 登录密码 **/
    private String password;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
