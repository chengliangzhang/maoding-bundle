//package com.maoding.filecenter.module.upload;
//
//import com.aliyun.oss.model.PutObjectResult;
//import com.maoding.aliyunoss.service.OssFileStorageClient;
//import com.maoding.core.base.BaseController;
//import com.maoding.core.bean.ApiResult;
//import com.maoding.fastdfsClient.proto.storage.DownloadByteArray;
//import com.maoding.fastdfsClient.service.FastFileStorageClient;
//import com.maoding.fastdfsClient.service.GenerateStorageClient;
//import com.maoding.filecenter.module.file.dao.NetFileDAO;
//import com.maoding.filecenter.module.file.dao.UserFileDAO;
//import com.maoding.filecenter.module.file.model.NetFileDO;
//import com.maoding.filecenter.module.file.model.UserFileDO;
//import com.maoding.utils.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import tk.mybatis.mapper.entity.Example;
//
//import javax.annotation.Resource;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by sandy on 2017/10/24.
// */
//@RestController
//@RequestMapping("/ossFileCenter")
//public class OSSUploadController extends BaseController {
//
//    @Autowired
//    private OssFileStorageClient ossFileStorageClient;
//
//    @Autowired
//    private NetFileDAO netFileDAO;
//
//    @Autowired
//    private UserFileDAO userFileDAO;
//
//    @Resource
//    private FastFileStorageClient fastFileStorageClient;
//
//    private String fileServer = "oss-cn-shenzhen.aliyuncs.com";
//
//    @RequestMapping("/callback")
//    @ResponseBody
//    public ApiResult ossFileUploadCallback(@RequestBody Map<String,String> callBody){
//        System.out.print("测试");
//        return null;
//    }
//
//
//    @RequestMapping("/getOssSignature")
//    @ResponseBody
//    public ApiResult getOssSignature(@RequestBody Map<String,String> callBody){
//        System.out.print("测试");
//        return   ApiResult.success(ossFileStorageClient.getSignedUrl());
//    }
//
//    @RequestMapping("/getAppOssSignature")
//    @ResponseBody
//    public ApiResult getAppOssSignature(){
//        System.out.print("测试");
//        return   ApiResult.success(ossFileStorageClient.getSignedUrlForApp());
//    }
//
//
//    @RequestMapping("/downloadFile")
//    @ResponseBody
//    public ApiResult downloadFile(@RequestBody Map<String,String> fileDTO){
//        return   ApiResult.success(ossFileStorageClient.downLoadFile(""));
//    }
//
//
//
//    //文件迁移到oss
//    @RequestMapping("/fileTransfer")
//    @ResponseBody
//    public ApiResult fileTransfer() {
//
//        Example example = new Example(NetFileDO.class);
//        example.createCriteria().andIsNotNull("filePath")
//        .andIsNull("bucket");
//        List<NetFileDO> list = netFileDAO.selectByExample(example);
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        System.out.println("活跃线程数："+Runtime.getRuntime().availableProcessors());
//        for(NetFileDO fileDO:list){
//            try{
//                if(!StringUtils.isNullOrEmpty(fileDO.getFilePath())){
//                    byte[] bytes = null;
//                    try{
//                        bytes = fastFileStorageClient.downloadFile(fileDO.getFileGroup(), fileDO.getFilePath(), new DownloadByteArray());
//                        System.out.println("下载文件："+bytes.length);
//                    }catch (Exception e){
//                        System.out.println("id=========="+fileDO.getId());
//                        //
//                        fileDO.setFilePath(null);
//                        netFileDAO.updateByPrimaryKey(fileDO);
//                        continue;
//                    }
//                    runThread(fixedThreadPool,netFileDAO,fileDO,bytes);
//                }
//            }catch (Exception e){
//                System.out.println("id======异常===="+fileDO.getId());
//                e.printStackTrace();
//              //  return ApiResult.failed("失败");
//            }
//        }
//        return ApiResult.success("成功导入");
//    }
//
//
//    //文件迁移到oss
//    @RequestMapping("/userFileTransfer")
//    @ResponseBody
//    public ApiResult userFileTransfer() {
//        Example example = new Example(NetFileDO.class);
//        example.createCriteria().andIsNull("bucket");
//        List<UserFileDO> list = userFileDAO.selectByExample(example);
//
//        ExecutorService  poolExecutor=    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//       // System.out.println("活跃线程数："+Runtime.getRuntime().availableProcessors());
//        for(UserFileDO fileDO:list){
//            try{
//                if(!StringUtils.isNullOrEmpty(fileDO.getAttachPath())){
//                    byte[] bytes = null;
//                    try{
//                        bytes = fastFileStorageClient.downloadFile(fileDO.getFileGroup(), fileDO.getAttachPath(), new DownloadByteArray());
//                        System.out.println("下载文件："+bytes.length);
//                    }catch (Exception e){
//                        System.out.println("id=========="+fileDO.getId());
//                        //
//                        fileDO.setAttachPath(null);
//                        userFileDAO.updateByPrimaryKey(fileDO);
//                        continue;
//                    }
//                    runThreadForUserAttach(poolExecutor,userFileDAO,fileDO,bytes);
//                }
//            }catch (Exception e){
//                System.out.println("id======异常===="+fileDO.getId());
//                e.printStackTrace();
//                //  return ApiResult.failed("失败");
//            }
//        }
//        return ApiResult.success("成功导入");
//    }
//
//    public void runThread(ExecutorService fixedThreadPool, final NetFileDAO netFileDAO,final NetFileDO fileDO,final byte[] bytes){
//        fixedThreadPool.execute( ()-> {
//            System.out.println("执行======"+fileDO.getId());
//                //迁移到阿里云上
//                String bucket = "publicmaoding";
//                if(!StringUtils.isNullOrEmpty(fileDO.getProjectId())){
//                    bucket = "testmaoding";
//                }
//                String key = fileDO.getFilePath();
//                PutObjectResult result = ossFileStorageClient.uploadFile(new ByteArrayInputStream(bytes),key,bucket);
//                if(result!=null){
//                    fileDO.setObjectKey(fileDO.getId()+"."+fileDO.getFileExtName());
//                    fileDO.setBucket(bucket);
//                    String url = "http://"+bucket+"."+this.fileServer+key; //= ossFileStorageClient.getFileUrl(bucket,key);
//                    fileDO.setOssFilePath(url);
//                    netFileDAO.updateByPrimaryKey(fileDO);
//                }else {
//                    System.out.println("id====222======"+fileDO.getId());
//                }
//            }
//        );
//    }
//
//
//    public void runThreadForUserAttach(ExecutorService fixedThreadPool, final UserFileDAO netFileDAO,final UserFileDO fileDO,final byte[] bytes){
//        fixedThreadPool.execute( ()-> {
//                    System.out.println("执行======"+fileDO.getId());
//                    //迁移到阿里云上
//                    String bucket = "publicmaoding";
//                    String key = fileDO.getAttachPath();
//                    PutObjectResult result = ossFileStorageClient.uploadFile(new ByteArrayInputStream(bytes),key,bucket);
//                    if(result!=null){
//                        fileDO.setObjectKey(key);
//                        fileDO.setBucket(bucket);
//                        //String url = ossFileStorageClient.getFileUrl(bucket,key);
//                        String url = "http://"+bucket+"."+this.fileServer+"/"+key;
//                        fileDO.setOssFilePath(url);
//                        userFileDAO.updateByPrimaryKey(fileDO);
//                    }else {
//                        System.out.println("id====222======"+fileDO.getId());
//                    }
//                }
//        );
//    }
//}
