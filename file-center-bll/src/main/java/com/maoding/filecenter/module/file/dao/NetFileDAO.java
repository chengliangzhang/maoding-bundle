package com.maoding.filecenter.module.file.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.filecenter.module.file.dto.DirectoryDTO;
import com.maoding.filecenter.module.file.dto.NetFileDTO;
import com.maoding.filecenter.module.file.dto.NetFileQueryDTO;
import com.maoding.filecenter.module.file.model.NetFileDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wuwq on 2017/05/31.
 */
@Repository
public interface NetFileDAO extends BaseDao<NetFileDO> {
    /**
     * 查询文件夹顺序
     */
    List<DirectoryDTO> getDirectoryDTOList(DirectoryDTO dir);

    /**
     * 查询文件及文件夹
     */
    List<DirectoryDTO> getNetFileOrDir(DirectoryDTO dir);

    List<NetFileDO> getNetFileByIds(NetFileDTO dto);

    List<NetFileDO> listNetFile(NetFileQueryDTO query);

}
