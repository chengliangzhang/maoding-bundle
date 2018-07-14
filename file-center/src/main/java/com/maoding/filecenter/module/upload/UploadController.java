///*
//package com.maoding.filecenter.module.upload;
//
//import com.maoding.config.MultipartConfig;
//import com.maoding.core.base.BaseController;
//import com.maoding.core.bean.ApiResult;
//import com.maoding.core.bean.MultipartFileParam;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.RandomAccessFile;
//import java.util.concurrent.atomic.AtomicLong;
//
//
//*/
///**
// * Created by Wuwq on 2016/12/18.
// *//*
//
//@RestController
//@RequestMapping("/fileCenter")
//public class UploadController extends BaseController {
//    private final static Logger logger = LoggerFactory.getLogger(UploadController.class);
//
//    */
///**
//     * 普通上传(服务器本地）
//     *//*
//
//    @RequestMapping(value = "localUpload", method = RequestMethod.POST)
//    public ApiResult localUpload(HttpServletRequest request, HttpServletResponse resp) throws Exception {
//        MultipartFileParam param = MultipartFileParam.parse(request);
//
//        String finalDirPath = "/localUpload/";
//        String tempDirPath = finalDirPath + param.getUploadId();
//        String tempFileName = param.getFileName();
//
//        File tmpDir = new File(tempDirPath);
//        File tmpFile = new File(tempDirPath, tempFileName);
//        if (!tmpDir.exists()) {
//            if(!tmpDir.mkdirs())
//                return ApiResult.failed("上传失败，无法创建临时目录",null);
//        }
//
//        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
//        accessTmpFile.write(param.getFileItem().get());
//        accessTmpFile.close();
//        return ApiResult.success("", null);
//    }
//
//    */
///**
//     * 分片上传(服务器本地）
//     *//*
//
//    @RequestMapping(value = "localUploadOnChunked", method = RequestMethod.POST)
//    public ApiResult localUploadOnChunked(HttpServletRequest request, HttpServletResponse resp) throws Exception {
//
//        MultipartFileParam param = MultipartFileParam.parse(request);
//
//        //这个必须与前端设定的值一致
//        long chunkSize = MultipartConfig.getChunkPerSize();
//
//        String finalDirPath = "/localUpload/";
//        String tempDirPath = finalDirPath + param.getUploadId();
//        String tempFileName = param.getFileName();
//
//        File tmpDir = new File(tempDirPath);
//        File tmpFile = new File(tempDirPath, tempFileName);
//        if (!tmpDir.exists()) {
//            if(!tmpDir.mkdirs())
//                return ApiResult.failed("上传失败，无法创建临时目录",null);
//        }
//
//        File confFile = new File(tempDirPath, param.getFileName() + ".conf");
//
//
//        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
//        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
//
//        long offset = chunkSize * param.getChunk();
//        accessTmpFile.seek(offset);  //定位到该分片的偏移量
//        accessTmpFile.write(param.getFileItem().get()); //写入该分片数据
//
//        accessConfFile.setLength(param.getChunks());
//        accessConfFile.seek(param.getChunk());
//        accessConfFile.write(Byte.MAX_VALUE);
//
//        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
//        byte[] completeList = FileUtils.readFileToByteArray(confFile);
//        byte isComplete = Byte.MAX_VALUE;
//        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
//            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
//            isComplete = (byte) (isComplete & completeList[i]);
//        }
//
//        if (isComplete == Byte.MAX_VALUE) {
//            logger.debug("upload complete !!");
//        }
//        accessTmpFile.close();
//        accessConfFile.close();
//
//        return ApiResult.success("", null);
//    }
//}
//*/
