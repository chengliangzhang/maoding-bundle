package com.maoding.storage.service;

import com.maoding.storage.dto.FdNodeDTO;
import com.maoding.storage.dto.FdNodeQueryDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/12
 * 类名: com.maoding.storage.service.StorageService
 * 作者: 张成亮
 * 描述:
 **/
public interface StorageService {
    /**
     * 描述       查询文件列表
     * 日期       2018/9/12
     * @author   张成亮
     **/
    List<FdNodeDTO> listFdNode(FdNodeQueryDTO query);
}
