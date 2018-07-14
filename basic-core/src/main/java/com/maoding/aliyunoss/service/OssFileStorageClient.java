//package com.maoding.aliyunoss.service;
//
//import com.aliyun.oss.model.AppendObjectResult;
//import com.aliyun.oss.model.CompleteMultipartUploadResult;
//import com.aliyun.oss.model.PutObjectResult;
//import com.maoding.fastdfsClient.domain.MateData;
//
//import java.io.File;
//import java.io.InputStream;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 面向普通应用的文件操作接口封装
// *
// * @author tobato
// */
//public interface OssFileStorageClient extends GenerateStorageClient {
//
//    /**
//     * 上传一般文件
//     *
//     */
//    PutObjectResult uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet);
//
//    /**
//     * 上传一般文件
//     *
//     */
//    PutObjectResult uploadFile(InputStream inputStream, String fileExtName,String bucket);
//    /**
//     * 追加上传
//     */
//    AppendObjectResult appendFile(String groupName, InputStream inputStream, long fileSize, String fileExtName);
//
//    /**
//     * 分片上传
//     */
//    CompleteMultipartUploadResult multipartUpload(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet);
//
//    /**
//     * 断点上传
//     */
//    void breakPointUpload(String uploadFile, long fileSize, String fileExtName, Set<MateData> metaDataSet);
//
//
//    /**
//     * 中断上传
//     */
//    void abortMultipartUpload(String key, String uploadId);
//
//    /**
//     * 流式下载文件
//     */
//    byte[] downLoadFile(String key);
//
//    String getFileUrl(String bucket,String key);
//    /**
//     * 上传图片并且生成缩略图
//     * <p>
//     * <pre>
//     * 支持的图片格式包括"JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"
//     * </pre>
//     *
//     * @param inputStream
//     * @param fileSize
//     * @param fileExtName
//     * @param metaDataSet
//     * @return
//     */
//    void uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName,
//                                     Set<MateData> metaDataSet);
//
//    /**
//     * 删除文件
//     *
//     * @param filePath 文件路径(groupName/path)
//     */
//    void deleteFile(String filePath);
//
//
//    Map<String,Object> getSignedUrl();
//
//    Map<String,Object> getSignedUrlForApp();
//}
