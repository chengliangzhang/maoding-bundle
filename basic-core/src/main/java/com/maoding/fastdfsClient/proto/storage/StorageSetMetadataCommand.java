package com.maoding.fastdfsClient.proto.storage;

import com.maoding.fastdfsClient.domain.MateData;
import com.maoding.fastdfsClient.proto.AbstractFdfsCommand;
import com.maoding.fastdfsClient.proto.FdfsResponse;
import com.maoding.fastdfsClient.proto.storage.enums.StorageMetdataSetType;
import com.maoding.fastdfsClient.proto.storage.internal.StorageSetMetadataRequest;

import java.util.Set;

/**
 * 设置文件标签
 *
 * @author tobato
 */
public class StorageSetMetadataCommand extends AbstractFdfsCommand<Void> {

    /**
     * 设置文件标签(元数据)
     *
     * @param groupName
     * @param path
     * @param metaDataSet
     * @param type
     */
    public StorageSetMetadataCommand(String groupName, String path, Set<MateData> metaDataSet,
                                     StorageMetdataSetType type) {
        this.request = new StorageSetMetadataRequest(groupName, path, metaDataSet, type);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
