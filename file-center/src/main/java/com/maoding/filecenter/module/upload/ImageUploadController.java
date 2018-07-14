package com.maoding.filecenter.module.upload;

import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsCutImageRequest;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;
import com.maoding.fastdfsClient.domain.MateData;
import com.maoding.fastdfsClient.domain.StorePath;
import com.maoding.fastdfsClient.exception.FdfsServerException;
import com.maoding.fastdfsClient.proto.storage.DownloadByteArray;
import com.maoding.fastdfsClient.service.FastFileStorageClient;
import com.maoding.filecenter.module.file.service.FastdfsService;
import com.maoding.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * Created by Wuwq on 2016/12/18.
 */
@RestController
@RequestMapping("/fileCenter")
public class ImageUploadController extends BaseController {

    private final static String IMAGE_UPLOAD_TEMP_DIR = "/uploadTempImage/";

    private final static Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    private final static String CUT_DELETE_GROUP = "cut_deleteGroup";
    private final static String CUT_DELETE_PATH = "cut_deletePath";

    private final static String ZOOM_WIDTH = "zoomWidth";
    private final static String ZOOM_HEIGHT = "zoomHeight";
    private final static String ZOOM_KEEP_RATIO = "zoomKeepRatio";

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FastdfsService fastdfsService;

    /**
     * 上传临时图片并转换为JPG（服务器本地）
     */
    @RequestMapping(value = "uploadTempImage", method = RequestMethod.POST)
    public ApiResult uploadTempImage(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);

        String tempFileName = StringUtils.buildUUID() + ".jpg";

        File tmpDir = new File(IMAGE_UPLOAD_TEMP_DIR);
        if (!tmpDir.exists()) {
            if (!tmpDir.mkdirs())
                return ApiResult.failed("上传失败，无法创建临时目录", null);
        }

        File tmpFile = new File(IMAGE_UPLOAD_TEMP_DIR, tempFileName);

        //转换JPG
        Thumbnails
                .of(param.getFileItem().getInputStream())
                .scale(1f)
                .outputQuality(0.9f)
                .outputFormat("jpg")
                .toFile(tmpFile);

