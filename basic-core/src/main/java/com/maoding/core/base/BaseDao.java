package com.maoding.core.base;

import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Wuwq on 2016/12/14.
 * 数据库访问层接口（自带通用方法）
 */
public interface BaseDao<T extends BaseEntity> extends BaseViewDao<T>,Mapper<T>,MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    /**
     * 描述       更新记录
     * 日期       2018/8/16
     * @author   张成亮
     **/
    default void updateById(@NotNull CoreEditDTO request){
        Class<T> clazz = getT();
        T entity = BeanUtils.createFrom(request,clazz);
        updateByPrimaryKeySelective(entity);
    }

    default T update(@NotNull CoreEditDTO request){
        final String unselected = "0";
        T entity = null;
        if (StringUtils.isNotSame(request.getIsSelected(), unselected)) {
            //如果request内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
            if (StringUtils.isNotEmpty(request.getId())){
                entity = selectById(request.getId());
                if (entity != null) {
                    //修改
                    BeanUtils.copyProperties(request, entity);
                    entity.setUpdateBy(request.getAccountId());
                    entity.resetUpdateDate();
                    updateByPrimaryKeySelective(entity);
                }
            }

            //如果request的id为空，或者数据库内没有此记录，则新增记录
            if (entity == null) {
                //如果id为空，则新增一条记录
                Class<T> clazz = getT();
                entity = BeanUtils.createFrom(request, clazz);
                entity.initEntity();
                entity.setCreateBy(request.getAccountId());
                insertSelective(entity);
            }
        }
        return entity;
    }

    default T updateById(@NotNull T request){
        T entity = null;
        //如果entity内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
        if (StringUtils.isNotEmpty(request.getId())){
            entity = selectById(request.getId());
            if (entity != null) {
                //修改
                BeanUtils.copyProperties(request, entity);
                entity.resetUpdateDate();
                updateByPrimaryKeySelective(entity);
            }
        }

        //如果entity的id为空，或者数据库内没有此记录，则新增记录
        if (entity == null) {
            entity = request;
            entity.initEntity();
            insertSelective(entity);
        }
        return entity;
    }

    default int BatchInsert(List<T> recordList){
        return insertList(recordList);
    }
}
