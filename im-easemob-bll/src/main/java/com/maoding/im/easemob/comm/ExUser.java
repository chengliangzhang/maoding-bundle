package com.maoding.im.easemob.comm;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.User;

/**
 * 扩展官方SDK的User,增加昵称字段
 */
public class ExUser extends User {
    @SerializedName("nickname")
    private String nickname = null;

    public ExUser nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public ExUser username(String username) {
        setUsername(username);
        return this;
    }

    public ExUser password(String password) {
        setPassword(password);
        return this;
    }

    /**
     * Get nickname
     * @return nickname
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
