package com.maoding.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Date;
import java.util.Set;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 : 通用SQL语句实现
 */
public class CoreMapper extends MapperTemplate {
    public static final String PROPERTY_NAME_NAME = "name";
    public static final String PROPERTY_NAME_PATH = "path";

    public CoreMapper(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 描述       通用带路径更新接口
     * 日期       2018/9/7
     * @author   张成亮
     **/
    public String coreUpdateWithPath(MappedStatement ms){
        return getUpdateString(ms);
    }

    /**
     * 描述       通用带路径删除接口
     * 日期       2018/9/7
     * @author   张成亮
     **/
    public String coreDeleteWithPath(MappedStatement ms){
        return getDeleteString(ms);
    }

    /** 生成更新sql */
    private String getUpdateString(MappedStatement ms){
        //获得列名称转换对象
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        StringBuilder sqlTable = new StringBuilder(); //sql语句内update <table>部分
        StringBuilder sqlSet = new StringBuilder(); //sql语句内<set></set>部分
        StringBuilder sqlWhere = new StringBuilder(); //sql语句内<where></where>部分
        //update <table> t
        sqlTable.append(SqlHelper.updateTable(entityClass,tableName(entityClass))).append("t");
        //根据各字段进行更新，id字段和输入为null的字段不更新
        sqlSet.append("<set>");
        for (EntityColumn column : columns) {
            if (isId(column)) {
                //<where>
                //  t.id=#{id}
                //</where>
                sqlWhere.append("<where>");
                sqlWhere.append("t.`").append(column.getColumn()).append("`=#{")
                        .append(column.getProperty()).append("}");
                sqlWhere.append("</where>");
            } else if (column.isUpdatable()){
                //<if test="<p>!=null">
                //  t.`<c>`=#{<p>[,jdbcType=TIMESTAMP]},
                //  [c.`<c>`=#{<p>[,jdbcType=TIMESTAMP]},]
                //</if>
                sqlSet.append("<if test=\"").append(column.getProperty()).append("!=null").append("\">");
                sqlSet.append("t.`").append(column.getColumn()).append("`")
                        .append("=#{").append(column.getProperty())
                        .append(column.getJavaType().isAssignableFrom(Date.class) ? ",jdbcType=TIMESTAMP" : "")
                        .append("}").append(",");

                //如果需要，更新子节点
                if (isPathField(column)) {
                    //table
                    //<if test="path != null>
                    //  left join <table> c on (c.path like concat(t.path,'/%')
                    //</if>
                    sqlTable.append("<if test=\"").append(column.getProperty()).append("!=null\">");
                    sqlTable.append(" left join ").append(SqlHelper.getDynamicTableName(entityClass, tableName(entityClass)))
                            .append(" c on c.`").append(column.getColumn()).append("` like concat(t.`")
                            .append(column.getColumn()).append("`,'/%')");
                    sqlTable.append("</if>");
                    //set
                    //  c.path=concat(#{path},substring(c.path,char_length(t.path)+1)),
                    sqlSet.append("c.`").append(column.getColumn()).append("`")
                            .append("=concat(#{").append(column.getProperty()).append("},substring(c.`")
                            .append(column.getColumn()).append("`,char_length(t.`")
                            .append(column.getColumn()).append("`)+1)),");
                } else if (isNeedModifyChild(column)) {
                    //c.`<c>`=#{<p>[,jdbcType=TIMESTAMP]},
                    sqlSet.append("c.`").append(column.getColumn()).append("`")
                            .append("=#{").append(column.getProperty())
                            .append(column.getJavaType().isAssignableFrom(Date.class) ? ",jdbcType=TIMESTAMP" : "")
                            .append("}").append(",");
                }

                sqlSet.append("</if>");
            }
        }
        sqlSet.append("</set>");
        StringBuilder sql = (new StringBuilder(sqlTable.toString()))
                .append(sqlSet.toString()).append(sqlWhere.toString());

        return sql.toString();
    }

    /** 生成删除sql */
    private String getDeleteString(MappedStatement ms){
        //获得列名称转换对象
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        StringBuilder sql = new StringBuilder(); //sql语句内<where></where>部分
        //delete from <table>
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        //where path=#{path} or path like concat(#{path},'/%')
        sql.append(" where ");
        for (EntityColumn column : columns) {
            if (isPathField(column)) {
                //  t.path=#{path} or t.path like concat(#{path},'/%')
                sql.append("`").append(column.getColumn()).append("`=#{")
                        .append(column.getProperty()).append("} or `")
                        .append(column.getColumn()).append("` like concat(#{")
                        .append(column.getProperty()).append("},'/%')");
                break;
            }
        }

        return sql.toString();
    }

    private boolean isId(EntityColumn column){
        return "id".equals(column.getProperty());
    }

    private boolean isPathField(EntityColumn column){
        return PROPERTY_NAME_PATH.equals(column.getProperty());
    }

    private boolean isNeedModifyChild(EntityColumn column) {
        return "deleted".equals(column.getProperty())
                || "lastModifyTime".equals(column.getProperty())
                || "lastModifyUserId".equals(column.getProperty());
    }
}
