package com.maoding.filecenter.module.file.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.core.exception.DataNotFoundException;
import com.maoding.filecenter.module.file.dto.DeleteDTO;
import com.maoding.filecenter.module.file.dto.DirectoryDTO;
import com.maoding.filecenter.module.file.dto.NetFileRenameDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wuwq on 2017/05/27.
 */
public interface NetFileService {

    /**
     * 创建目录
     */
    ApiResult createDirectory(DirectoryDTO dir) throws DataNotFoundException;

    /**
     * 上传文件
     */
    ApiResult uploadFile(HttpServletRequest request) throws Exception;

    /**
     * 重命名文件
     */
    ApiResult rename(NetFileRenameDTO dto);

    /**
     * 删除文件
     */
    ApiResult delete(DeleteDTO dto);

    /**
     * 查询文件夹顺序
     */
    List<DirectoryDTO> getDirectoryDTOList(DirectoryDTO dir);
}
