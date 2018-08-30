package com.maoding.core.base;

import com.maoding.utils.TraceUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/16
 * 类名: com.maoding.core.base.BaseHttpCallback
 * 作者: 张成亮
 * 描述:
 **/
public class BaseHttpCallback implements Callback {
    @Override
    public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
        TraceUtils.enter(call,e);
    }

    @Override
    public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
        TraceUtils.enter(call,response);
    }

}
