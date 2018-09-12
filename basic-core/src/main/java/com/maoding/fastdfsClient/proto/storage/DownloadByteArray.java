package com.maoding.fastdfsClient.proto.storage;

import com.maoding.fastdfsClient.domain.FileInfo;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 下载为byte流
 *
 * @author tobato
 */
public class DownloadByteArray implements DownloadCallback<byte[]> {
    private OutputStream os = null;
    private FileInfo info = null;

    public FileInfo getInfo() {
        return info;
    }

    public void setInfo(FileInfo info) {
        this.info = info;
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    @Override
    public byte[] recv(InputStream ins) throws IOException {
        if (getOs() != null){
            final int maxBlockSize = 4096;

            long fileSize = (getInfo() != null) ? getInfo().getFileSize() : maxBlockSize;
            if (fileSize > maxBlockSize){
                for (long pos=0; pos<fileSize; pos+=maxBlockSize){
                    long size = (fileSize - pos);
                    if (size > maxBlockSize){
                        size = maxBlockSize;
                    }
                    IOUtils.copy(ins,getOs(),(int)size);
                }
            } else {
                IOUtils.copy(ins,getOs());
            }

            return null;
        } else {
            return IOUtils.toByteArray(ins);
        }
    }

    public DownloadByteArray(){}
    public DownloadByteArray(OutputStream os, FileInfo info){
        setOs(os);
        setInfo(info);
    }
}
