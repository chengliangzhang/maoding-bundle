package com.maoding.aliyunoss.bean;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PartUploader implements Runnable {
        
        private File localFile;
        private InputStream inputStream;
        private long startPos;        
        
        private long partSize;
        private int partNumber;
        private String uploadId;
        private String bucketName;
        private String key;

        private List<PartETag> partETags;

        private OSSClient client;

        public PartUploader(File localFile, long startPos, long partSize, int partNumber,
                            String uploadId,String bucketName,String key,List<PartETag> partETags,OSSClient client) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
            this.key = key;
            this.bucketName = bucketName;
            this.client = client;
            this.partETags = partETags;
        }

    public PartUploader(InputStream inputStream, long startPos, long partSize, int partNumber,
                        String uploadId,String bucketName,String key,List<PartETag> partETags,OSSClient client) {
        this.inputStream = inputStream;
        this.startPos = startPos;
        this.partSize = partSize;
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        this.key = key;
        this.bucketName = bucketName;
        this.client = client;
        this.partETags = partETags;
    }

    @Override
    public void run() {
        InputStream instream = null;
        try {
            if(this.localFile!=null){
                instream = new FileInputStream(this.localFile);
            }
            if(this.inputStream!=null){
                instream = inputStream;
            }
            instream.skip(this.startPos);

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(this.uploadId);
            uploadPartRequest.setInputStream(instream);
            uploadPartRequest.setPartSize(this.partSize);
            uploadPartRequest.setPartNumber(this.partNumber);

            UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
            synchronized (partETags) {
                partETags.add(uploadPartResult.getPartETag());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}