        return ApiResult.success(null, tempFileName);
    }


    /**
     * 上传LOGO（FastDFS）
     */
    @RequestMapping(value = "fastUploadLogo", method = RequestMethod.POST)
    public ApiResult fastUploadLogo(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        if (param.isChunked())
            throw new UnsupportedOperationException("该上传不支持分片模式");

        Builder builder = Thumbnails
                .of(param.getFileItem().getInputStream());

        //缩放
        String zoomWidth = Optional.ofNullable((String) param.getParam().get(ZOOM_WIDTH)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        String zoomHeight = Optional.ofNullable((String) param.getParam().get(ZOOM_HEIGHT)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        Boolean zoomKeepRatio = Optional.ofNullable((Boolean) param.getParam().get(ZOOM_KEEP_RATIO)).filter(s -> s!=null&&!s.equals("null")).orElse(true);


        if (zoomWidth == null && zoomHeight != null) {
            builder = builder.width(Integer.parseInt(zoomWidth));
        } else if (zoomWidth != null && zoomHeight == null) {
            builder = builder.height(Integer.parseInt(zoomHeight));
        } else if (zoomWidth != null && zoomHeight != null) {
            builder = builder.size(Integer.parseInt(zoomWidth), Integer.parseInt(zoomHeight));
        }else {
            builder = builder.scale(1f);
        }
        builder = builder.keepAspectRatio(zoomKeepRatio);

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
        FastdfsUploadResult r = new FastdfsUploadResult();
        r.setUploadId(param.getUploadId());
        r.setFileName(param.getFileName());
        r.setFileExtName(param.getFileExtName());
        r.setFastdfsGroup(storePath.getGroup());
        r.setFastdfsPath(storePath.getPath());
        r.setFileSize(param.getSize());

        return ApiResult.success(null, r);
    }


    /**
     * 上传图片（FastDFS）
     */
    @RequestMapping(value = "fastUploadImage", method = RequestMethod.POST)
    public ApiResult fastUploadImage(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        if (param.isChunked())
            throw new UnsupportedOperationException("该上传不支持分片模式");

        //转换
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String extName = "jpg";
        if (param.getFileExtName().equalsIgnoreCase("png"))
            extName = "png";
        Thumbnails
                .of(param.getFileItem().getInputStream())
                .scale(1f)
                .outputQuality(0.9f)
                .outputFormat(extName)
                .toOutputStream(os);

        //作为原图的从文件上传
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        long fileSize = os.size();
        os.close();

        //判断是否存在"裁剪时成功时要删掉的图"（一般指上一个头像之类）
        String cutDelGroup = Optional.ofNullable((String) param.getParam().get(CUT_DELETE_GROUP)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        String cutDelPath = Optional.ofNullable((String) param.getParam().get(CUT_DELETE_PATH)).filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).orElse(null);
        Set<MateData> metas = null;
        if (cutDelPath != null) {
            metas = new HashSet<>();
            metas.add(new MateData(CUT_DELETE_GROUP, cutDelGroup));
            metas.add(new MateData(CUT_DELETE_PATH, cutDelPath));
        }

        StorePath storePath = storageClient.uploadFile(is, fileSize, extName, metas);

        FastdfsUploadResult r = new FastdfsUploadResult();
        r.setUploadId(param.getUploadId());
        r.setFileName(param.getFileName());
        r.setFileExtName(param.getFileExtName());
        r.setFastdfsGroup(storePath.getGroup());
        r.setFastdfsPath(storePath.getPath());
        r.setFileSize(param.getSize());

        return ApiResult.success(null, r);
    }

    /**
     * 裁剪图片并替换原图（此接口时不需要上传文件，但需要FastDFS已存在原图，并提交相应裁剪参数）
     */
    @RequestMapping(value = "fastCutImage", method = RequestMethod.POST)
    public ApiResult fastCutImage(@RequestBody FastdfsCutImageRequest request) throws Exception {

        String group = request.getGroup();
        String path = request.getPath();
        int x = request.getX();
        int y = request.getY();
        int width = request.getWidth();
        int height = request.getHeight();
        int zoomWidth = request.getZoomWidth();
        int zoomHeight = request.getZoomHeight();
        boolean zoomKeepRatio=request.isZoomKeepRatio();

        //取出原图
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());

        Builder builder = Thumbnails
                .of(new ByteArrayInputStream(bytes));

        //裁剪缩放
        builder = cutAndZoom(builder, x, y, width, height, zoomWidth, zoomHeight, zoomKeepRatio);
        String extName = "jpg";
        if (request.getFileExtName().equalsIgnoreCase("png"))
            extName = "png";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        builder.outputQuality(1.0f)
                .outputFormat(extName)
                .toOutputStream(os);

        //上传最终图
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        long fileSize = os.size();
        os.close();
        StorePath storePath = storageClient.uploadFile(group, is, fileSize, extName);

        //裁剪时成功时要删掉的图（一般指上一个头像之类）
        deleteOnCut(group, path);

        //删除原图
        storageClient.deleteFile(group, path);

        //处理文件名
        String cutPrefixName = request.getCutPrefixName();
        String fileName = generateFileName(request.getFileName(), cutPrefixName, extName);

        FastdfsUploadResult r = new FastdfsUploadResult();
        r.setFileSize(fileSize);
        r.setFileName(fileName);
        r.setFileExtName(request.getFileExtName());
        r.setFastdfsGroup(storePath.getGroup());
        r.setFastdfsPath(storePath.getPath());
        r.setPrefixName(cutPrefixName);

        return ApiResult.success(null, r);
    }

    /**
     * 裁剪图片并作为从文件上传到FastDFS（此接口时不需要上传文件，但需要FastDFS已存在原图，并提交相应裁剪参数）
     */
    @RequestMapping(value = "fastCutImageAsSlaveFile", method = RequestMethod.POST)
    public ApiResult fastCutImageAsSlaveFile(@RequestBody FastdfsCutImageRequest request) throws Exception {

        String group = request.getGroup();
        String path = request.getPath();
        int x = request.getX();
        int y = request.getY();
        int width = request.getWidth();
        int height = request.getHeight();
        int zoomWidth = request.getZoomWidth();
        int zoomHeight = request.getZoomHeight();
        boolean zoomKeepRatio=request.isZoomKeepRatio();

        //取出原图
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());

        Builder<? extends InputStream> builder = Thumbnails
                .of(new ByteArrayInputStream(bytes));

        //裁剪缩放
        builder = cutAndZoom(builder, x, y, width, height, zoomWidth, zoomHeight,zoomKeepRatio);
        String extName = "jpg";
        if (request.getFileExtName().equalsIgnoreCase("png"))
            extName = "png";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        builder.outputQuality(1.0f)
                .outputFormat(extName)
                .toOutputStream(os);

        //作为原图的从文件上传
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        long fileSize = os.size();
        os.close();
        String cutPrefixName = request.getCutPrefixName();
        StorePath storePath = storageClient.uploadSlaveFile(group, path, is, fileSize, cutPrefixName, request.getFileExtName());

        //裁剪时成功时要删掉的图（一般指上一个头像之类）
        deleteOnCut(group, path);

        //处理文件名
        String fileName = generateFileName(request.getFileName(), cutPrefixName, extName);

        FastdfsUploadResult r = new FastdfsUploadResult();
        r.setFileSize(fileSize);
        r.setFileName(fileName);
        r.setFileExtName(request.getFileExtName());
        r.setFastdfsGroup(storePath.getGroup());
        r.setFastdfsPath(storePath.getPath());
        r.setPrefixName(cutPrefixName);

        return ApiResult.success(null, r);
    }

    //裁剪时成功时要删掉的图（一般指上一个头像之类）
    private void deleteOnCut(String group, String path) {
        try {
            Set<MateData> metas = storageClient.getMetadata(group, path);
            if (metas != null) {
                MateData cutDelGroup = metas.stream().filter(m -> m.getName().equalsIgnoreCase(CUT_DELETE_GROUP)).findFirst().orElse(null);

                MateData cutDelPath = metas.stream().filter(m -> m.getName().equalsIgnoreCase(CUT_DELETE_PATH)).findFirst().orElse(null);

                String delGroup = cutDelGroup.getValue();
                String delPath = cutDelPath.getValue();
                if (StringUtils.isNotBlank(delGroup) && !delGroup.equals("null")
                        && StringUtils.isNotBlank(delPath) && !delPath.equals("null")) {

                    storageClient.deleteFile(cutDelGroup.getValue(), cutDelPath.getValue());

                } else if ((StringUtils.isBlank(delGroup) || delGroup.equals("null"))
                        && StringUtils.isNotBlank(delPath) && !delPath.equals("null")) {

                    if (StringUtils.startsWith(delPath, "group") || StringUtils.startsWith(delPath, "/group"))

                        storageClient.deleteFile(delPath);
                }
            }
        } catch (FdfsServerException ex) {
            logger.error(ex.getMessage());
        }
    }

    //处理文件名
    private String generateFileName(String fileName, String prefix, String extName) {
        String name;
        if (StringUtils.isBlank(fileName))
            name = StringUtils.buildUUID();
        else {
            int extIndex = fileName.lastIndexOf(".");
            if (extIndex != -1)
                name = fileName.substring(0, extIndex - 1);
            else
                name = fileName;
        }

        return name + prefix + "." + extName;
    }

    //裁剪缩放
    private Builder<? extends InputStream> cutAndZoom(Builder<? extends InputStream> builder, int x, int y, int width, int height, int zoomWidth, int zoomHeight,boolean zoomKeepRatio) {
        //裁剪转换
        builder = builder.sourceRegion(x, y, width, height);

        //缩放
        if (zoomWidth != 0 && zoomHeight == 0) {
            builder = builder.width(zoomWidth);
        } else if (zoomWidth == 0 && zoomHeight != 0) {
            builder = builder.height(zoomHeight);
        } else if (zoomWidth != 0 && zoomHeight != 0) {
            builder = builder.size(zoomWidth, zoomHeight);
        }else {
            builder = builder.scale(1f);
        }
        builder = builder.keepAspectRatio(zoomKeepRatio);

       /* //判断是否需要缩放
        if (zoomWidth != 0 && zoomHeight == 0) {
            //以宽度为准
            if (width > zoomWidth)
                builder = builder.size(zoomWidth, (zoomWidth / width) * height);
        } else if (zoomWidth == 0 && zoomHeight != 0) {
            //以高度为准
            if (height > zoomHeight)
                builder = builder.size((zoomHeight / height) * width, zoomHeight);
        } else if (zoomWidth != 0 && zoomHeight != 0) {
            //强制宽度高度
            builder = builder.forceSize(zoomWidth, zoomHeight);
        } else {
            builder = builder.scale(1f);
        }*/

        return builder;
    }


    @RequestMapping(value = "downLoadFile/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void downLoadFile(@PathVariable String id, HttpServletResponse response) throws Exception {
         fastdfsService.downLoadFile(id,response);
    }
}
