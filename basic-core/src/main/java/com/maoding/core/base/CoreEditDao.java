package com.maoding.core.base;

import com.maoding.mybatis.provider.CoreMapper;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/10
 * 类名: com.maoding.core.base.CoreEditDao
 * 作者: 张成亮
 * 描述: 
 **/
public interface CoreEditDao<T extends CoreEntity> extends BaseViewDao<T>,Mapper<T>,MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    String UNSELECTED = "0";
    short DELETED = (short)1;

    /**
     * 描述     插入一条记录
     * 日期     2018/9/7
     * @author  张成亮
     **/
    default int insert(@NotNull T entity){
        if (StringUtils.isEmpty(entity.getId())){
            entity.reset();
        } else {
            entity.resetCreateTime();
            entity.update();
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
        entity.setLastModifyUserId(request.getAccountId());
        return updateById(entity);
    }

    /**
     * 描述       通过更新申请进行记录更新，需要判断记录是否被选中
     * 日期       2018/8/16
     * @author   张成亮
     * @param    request 更新申请
     *                   accountId 申请者用户编号
     *                   isSelected 是否被选中进行更新
     * @return   如果选中进行更新，返回更新后的记录，否则返回空
     **/
    default T update(@NotNull CoreEditDTO request){
        T entity = null;
        if (StringUtils.isNotSame(request.getIsSelected(), UNSELECTED)) {
            entity = updateById(request);
        }
        return entity;
    }

    /**
     * 描述       通过数据库实体类进行记录更新，如果数据库内无此记录，插入一条记录
     *              如果实体包含path字段，带子节点更新
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
                updatedEntity.resetLastModifyTime();
                //如果包含路径字段，做特殊处理
                if (BeanUtils.hasField(entity.getClass(),CoreMapper.PROPERTY_NAME_PATH,String.class)){
                    //如果路径字段为空,且修改了名称,同步更改路径字段
                    if (BeanUtils.hasField(entity.getClass(),CoreMapper.PROPERTY_NAME_NAME,String.class)){
                        String path = BeanUtils.getProperty(entity,CoreMapper.PROPERTY_NAME_PATH,String.class);
                        if (StringUtils.isEmpty(path)){
                            String name = BeanUtils.getProperty(entity,CoreMapper.PROPERTY_NAME_NAME,String.class);
                            if (StringUtils.isNotEmpty(name)){
                                String updatedPath = BeanUtils.getProperty(updatedEntity,CoreMapper.PROPERTY_NAME_PATH,String.class);
                                if (StringUtils.contains(updatedPath,"/")){
                                    updatedPath = StringUtils.lastLeft(updatedPath,"/") + "/" + name;
                                } else {
                                    updatedPath = name;
                                }
                                BeanUtils.setProperty(updatedEntity,CoreMapper.PROPERTY_NAME_PATH,updatedPath);
                            }
                        }
                    }
                    coreUpdateWithPath(updatedEntity);
                } else {
                    updateByPrimaryKeySelective(updatedEntity);
                }
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
     * 描述       通用带路径更新接口，同时更新所有子节点
     * 日期       2018/9/7
     * @author   张成亮
     **/
    @UpdateProvider(type = CoreMapper.class, method = "dynamicSQL")
    void coreUpdateWithPath(@NotNull T entity);

    /**
     * 描述     通过更新申请更改一条记录的删除标志为真
     * 日期     2018/9/7
     * @author  张成亮
     **/
    default void deleteById(@NotNull CoreEditDTO request){
        if (StringUtils.isNotEmpty(request.getId())) {
            Class<T> clazz = getT();
            T entity = BeanUtils.createFrom(request, clazz);
            entity.setLastModifyUserId(request.getAccountId());
            entity.setDeleted(DELETED);
            updateById(entity);
        }
    }

    /**
     * 描述       通过更新申请更改一条记录的删除标志为真，需要判断记录是否被选中
     * 日期       2018/9/10
     * @author   张成亮
     **/
    default void delete(@NotNull CoreEditDTO request){
        if (StringUtils.isNotSame(request.getIsSelected(), UNSELECTED)) {
            deleteById(request);
        }
    }

    /**
     * 描述       通过数据库实体类更改一条记录的删除标志为真
     * 日期       2018/9/10
     * @author   张成亮
     **/
    default void deleteById(@NotNull T entity){
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity.setDeleted(DELETED);
            updateById(entity);
        }
    }

    /**
     * 描述     通过更新申请删除一条记录
     * 日期     2018/9/7
     * @author  张成亮
     **/
    default void destroyById(@NotNull CoreEditDTO request){
        if (StringUtils.isNotEmpty(request.getId())) {
            Class<T> entityClass = getT();
            if (BeanUtils.hasField(entityClass, CoreMapper.PROPERTY_NAME_PATH, String.class)) {
                T entity = selectById(request.getId());
                coreDeleteWithPath(entity);
            } else {
                T entity = BeanUtils.createFrom(request,entityClass);
                deleteByPrimaryKey(entity);
            }
        }
    }

    /**
     * 描述       通过更新申请删除一条记录，需要判断记录是否被选中
     * 日期       2018/9/10
     * @author   张成亮
     **/
    default void destroy(@NotNull CoreEditDTO request){
        if (StringUtils.isNotSame(request.getIsSelected(), UNSELECTED)) {
            destroyById(request);
        }
    }

    /**
     * 描述       根据记录信息删除一条记录
     * 日期       2018/9/10
     * @author   张成亮
     **/
    @DeleteProvider(type = CoreMapper.class, method = "dynamicSQL")
    void coreDeleteWithPath(@NotNull T entity);
}
