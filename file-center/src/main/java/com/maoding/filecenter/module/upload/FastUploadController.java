package com.maoding.filecenter.module.upload;

import com.maoding.config.MultipartConfig;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;
import com.maoding.fastdfsClient.FdfsClientConstants;
import com.maoding.fastdfsClient.domain.FileInfo;
import com.maoding.fastdfsClient.domain.StorePath;
import com.maoding.fastdfsClient.service.AppendFileStorageClient;
import com.maoding.fastdfsClient.service.FastFileStorageClient;
import com.maoding.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * Created by Wuwq on 2016/12/18.
 */
@RestController
@RequestMapping("/fileCenter")
public class FastUploadController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(FastUploadController.class);

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private AppendFileStorageClient appendStorageClient;

    /**
     * 上传(FastDFS)
     */
    @RequestMapping(value = "fastUpload", method = RequestMethod.POST)
    public ApiResult fastUpload(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String extName = param.getFileExtName();
        FileItem fileItem = param.getFileItem();
        long size = fileItem.getSize();
        if (param.isChunked()) {
            //分片上传
            int chunk = param.getChunk();
            if (chunk == 0) {
                //TODO 当FastDFS服务器存在多个组的时候需要分配逻辑
                StorePath storePath = appendStorageClient.uploadAppenderFile(FdfsClientConstants.DEFAULT_GROUP, fileItem.getInputStream(), fileItem.getSize(), extName);
                return ApiResult.success(null, FastdfsUploadResult.parse(param, storePath.getGroup(), storePath.getPath()));
            }

            //分片叠加（分片大小必须和前端保持一致）
            long offset = MultipartConfig.ChunkSize * param.getChunk();
            String group = param.getFastdfsGroup();
            String path = param.getFastdfsPath();
            appendStorageClient.modifyFile(group, path, fileItem.getInputStream(), size, offset);

            //非最后一片
            if (chunk < param.getChunks() - 1) {
                return ApiResult.success(null, FastdfsUploadResult.parse(param, group, path));
            }

            //TODO 要考虑通过限时缓存来计算文件大小，减少消耗
            FileInfo fileInfo = appendStorageClient.queryFileInfo(group, path);
            FastdfsUploadResult r = FastdfsUploadResult.parse(param, group, path);
            r.setFileSize(fileInfo.getFileSize());
            return ApiResult.success(null, r);

        } else {
            //非分片上传
            StorePath storePath = storageClient.uploadFile(fileItem.getInputStream(), size, extName, null);
            return ApiResult.success(null, FastdfsUploadResult.parse(param, storePath.getGroup(), storePath.getPath()));
        }
    }

    /**
     * 删除文件(FastDFS)
     */
    @RequestMapping(value = "fastDelete", method = RequestMethod.POST)
    public ApiResult fastDelete(@RequestBody Map<String, Object> param) {
        String group = (String) param.get("group");
        String path = (String) param.get("path");
        if (StringUtils.isBlank(path))
            throw new NullPointerException("Path不能为空");

        if (StringUtils.isNotBlank(group))
            storageClient.deleteFile(group, path);
        else if (StringUtils.isBlank(group) &&
                (StringUtils.startsWith(path, "group") || StringUtils.startsWith(path, "/group"))) {
            storageClient.deleteFile(path);
        } else {
            return ApiResult.failed("指定删除文件的参数无效", null);
        }

        return ApiResult.success(null, null);
    }
}
