package com.maoding.coreBuffer;

import com.maoding.coreBuffer.redis.RedisBuffer;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/9
 * 类名: com.maoding.coreBuffer.BufferFactory
 * 作者: 张成亮
 * 描述: 缓存对象工厂
 **/
public class BufferFactory {
    public static CoreBuffer getBuffer(String bufferType){
        return new RedisBuffer();
    }
}
