package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("fileApi")
public class FileApiImpl implements FileApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private UploadAndDownloadFilesApi uploadAndDownloadFilesApi;

    @Override
    public ApiResult uploadFile(final Object file) {
        return apiResultHandler.handle(() -> uploadAndDownloadFilesApi.orgNameAppNameChatfilesPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), (File) file, true));
    }

    @Override
    public ApiResult downloadFile(final String fileUUID, final String shareSecret, final Boolean isThumbnail) {
        return apiResultHandler.handle(() -> uploadAndDownloadFilesApi.orgNameAppNameChatfilesUuidGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), fileUUID, shareSecret, isThumbnail));
    }
}
