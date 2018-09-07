package com.maoding.core.base;

import com.maoding.utils.StringUtils;
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

    public CoreMapper(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 描述       通用更新接口
     * 日期       2018/9/7
     * @author   张成亮
     **/
    public String coreUpdate(MappedStatement ms){
        return getUpdateString(ms);
    }

    /** 生成更新sql */
    private String getUpdateString(MappedStatement ms){
        final String PARAM_ENTITY_NAME = "entity";

        //获得列名称转换对象
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        //获取pathField参数
        String pathField = null;
        for (EntityColumn column : columns) {
            if (isPathField(column)){
                pathField = column.getProperty();
                break;
            }
        }

        StringBuilder sqlTable = new StringBuilder(); //sql语句内update <table>部分
        StringBuilder sqlSet = new StringBuilder(); //sql语句内<set></set>部分
        StringBuilder sqlWhere = new StringBuilder(); //sql语句内<where></where>部分
        //update <table> t
        sqlTable.append(SqlHelper.updateTable(entityClass,tableName(entityClass))).append(" t");
        //根据各字段进行更新，id字段和输入为null的字段不更新
        sqlSet.append("<set>");
        sqlWhere.append("<where>");
        for (EntityColumn column : columns) {
            if (!column.isUpdatable()){
                continue;
            }
            if (isId(column)) {
                sqlWhere.append(getIdCondition(column));
            } else {
                //<if test="entity.<p>!=null">
                sqlSet.append("<if test=\"").append(PARAM_ENTITY_NAME)
                        .append(".").append(column.getProperty()).append("!=null")
                        .append("\">");
                //t.`<c>`=#{entity.<p>[,jdbcType=TIMESTAMP]},
                sqlSet.append("t.`").append(column.getColumn()).append("`")
                        .append("=#{").append(PARAM_ENTITY_NAME).append(".")
                        .append(column.getProperty());
                if (column.getJavaType().isAssignableFrom(Date.class)){
                    sqlSet.append(",jdbcType=TIMESTAMP");
                }
                sqlSet.append("}").append(",");
                //如果是路径字段，添加路径更改SQL
                if (StringUtils.isSame(column.getProperty(),pathField)){
                    //<if test="entity.path != null">
                    //  left join <table> c on (c.path like concat(t.path,'/%')
                    //</if>
                    sqlTable.append("<if test=\"").append(PARAM_ENTITY_NAME).append(".")
                            .append(column.getProperty()).append("!=null\">");
                    sqlTable.append("left join ").append(SqlHelper.getDynamicTableName(entityClass,tableName(entityClass)))
                            .append(" c on c.").append(column.getColumn()).append(" like concat(t.")
                            .append(column.getColumn()).append(",'/%')");
                    sqlTable.append("</if>");
                    //c.path=concat(#{entity.path},substring(c.path,char_length(t.path)+1)),
                    sqlSet.append("c.").append(column.getColumn())
                            .append("=concat(#{").append(PARAM_ENTITY_NAME).append(".")
                            .append(column.getProperty()).append("},substring(c.")
                            .append(column.getColumn()).append(",char_length(t.")
                            .append(column.getColumn()).append(")+1)),");
                }
                //</if>
                sqlSet.append("</if>");
            }
        }
        sqlSet.append("</set>");
        sqlWhere.append("</where>");
        StringBuilder sql = (new StringBuilder(sqlTable.toString()))
                .append(sqlSet.toString()).append(sqlWhere.toString());

        return sql.toString();
    }

    /** 生成删除sql */
    private String getDeleteString(MappedStatement ms){
        final Class<?> entityClass = getEntityClass(ms); //获得列名称转换对象

        StringBuilder sql = new StringBuilder(); //sql语句
        StringBuilder sqlWhere = new StringBuilder(); //sql语句内where部分
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        //update <table> t
        sql.append(SqlHelper.updateTable(entityClass,tableName(entityClass))).append(" t");
        //set deleted=1,last_modify_user_id=#{lastModifyUserId},lastModifyTime=时间
        sql.append("<set>");
        sqlWhere.append("<where>");
        for (EntityColumn column : columns) {
            if (!column.isUpdatable()) continue;
            if (isDeleted(column)) {
                // set deleted=1
                sql.append("t.`").append(column.getColumn()).append("`").append("=1").append(",");
                // and deleted=0
                sqlWhere.append(" and t.`").append(column.getColumn()).append("`").append("=0");
            } else if (isId(column)) {
                sqlWhere.append(getIdCondition(column));
            } else if (isLastModifyUserId(column)) {
                sql.append(getLastModifyUserIdSetString(column));
            } else if (isLastModifyRoleId(column)) {
                sql.append(getLastModifyRoleIdSetString(column));
            } else if (isLastModifyTime(column)) {
                sql.append(getLastModifyTimeSetString(column));
            }
        }
        sql.append("</set>");
        sqlWhere.append("</where>");

        sql.append(sqlWhere.toString());

        return sql.toString();
    }

    /**
     *   <if test="idList != null and idList.size() > 0">
     *       and t.`id` in
     *       <foreach collection="idList" item="id" open="(" separator="," close=")">
     *          #{id}
     *       </foreach>
     *   </if>
     */
    private String getIdCondition(EntityColumn column){
        final String ID_LIST_PARAM = "idList"; //@Param内定义的字符串
        return "<if test=\"" + ID_LIST_PARAM + "!=null and " + ID_LIST_PARAM + ".size() > 0\">" +
                " and t.`" + column.getColumn() + "` in " +
                "<foreach collection=\"" + ID_LIST_PARAM + "\" item=\"id\" open=\"(\" separator=\",\" close=\")\">" +
                "#{id}" +
                "</foreach>" +
                "</if>";
    }

    /**
     *  <if test="lastModifyUserId != null">
     *      t.`<c>` = #{lastModifyUserId},
     *  </if>
     */
    private String getLastModifyUserIdSetString(EntityColumn column){
        final String PARAM_NAME = "lastModifyUserId"; //@Param内定义的字符串
        return "<if test=\"" + PARAM_NAME + "!=null\">" +
                "t.`" + column.getColumn() + "`" + "=#{" + PARAM_NAME + "}" + "," +
                "</if>";
    }

    /**
     *  <if test="lastModifyRoleId != null">
     *      t.`<c>` = #{lastModifyRoleId},
     *  </if>
     */
    private String getLastModifyRoleIdSetString(EntityColumn column){
        final String PARAM_NAME = "lastModifyRoleId"; //@Param内定义的字符串
        return "<if test=\"" + PARAM_NAME + "!=null\">" +
                "t.`" + column.getColumn() + "`" + "=#{" + PARAM_NAME + "}" + "," +
                "</if>";
    }

    /**
     *  <choose>
     *     <when test="lastModifyTime != null">
     *         t.`<c>` = #{lastModifyTime},
     *     </when>
     *     <otherwise>
     *         t.`<c>` = now()
     *     </otherwise>
     *  </choose>
     */
    private String getLastModifyTimeSetString(EntityColumn column){
        final String PARAM_NAME = "lastModifyTime"; //@Param内定义的字符串
        return "<choose>" +
                "<when test=\"" + PARAM_NAME + "!=null\">" +
                "t.`" + column.getColumn() + "`" + "=#{" + PARAM_NAME + "}" + "," +
                "</when>" +
                "<otherwise>" +
                "t.`" + column.getColumn() + "`" + "=now()" + "," +
                "</otherwise>" +
                "</choose>";
    }

    private boolean isId(EntityColumn column){
        return "id".equals(column.getColumn());
    }

    private boolean isPathField(EntityColumn column){
        return "pathField".equals(column.getColumn());
    }
}
