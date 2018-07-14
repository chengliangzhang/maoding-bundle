package com.maoding.im.easemob.api;

import com.maoding.im.easemob.comm.TokenUtils;
import org.springframework.stereotype.Service;


@Service("authTokenApi")
public class AuthTokenApiImpl implements AuthTokenApi {

    @Override
    public String getAuthToken() {
        return TokenUtils.getAccessToken();
    }
}
