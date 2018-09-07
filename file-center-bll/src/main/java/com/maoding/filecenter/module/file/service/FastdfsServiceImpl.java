package com.maoding.filecenter.module.file.service;

import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;
import com.maoding.fastdfsClient.FdfsClientConstants;
import com.maoding.fastdfsClient.domain.FileInfo;
import com.maoding.fastdfsClient.domain.StorePath;
import com.maoding.fastdfsClient.domain.ThumbImageConfig;
import com.maoding.fastdfsClient.proto.storage.DownloadByteArray;
import com.maoding.fastdfsClient.service.AppendFileStorageClient;
import com.maoding.fastdfsClient.service.FastFileStorageClient;
import com.maoding.filecenter.module.file.dao.NetFileDAO;
import com.maoding.filecenter.module.file.model.NetFileDO;
import com.maoding.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;

/**
 * Created by Wuwq on 2017/06/01.
 */
@Service("fastdfsService")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class FastdfsServiceImpl extends BaseService implements FastdfsService {

    private final static String ZOOM_WIDTH = "zoomWidth";
    private final static String ZOOM_HEIGHT = "zoomHeight";
    private final static String ZOOM_KEEP_RATIO = "zoomKeepRatio";

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private AppendFileStorageClient appendStorageClient;

    /**
     * 缩略图生成配置
     */
    @Resource
    private ThumbImageConfig thumbImageConfig;

    @Autowired
    private NetFileDAO netFileDAO;
    /**
     * 上传文件
     */
    @Override
    public FastdfsUploadResult upload(MultipartFileParam param) throws Exception {
        String extName = param.getFileExtName();
        FileItem fileItem = param.getFileItem();
        long size = fileItem.getSize();

        FastdfsUploadResult fuResult;

        //判断是否分片上传
        if (param.isChunked()) {

            //分片大小必须和前端保持一致
            if (param.getChunkPerSize() == null || param.getChunkPerSize().equals(0L))
                throw new UnsupportedOperationException("没有指定分片大小");

            int chunk = param.getChunk();
            StorePath storePath=null;
            if (chunk == 0) {
                //TODO 当FastDFS服务器存在多个组的时候需要分配逻辑
                 storePath = appendStorageClient.uploadAppenderFile(FdfsClientConstants.DEFAULT_GROUP, fileItem.getInputStream(), fileItem.getSize(), extName);
                fuResult = FastdfsUploadResult.parse(param, storePath.getGroup(), storePath.getPath());
                fuResult.setNeedFlow(true);
                return fuResult;
            }
            // 上传第二段文字
            String secondText = "This is second paragraph. \r\n";
            InputStream secondIn = getTextInputStream(secondText);
            //分片叠加
            long offset = param.getChunkPerSize() * param.getChunk().longValue();
            String group = param.getFastdfsGroup();
            String path = param.getFastdfsPath();
            appendStorageClient.modifyFile(group, path, fileItem.getInputStream(), size, offset);

//            appendStorageClient.appendFile(group, path, secondIn, offset);

            //非最后一块分片
            if (chunk < param.getChunks() - 1) {
                fuResult = FastdfsUploadResult.parse(param, group, path);
                fuResult.setNeedFlow(true);
                return fuResult;
            }

            //TODO 要考虑通过限时缓存来计算文件大小，减少消耗
            FileInfo fileInfo = appendStorageClient.queryFileInfo(group, path);
            fuResult = FastdfsUploadResult.parse(param, group, path);
            fuResult.setFileSize(fileInfo.getFileSize());
        } else {
            //非分片上传
            StorePath storePath = storageClient.uploadFile(fileItem.getInputStream(), size, extName, null);
            fuResult = FastdfsUploadResult.parse(param, storePath.getGroup(), storePath.getPath());
        }
        fuResult.setNeedFlow(false);
        return fuResult;
    }

    @Override
    public FastdfsUploadResult uploadImageAndCrtThumbImage(MultipartFileParam param) throws Exception {
        Thumbnails.Builder builder = Thumbnails.of(param.getFileItem().getInputStream());

        //转换
        String extName = "jpg";
        if (param.getFileExtName().equalsIgnoreCase("png"))
            extName = "png";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        builder = builder.scale(1f);
        builder.outputQuality(0.9f).
                outputFormat(extName).
                toOutputStream(os);

        //上传最终图
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        long fileSize = os.size();
        os.close();

        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(is, fileSize, extName, null);
        FastdfsUploadResult fuResult = new FastdfsUploadResult();
        fuResult.setUploadId(param.getUploadId());
        fuResult.setFileName(param.getFileName());
        fuResult.setFileExtName(param.getFileExtName());
        fuResult.setFastdfsGroup(storePath.getGroup());
        fuResult.setFastdfsPath(storePath.getPath());
        fuResult.setFileSize(param.getSize());
        fuResult.setNeedFlow(false);

        return fuResult;
    }

    private InputStream getTextInputStream(String text) throws Exception {
        return new ByteArrayInputStream(text.getBytes("UTF-8"));
    }

    public void inputStreamTofile(InputStream ins,File file) throws Exception{
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    public void inputstreamtofile(InputStream ins,File file) throws Exception{
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    /**
     * 上传LOGO
     */
    public FastdfsUploadResult uploadLogo(MultipartFileParam param) throws Exception {
        Thumbnails.Builder builder = Thumbnails.of(param.getFileItem().getInputStream());

        //缩放
        String zoomWidth = Optional.ofNullable((String) param.getParam().get(ZOOM_WIDTH)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        String zoomHeight = Optional.ofNullable((String) param.getParam().get(ZOOM_HEIGHT)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        Boolean zoomKeepRatio = Optional.ofNullable((Boolean) param.getParam().get(ZOOM_KEEP_RATIO)).filter(s -> s != null && !s.equals("null")).orElse(true);


        if (zoomWidth == null && zoomHeight != null) {
            builder = builder.width(Integer.parseInt(zoomWidth));
            builder = builder.keepAspectRatio(zoomKeepRatio);
        } else if (zoomWidth != null && zoomHeight == null) {
            builder = builder.height(Integer.parseInt(zoomHeight));
            builder = builder.keepAspectRatio(zoomKeepRatio);
        } else if (zoomWidth != null && zoomHeight != null) {
            builder = builder.size(Integer.parseInt(zoomWidth), Integer.parseInt(zoomHeight));
            builder = builder.keepAspectRatio(zoomKeepRatio);
        } else {
            builder = builder.scale(1f);
        }


        //转换
        String extName = "jpg";
        if (param.getFileExtName().equalsIgnoreCase("png"))
            extName = "png";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        builder.outputQuality(0.9f).
                outputFormat(extName).
                toOutputStream(os);

        //上传最终图
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        long fileSize = os.size();
        os.close();

        StorePath storePath = storageClient.uploadFile(is, fileSize, extName, null);
        FastdfsUploadResult fuResult = new FastdfsUploadResult();
        fuResult.setUploadId(param.getUploadId());
        fuResult.setFileName(param.getFileName());
        fuResult.setFileExtName(param.getFileExtName());
        fuResult.setFastdfsGroup(storePath.getGroup());
        fuResult.setFastdfsPath(storePath.getPath());
        fuResult.setFileSize(param.getSize());
        fuResult.setNeedFlow(false);

        return fuResult;
    }

    /**
     * 从 FastDFS 删除文件
     */
    @Override
    public ApiResult delete(String group, String path) {
        if (StringUtils.isBlank(path))
            throw new NullPointerException("Path不能为空");

        if (StringUtils.isNotBlank(group))
            storageClient.deleteFile(group, path);
        else if (StringUtils.isBlank(group) && (StringUtils.startsWith(path, "group") || StringUtils.startsWith(path, "/group"))) {
            storageClient.deleteFile(path);
        } else {
            return ApiResult.failed("指定删除文件的参数无效", null);
        }
        return ApiResult.success(null, null);
    }

    @Override
    public void downLoadFile(String id, HttpServletResponse response) throws Exception {


        NetFileDO file = netFileDAO.selectByPrimaryKey(id);
        if(file!=null){
            String fileName = file.getFileName();
            if(file.getFileName().indexOf(".")<0){
                fileName = fileName+"."+file.getFileExtName();
            }

            //建立传输通道
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            OutputStream os = response.getOutputStream();

            FileInfo info = storageClient.queryFileInfo(file.getFileGroup(),file.getFilePath());

            storageClient.downloadFile(file.getFileGroup(),file.getFilePath(),new DownloadByteArray(os,info));
        }
    }

    @Override
    public String getCrtPath(String path) {
        String suffix = path.substring(path.lastIndexOf("."),path.length());
        String thumbPath = path.substring(0,path.lastIndexOf("."));
        return thumbPath+thumbImageConfig.getPrefixName()+suffix;
    }
}
