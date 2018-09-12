package com.maoding.admin.module.orgAuth.dto;

import com.maoding.core.base.CorePageDTO;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
public class OrgAuthPageDTO extends CorePageDTO<OrgAuthDTO> {
    /** 维持兼容性 **/
    /** 列表数据 **/
    private List<OrgAuthDTO> list;

    public List<OrgAuthDTO> getList() {
        return (list == null) ? getData() : list;
    }

    public void setList(List<OrgAuthDTO> list) {
        this.list = list;
        setData(list);
    }
}
