package com.maoding.storage.service;

import com.maoding.core.base.BaseService;
import com.maoding.storage.dao.StorageDao;
import com.maoding.storage.dto.FdNodeDTO;
import com.maoding.storage.dto.FdNodeQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/12
 * 类名: com.maoding.storage.service.StorageServiceImpl
 * 作者: 张成亮
 * 描述:
 **/
@Service("storageService")
public class StorageServiceImpl extends BaseService implements StorageService {
    @Autowired
    private StorageDao storageDao;

    /**
     * 描述       查询文件列表
     * 日期       2018/9/12
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<FdNodeDTO> listFdNode(FdNodeQueryDTO query) {
        return storageDao.list(query);
    }
}
