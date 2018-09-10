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
public interface BaseEditDao<T extends BaseEntity> extends BaseViewDao<T>,Mapper<T>,MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    /**
     * 描述       通过更新申请进行记录更新
     * 日期       2018/8/16
     * @author   张成亮
     * @param    request 更新申请
     *                   accountId 申请者用户编号
     * @return   返回更新后的记录
     **/
    default T updateById(@NotNull CoreEditDTO request){
        Class<T> clazz = getT();
        T entity = BeanUtils.createFrom(request,clazz);
        entity.setUpdateBy(request.getAccountId());
        return updateById(entity);
    }

    /**
     * 描述       通过更新申请进行记录更新，需要判断记录是否被选中更新
     * 日期       2018/8/16
     * @author   张成亮
     * @param    request 更新申请
     *                   accountId 申请者用户编号
     *                   isSelected 是否被选中进行更新
     * @return   如果选中进行更新，返回更新后的记录，否则返回空
     **/
    default T update(@NotNull CoreEditDTO request){
        final String unselected = "0";
        T entity = null;
        if (StringUtils.isNotSame(request.getIsSelected(), unselected)) {
            entity = updateById(request);
        }
        return entity;
    }

    /**
     * 描述       通过数据库实体类进行记录更新，如果数据库内无此记录，插入一条记录
     * 日期       2018/9/7
     * @author   张成亮
     **/
    default T updateById(@NotNull T entity){
        T updatedEntity = null;
        //如果entity内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
        if (StringUtils.isNotEmpty(entity.getId())){
            updatedEntity = selectById(entity.getId());
            if (updatedEntity != null) {
                //修改
                BeanUtils.copyProperties(entity, updatedEntity);
                updatedEntity.resetUpdateDate();
                updateByPrimaryKeySelective(updatedEntity);
            }
        }

        //如果entity的id为空，或者数据库内没有此记录，则新增记录
        if (updatedEntity == null) {
            updatedEntity = entity;
            insert(updatedEntity);
        }
        return updatedEntity;
    }

    /**
     * 描述     插入一条记录
     * 日期     2018/9/7
     * @author  张成亮
     **/
    default int insert(@NotNull T entity){
        if (StringUtils.isEmpty(entity.getId())){
            entity.initEntity();
        } else if (entity.getCreateDate() == null) {
            entity.resetCreateDate();
        }
        return insertSelective(entity);
    }

    /**
     * 描述       批量插入记录
     * 日期       2018/9/7
     * @author   张成亮
     **/
    default int insert(@NotNull List<T> entityList){
        entityList.forEach(this::insert);
        return entityList.size();
    }

    @Deprecated
    /** 使用insert代替 **/
    default int BatchInsert(@NotNull List<T> recordList){
        return insert(recordList);
    }

    /**
     * 描述     删除一条记录
     * 日期     2018/9/7
     * @author  张成亮
     **/
    default void deleteById(@NotNull CoreEditDTO request){
        if (StringUtils.isNotEmpty(request.getId())) {
            Class<T> clazz = getT();
            T entity = BeanUtils.createFrom(request, clazz);
            deleteById(entity);
        }
    }

    /**
     * 描述       删除一条记录，需要判断记录是否被选中
     * 日期       2018/9/10
     * @author   张成亮
     **/
    default void delete(@NotNull CoreEditDTO request){
        final String unselected = "0";
        if (StringUtils.isNotSame(request.getIsSelected(), unselected)) {
            deleteById(request);
        }
    }

    /**
     * 描述       根据记录信息删除一条记录
     * 日期       2018/9/10
     * @author   张成亮
     **/
    default void deleteById(@NotNull T entity){
        if (StringUtils.isNotEmpty(entity.getId())) {
            deleteByPrimaryKey(entity);
        }
    }
}
