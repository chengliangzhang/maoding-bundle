//package com.maoding.aliyunoss.service;
//
//
//import com.alibaba.druid.support.json.JSONUtils;
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.common.utils.BinaryUtil;
//import com.aliyun.oss.model.*;
//import com.aliyun.oss.model.Callback.CalbackBodyType;
//import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
//import com.maoding.aliyunoss.AliyunossClientConstants;
//import com.maoding.aliyunoss.bean.PartUploader;
//import com.maoding.aliyunoss.domain.AssumeRoleConfig;
//import com.maoding.aliyunoss.domain.OssConnectionManager;
//import com.maoding.fastdfsClient.domain.FileInfo;
//import com.maoding.fastdfsClient.domain.MateData;
//import com.maoding.fastdfsClient.domain.StorePath;
//import com.maoding.fastdfsClient.proto.storage.DownloadCallback;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
///**
// * 面向应用的接口实现
// *
// * @author tobato
// */
//@Component
//public class DefaultOssFileStorageClient implements OssFileStorageClient {
//
//    private final static Logger logger = LoggerFactory.getLogger(DefaultOssFileStorageClient.class);
//
//    @Resource
//    private OssConnectionManager ossConnectionManager;
//
//
//    /**
//     * 支持的图片类型
//     */
//    private static final String[] SUPPORT_IMAGE_TYPE = {"JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"};
//    private static final List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);
//
//    @Override
//    public PutObjectResult uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
//        OSSClient ossClient = ossConnectionManager.getInstance();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        PutObjectResult result = null;
//        try {
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (!ossClient.doesBucketExist(bucketName)) {
//                ossClient.createBucket(bucketName);
//            }
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileExtName,inputStream);
//          //  putObjectRequest.setCallback(this.getCallBack(null,null,null));
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//            result = ossClient.putObject(putObjectRequest);
//        } catch (OSSException oe) {
//            oe.printStackTrace();
//        } catch (ClientException ce) {
//            ce.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            shutdownClient(ossClient);
//        }
//        return result;
//    }
//
//    @Override
//    public PutObjectResult uploadFile(InputStream inputStream, String fileExtName,String bucket) {
//        OSSClient ossClient = ossConnectionManager.getInstance();
//        String bucketName = bucket;
//        PutObjectResult result = null;
//        try {
//
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileExtName,inputStream);
//            //  putObjectRequest.setCallback(this.getCallBack(null,null,null));
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//            result = ossClient.putObject(putObjectRequest);
//        } catch (OSSException oe) {
//            oe.printStackTrace();
//        } catch (ClientException ce) {
//            ce.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//           // shutdownClient(ossClient);
//        }
//
//
//        return result;
//    }
//
//    private void shutdownClient(OSSClient ossClient){
//        ossClient.shutdown();
//    }
//
//    @Override
//    public void uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
//    }
//
//    @Override
//    public void deleteFile(String filePath) {
//
//    }
//
//    @Override
//    public Map<String, Object> getSignedUrl() {
//        String dir = "user-dir";
//        // String host = "http://" + bucket + "." + endpoint;
//        OSSClient client = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        // String bucketName = "imaoding";
//        try {
//            long expireTime = 30;
//            long expireEndTime = System.currentTimeMillis() + expireTime * 100000;
//            Date expiration = new Date(expireEndTime);
//            PolicyConditions policyConds = new PolicyConditions();
//            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
//            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
//            // policyConds.addConditionItem();
//
//            String postPolicy = client.generatePostPolicy(expiration, policyConds);
//            byte[] binaryData = postPolicy.getBytes("utf-8");
//            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
//            String postSignature = client.calculatePostSignature(postPolicy);
//            Map<String, Object> callback = new HashMap<>();
//            callback.put("callbackUrl","http://39.108.169.202:8077/fileCenter/callback/callbackParam");
//            callback.put("callbackHost","oss-cn-shenzhen.aliyuncs.com");
//
//            Map<String, String> callbackVar = new HashMap<>();
//            callbackVar.put("cellphone","18665965065");
//            callbackVar.put("password","123456");
//            callback.put("callbackVar",callbackVar);
////            callback.put("callbackBody", "{\"bucket\":${bucket},\"object\":${object},"
////                    + "\"mimeType\":${mimeType},\"size\":${size},"
////                    + "\"cellphone\":${x:cellphone},\"password\":\"3333\"}");
//
//            callback.put("callbackBody",this.getCallbackBody(callbackVar));
//            callback.put("callbackBodyType","application/json");
//            String callbackString = JSONUtils.toJSONString(callback);
//            byte[] binaryData2 =  callbackString.getBytes();
//
//            Map<String, Object> respMap = new LinkedHashMap<>();
//            respMap.put("accessid", client.getCredentialsProvider().getCredentials().getAccessKeyId());
//            respMap.put("policy", encodedPolicy);
//            respMap.put("signature", postSignature);
//            respMap.put("dir", dir);
//            respMap.put("host",  "http://" + bucketName + "." +client.getEndpoint().getHost());
//            respMap.put("expire", String.valueOf(expireEndTime / 1000));
//            respMap.put("callback", BinaryUtil.toBase64String(binaryData2));
//            return respMap;
//
//        } catch (Exception e) {
//
//            logger.error(e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     *@Author：MaoSF
//     *Description：获取callbackBody字符串
//     *@Date:2017/11/9
//     *
//     */
//    private String getCallbackBody( Map<String, String> callbackVar ){
//        //循环输出key
//        StringBuilder callBodyBuf= new StringBuilder();
//        for (Map.Entry<String, String> entry : callbackVar.entrySet()) {
//            callBodyBuf.append(",");
//            callBodyBuf.append("\"");
//            callBodyBuf.append(entry.getKey());
//            callBodyBuf.append("\"");
//            callBodyBuf.append(":");
//            callBodyBuf.append("\"");
//            callBodyBuf.append(entry.getValue());
//            callBodyBuf.append("\"");
//        }
//        String callBodyStr = callBodyBuf.toString();
//        //oss自带参数
//        String bodyStr = "{\"bucket\":${bucket},\"object\":${object},"
//                + "\"mimeType\":${mimeType},\"size\":${size}";
//        bodyStr = bodyStr + callBodyStr;
//        bodyStr = bodyStr + "}";
//        return bodyStr;
//    }
//
//    @Override
//    public Map<String, Object> getSignedUrlForApp() {
//        Map<String,Object> map = new HashMap<>();
//        try {
//            final AssumeRoleResponse response = AssumeRoleConfig.assumeRole();
//            map.put("expiration",response.getCredentials().getExpiration());
//            map.put("accessKeyId",response.getCredentials().getAccessKeyId());
//            map.put("accessKeySecret",response.getCredentials().getAccessKeySecret());
//            map.put("securityToken",response.getCredentials().getSecurityToken());
//        } catch (com.aliyuncs.exceptions.ClientException e) {
//            logger.error("Failed to get a token.    Error code: " + e.getErrCode()+"    Error message: " + e.getErrMsg());
//        }
//        return map;
//    }
//
//
//    @Override
//    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
//        return null;
//    }
//
//    @Override
//    public AppendObjectResult appendFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
//        OSSClient client = ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        try {
//            Long firstPosition = 0L;
//            System.out.println("Begin to append object at position(" + firstPosition + ")");
//            AppendObjectResult appendObjectResult = client.appendObject(
//                    new AppendObjectRequest(bucketName, fileExtName, inputStream).withPosition(0L));
//
//            Long nextPosition = appendObjectResult.getNextPosition();
//            appendObjectResult = client.appendObject(
//                    new AppendObjectRequest(bucketName, fileExtName, new File("333"))
//                            .withPosition(nextPosition));
//            return appendObjectResult;
//        } catch (OSSException oe) {
//            logger.error("上次文件失败");
//            logger.error("上次文件失败----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//
//            logger.error("Error Message: " + ce.getMessage());
//        } finally {
//            shutdownClient(client);
//        }
//        return null;
//    }
//
//    @Override
//    public CompleteMultipartUploadResult multipartUpload( InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
//        OSSClient client = ossConnectionManager.getClient();
//        List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        try {
//            String uploadId = claimUploadId(client,bucketName,fileExtName);
//            final long partSize = 5 * 1024 * 1024L;   // 5MB
//            int partCount = (int) (fileSize / partSize);
//            if (fileSize % partSize != 0) {
//                partCount++;
//            }
//            if (partCount > 10000) {
//                logger.error("文件大小不能超过5G");
//                throw new RuntimeException("Total parts count should not exceed 10000");
//            }
//            for (int i = 0; i < partCount; i++) {
//                long startPos = i * partSize;
//                long curPartSize = (i + 1 == partCount) ? (fileSize - startPos) : partSize;
//                executorService.execute(new PartUploader(inputStream, startPos, curPartSize, i + 1, uploadId,bucketName,fileExtName,partETags,client));
//            }
//            //文件上传完之后，关闭
//            executorService.shutdown();
//            while (!executorService.isTerminated()) {
//                try {
//                    executorService.awaitTermination(5, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (partETags.size() != partCount) {
//                throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
//            } else {
//                System.out.println("Succeed to complete multiparts into an object named " + fileExtName + "\n");
//            }
//
//           //完成上传
//           return completeMultipartUpload(client,bucketName,fileExtName,uploadId,partETags);
//        } catch (OSSException oe) {
//            logger.error("上次文件失败");
//            logger.error("上次文件失败----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.error("Error Message: " + ce.getMessage());
//        } finally {
//            if (client != null) {
//                shutdownClient(client);
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void breakPointUpload(String uploadFile, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        try {
//            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, fileExtName);
//            // 待上传的本地文件
//            uploadFileRequest.setUploadFile(uploadFile);//此处需要修改
//            // 设置并发下载数，默认1
//            uploadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            uploadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            uploadFileRequest.setEnableCheckpoint(true);
//
//           // uploadFileRequest.setCallback(this.getCallBack(null,null,null));
//            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
//
//            CompleteMultipartUploadResult multipartUploadResult =
//                    uploadResult.getMultipartUploadResult();
//            System.out.println(multipartUploadResult.getETag());
//
//        } catch (OSSException oe) {
//            logger.error("上次文件失败oe----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.error("上次文件失败ce----"+"Error code: " + ce.getErrorCode());
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//    @Override
//    public void abortMultipartUpload(String key,String uploadId) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        // 上传字符串
//        // 取消分片上传，其中uploadId来自于initiateMultipartUpload
//        AbortMultipartUploadRequest abortMultipartUploadRequest =
//                new AbortMultipartUploadRequest(bucketName, key, uploadId);
//        ossClient.abortMultipartUpload(abortMultipartUploadRequest);
//        // 关闭client
//        shutdownClient(ossClient);
//    }
//
//    @Override
//    public byte[] downLoadFile(String key) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        OSSObject ossObject = ossClient.getObject(bucketName, key);
//        InputStream in = ossObject.getObjectContent();
//        try{
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            byte[] buffer = new byte[4096];
//            int n = 0;
//            while (-1 != (n = in.read(buffer))) {
//                output.write(buffer, 0, n);
//            }
//            in.close();
//            return output.toByteArray();
//            // 关闭client
//        }catch (OSSException oe) {
//            logger.error("上次文件失败oe----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.error("上次文件失败ce----"+"Error code: " + ce.getErrorCode());
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//
//            shutdownClient(ossClient);
//        }
//        return null;
//    }
//
//    @Override
//    public String getFileUrl(String bucket,String key) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        // 设置URL过期时间为10年  3600l* 1000*24*365*10
//        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 100);
//        // 生成URL
//        URL url = ossClient.generatePresignedUrl(bucket, key, expiration);
//        if (url != null) {
//            return url.toString();
//        }
//        return null;
//    }
//
//    private CompleteMultipartUploadResult completeMultipartUpload( OSSClient client,String bucketName,String key,String uploadId, List<PartETag> partETags) {
//        // Make part numbers in ascending order
//        Collections.sort(partETags, new Comparator<PartETag>() {
//            @Override
//            public int compare(PartETag p1, PartETag p2) {
//                return p1.getPartNumber() - p2.getPartNumber();
//            }
//        });
//
//        CompleteMultipartUploadRequest completeMultipartUploadRequest =
//                new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
//        // completeMultipartUploadRequest.setCallback(this.getCallBack(null,null,null));
//       return client.completeMultipartUpload(completeMultipartUploadRequest);
//    }
//
//    private String claimUploadId(OSSClient client,String bucketName,String key) {
//        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
//        InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
//        return result.getUploadId();
//    }
//
//    private Callback getCallBack(String callbackUrl,String host,Object body){
////        Callback callback = new Callback();
////        callback.setCallbackUrl(callbackUrl);
////        callback.setCallbackHost("oss-cn-hangzhou.aliyuncs.com");
////        callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
////        callback.setCalbackBodyType(CalbackBodyType.JSON);
////        callback.addCallbackVar("x:var1", "value1");
////        callback.addCallbackVar("x:var2", "value2");
//        // 上传回调参数
//        Callback callback = new Callback();
//        callback.setCallbackUrl(callbackUrl);
//        callback.setCallbackHost(host);
//
//        callback.setCallbackBody(JSONUtils.toJSONString(body));
//        callback.setCalbackBodyType(CalbackBodyType.JSON);
//        callback.addCallbackVar("x:var1", "value1");
//        callback.addCallbackVar("x:var2", "value2");
//        return callback;
//    }
//
//    @Override
//    public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream, long fileSize, String prefixName, String fileExtName) {
//        return null;
//    }
//
//    @Override
//    public ObjectMetadata getMetadata(String groupName, String key) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        try {
//            // 获取文件的元信息
//            ObjectMetadata metadata = ossClient.getObjectMetadata(bucketName, key);
//            return metadata;
//        } catch (OSSException oe) {
//            logger.error("上次文件失败oe----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.error("Error Message: " + ce.getMessage());
//        } finally {
//            /*
//             * Do not forget to shut down the client finally to release all allocated resources.
//             */
//            shutdownClient(ossClient);
//        }
//        return null;
//    }
//
//    @Override
//    public void overwriteMetadata(String groupName, String path, Set<MateData> metaDataSet) {
//
//    }
//
//    @Override
//    public void mergeMetadata(String groupName, String path, Set<MateData> metaDataSet) {
//
//    }
//
//    @Override
//    public FileInfo queryFileInfo(String groupName, String path) {
//        return null;
//    }
//
//    @Override
//    public void deleteFile(String groupName, String path) {
//
//    }
//
//    @Override
//    public <T> T downloadFile(String groupName, String key, DownloadCallback<T> callback) {
//        OSSClient ossClient = this.ossConnectionManager.getClient();
//        String bucketName = AliyunossClientConstants.DEFAULT_BUCKET_NAME;
//        try {
//            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, key);
//            // 设置本地文件
//            downloadFileRequest.setDownloadFile("D://"+key);
//            // 设置并发下载数，默认1
//            downloadFileRequest.setTaskNum(5);
//            // 设置分片大小，默认100KB
//            downloadFileRequest.setPartSize(1024 * 1024 * 1);
//            // 开启断点续传，默认关闭
//            downloadFileRequest.setEnableCheckpoint(true);
//            DownloadFileResult downloadResult = ossClient.downloadFile(downloadFileRequest);
//
//        } catch (OSSException oe) {
//            logger.error("上次文件失败oe----"+"Error code: " + oe.getErrorCode()
//                    +"  Request ID:      " + oe.getRequestId()
//                    +"  Host ID:         " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.error("Error Message: " + ce.getMessage());
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//            shutdownClient(ossClient);
//        }
//        return null;
//    }
//
//    @Override
//    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback) {
//        return null;
//    }
//}
