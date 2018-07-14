package com.maoding.fastdfsClient.proto.storage;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 下载为byte流
 *
 * @author tobato
 */
public class DownloadByteArray implements DownloadCallback<byte[]> {

    @Override
    public byte[] recv(InputStream ins) throws IOException {
        return IOUtils.toByteArray(ins);
    }
}
