package com.maoding.coreBuffer;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 11:06
 * 描    述 : 缓存接口
 */
public interface CoreBuffer {
    /** 添加缓存
     * @param key 缓存名称
     * @param list 要添加的缓存内容
     * @param aliveTime 延时时间
     * */
    default <T> void setList(@NotNull String key, @NotNull List<? extends T> list, long aliveTime){}
    /** 获取缓存
     * @param key 缓存名称
     * @param aliveTime 延时时间
     * @return 获取到的内容
     * */
    default <T> List<T> getList(@NotNull String key, long aliveTime){return null;}
    /** 刷新缓存内某个元素
     * @param element 要刷新的元素
     * @param aliveTime 延时时间
     * @return 是否在缓存内找到此元素
     * */
    default <T> boolean replaceInList(@NotNull T element, long aliveTime){return false;}
    /** 清除包含某个元素的缓存
     * @param element 要查找的元素
     * */
    default <T> void removeListInclude(@NotNull T element){}
}
