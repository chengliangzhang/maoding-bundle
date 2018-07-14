package com.maoding.fastdfsClient.proto.tracker.internal;

import com.maoding.fastdfsClient.proto.CmdConstants;
import com.maoding.fastdfsClient.proto.FdfsRequest;
import com.maoding.fastdfsClient.proto.ProtoHead;

/**
 * 列出分组命令
 *
 * @author tobato
 */
public class TrackerListGroupsRequest extends FdfsRequest {

    public TrackerListGroupsRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
