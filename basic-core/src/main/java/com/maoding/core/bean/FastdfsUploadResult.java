package com.maoding.core.bean;

/**
 * Created by Wuwq on 2016/12/18.
 */
public class FastdfsUploadResult {
    private Integer chunks = 0;
    private Integer chunk = 0;
    /**
     * 当前分片大小
     **/
    private Long chunkSize = 0L;
    private String uploadId;
    private String fileName;
    private Long fileSize = 0L;
    private String fileExtName;
    private String fastdfsGroup;
    private String fastdfsPath;
    private String prefixName;
    /**
     * 是否需要后续上传（针对分片）
     **/
    private Boolean needFlow;

    /**
     * 表ID，视业务情况赋值（可选字段）
     */
    private String netFileId;

    /**
     * 序号，视业务情况赋值（可选字段）
     */
    private Integer netFileSeq;

    private String fileFullPath;
    /** 是否可以覆盖 */
    private String isWritable;

    public String getIsWritable() {
        return isWritable;
    }

    public void setIsWritable(String isWritable) {
        this.isWritable = isWritable;
    }

    public static FastdfsUploadResult parse(MultipartFileParam param, String group, String path) {
        FastdfsUploadResult r = new FastdfsUploadResult();
        r.setUploadId(param.getUploadId());
        r.setFileName(param.getFileName());
        r.setFileExtName(param.getFileExtName());
        r.setChunks(param.getChunks());
        r.setChunk(param.getChunk());
        r.setFastdfsGroup(group);
        r.setFastdfsPath(path);

        if (param.isChunked())
            r.setChunkSize(param.getSize());
        else
            r.setFileSize(param.getSize());

        return r;
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

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
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

    public String getPrefixName() {
        return prefixName;
    }

    public void setPrefixName(String prefixName) {
        this.prefixName = prefixName;
    }

    public Boolean getNeedFlow() {
        return needFlow;
    }

    public void setNeedFlow(Boolean needFlow) {
        this.needFlow = needFlow;
    }

    public String getNetFileId() {
        return netFileId;
    }

    public void setNetFileId(String netFileId) {
        this.netFileId = netFileId;
    }

    public Integer getNetFileSeq() {
        return netFileSeq;
    }

    public void setNetFileSeq(Integer netFileSeq) {
        this.netFileSeq = netFileSeq;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }
}
