package com.maoding.core.bean;

import com.maoding.config.MultipartConfig;
import com.maoding.utils.NumberUtils;
import com.maoding.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuwq on 2016/12/18.
 */
public class MultipartFileParam {
    //该请求是否是multipart
    /*private boolean isMultipart;*/
    /**
     * 任务ID
     */
    private String uploadId;

    /**
     * 总分片数量
     */
    private Integer chunks;

    /**
     * 当前为第几块分片
     */
    private Integer chunk;

    /**
     * 每个分片的约定大小
     */
    private Long chunkPerSize = 0L;

    /**
     * 当前分片大小
     */
    private Long size = 0L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String fileExtName;

    /**
     * 分片对象
     */
    private FileItem fileItem;

    /**
     * 仅用于FASTDFS Append
     */
    private String fastdfsGroup;

    /**
     * 仅用于FASTDFS Append
     */
    private String fastdfsPath;

    /**
     * 请求中附带的自定义参数
     */
    private HashMap<String, Object> param = new HashMap<>();

    /**
     * 在HttpServletRequest中获取分段上传文件请求的信息
     */
    public static MultipartFileParam parse(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart)
            throw new UnsupportedOperationException("unable to parse request content which is not multipart Content.");

        MultipartFileParam param = new MultipartFileParam();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MultipartConfig.MaxInMemorySize);
        ServletFileUpload upload = new ServletFileUpload(factory);

        // 得到所有的表单域，它们目前都被当作FileItem
        List<FileItem> fileItems = upload.parseRequest(request);
        if (fileItems.size() == 0) {
            //获取不到FileItems的另外处理方式
            Map<String, List<FileItem>> maps = upload.parseParameterMap(request);
            if (maps.size() > 0) {
                for (Map.Entry<String, List<FileItem>> entry : maps.entrySet()) {
                    fileItems.addAll(entry.getValue());
                }
            }
        }

        for (FileItem fileItem : fileItems) {
            String fieldName = fileItem.getFieldName();

            switch (fieldName) {
                case "uploadId":
                    param.setUploadId(fileItem.getString());
                    break;
                case "name":
                    String name = new String(fileItem.getString().getBytes("ISO-8859-1"), "UTF-8");
                    param.setFileName(name);
                    int extIndex = name.lastIndexOf(".");
                    if (extIndex != -1) {
                        String extName = name.substring(extIndex + 1);
                        param.setFileExtName(extName);
                    }
                    break;
                case "chunks":
                    param.setChunks(NumberUtils.toInt(fileItem.getString()));
                    break;
                case "chunk":
                    param.setChunk(NumberUtils.toInt(fileItem.getString()));
                    break;
                case "chunkPerSize":
                    param.setChunkPerSize(NumberUtils.toLong(fileItem.getString()));
                    break;
                case "file":
                    param.setFileItem(fileItem);
                    long size = fileItem.getSize();
                    if (size > MultipartConfig.MaxUploadSize)
                        throw new MaxUploadSizeExceededException(MultipartConfig.MaxUploadSize);
                    param.setSize(size);
                    break;
                case "fastdfsGroup":
                    param.setFastdfsGroup(fileItem.getString());
                    break;
                case "fastdfsPath":
                    param.setFastdfsPath(fileItem.getString());
                    break;
                default:
                    param.getParam().put(fieldName, fileItem.getString());
                    break;
            }
        }

        //如果没有获取到filExtName,fileName的时候，做补偿处理
        if(StringUtils.isNullOrEmpty(param.getFileName()) || StringUtils.isNullOrEmpty(param.getFileExtName())){
            if(null !=param.getFileItem()
                    && !StringUtils.isNullOrEmpty(param.getFileItem().getName())
                    && param.getFileItem().getName().lastIndexOf(".")>-1){
                param.setFileName(param.getFileItem().getName());
                int extIndex = param.getFileItem().getName().lastIndexOf(".");
                if (extIndex != -1) {
                    String extName = param.getFileItem().getName().substring(extIndex + 1);
                    param.setFileExtName(extName);
                }
            }else {
                param.setFileName("未知.jpg");
                param.setFileExtName("jgp");
            }
        }

        return param;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public Long getChunkPerSize() {
        return chunkPerSize;
    }

    public void setChunkPerSize(Long chunkSize) {
        this.chunkPerSize = chunkSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public String getFastdfsGroup() {
        return fastdfsGroup;
    }

    public void setFastdfsGroup(String fastdfsGroup) {
        this.fastdfsGroup = fastdfsGroup;
    }

    public String getFastdfsPath() {
        return fastdfsPath;
    }

    public void setFastdfsPath(String fastdfsPath) {
        this.fastdfsPath = fastdfsPath;
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public void setParam(HashMap<String, Object> param) {
        this.param = param;
    }

    /**
     * 是否分片上传
     */
    public Boolean isChunked() {
        return chunks != null && chunks > 0;
    }
}
