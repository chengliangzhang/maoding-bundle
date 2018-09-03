package com.maoding.core.base;

import com.maoding.utils.TraceUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.slf4j.Logger;

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
    /** 日志对象 */
    private final Logger log = TraceUtils.getLogger(this.getClass());

    @Override
    public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
        TraceUtils.enter(log,call,e);
    }

    @Override
    public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
        TraceUtils.enter(log,call,response);
    }

}
