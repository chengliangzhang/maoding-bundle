package com.maoding.corp.module.corpclient.dto;

import java.util.List;

/**
 * Created by Idccapp21 on 2017/3/14.
 */
public class CoUsersDTO {
    private List<String> statuss;
    private List<String> ids;
    private List<String> names;
    private List<String> loginIds;

    public List<String> getStatuss() {
        return statuss;
    }

    public void setStatuss(List<String> statuss) {
        this.statuss = statuss;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getLoginIds() {
        return loginIds;
    }

    public void setLoginIds(List<String> loginIds) {
        this.loginIds = loginIds;
    }
}
