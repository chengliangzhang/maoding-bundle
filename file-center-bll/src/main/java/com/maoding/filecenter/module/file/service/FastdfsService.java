package com.maoding.filecenter.module.file.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Wuwq on 2017/06/01.
 */
public interface FastdfsService {
    /**
     * 上传文件
     */
    FastdfsUploadResult upload(MultipartFileParam param) throws Exception;

    FastdfsUploadResult uploadImageAndCrtThumbImage(MultipartFileParam param) throws Exception;

    /**
     * 上传LOGO
     */
    FastdfsUploadResult uploadLogo(MultipartFileParam param) throws Exception;

    /**
     * 从 FastDFS 删除文件
     */
    ApiResult delete(String group, String path);

    /**
     * FastDFS下载文件
     * @param id(文件id)
     */
    void downLoadFile(String id, HttpServletResponse response) throws Exception;

    String getCrtPath(String path) ;
}
