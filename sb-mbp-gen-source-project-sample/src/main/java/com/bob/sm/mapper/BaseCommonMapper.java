package com.bob.sm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 基本Mapper
 * @param <T>
 * @author Bob
 */
public interface BaseCommonMapper<T> extends BaseMapper<T> {

    /**
     * 置空关联字段
     * @param tableName 数据表名
     * @param relationColumnName 关联字段的列名
     * @param relationId 关联字段的列的值
     */
    @Update("UPDATE ${tableName} SET ${relationColumnName} = null WHERE ${relationColumnName} = #{relationId}")
    void cascadeToNull(@Param("tableName") String tableName, @Param("relationColumnName") String relationColumnName,
                       @Param("relationId") String relationId);

    /**
     * 按条件查询列表
     * @param queryMain 主查询条目
     * @param wrapper 查询条件
     * @param limit 限制查询结果数量
     * @return 查询结果
     */
    @Select("<script>"
            + "${queryMain} ${ew.customSqlSegment}"
            + "<if test='limit != null'>"
            + "LIMIT ${limit}"
            + "</if>"
            + "</script>")
    List<T> joinSelectList(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper,
                           @Param("limit") Integer limit);

    /**
     * 按条件查询分页
     * @param page 分页条件
     * @param queryMain 主查询条目
     * @param wrapper 查询条件
     * @return 查询结果
     */
    @Select("${queryMain} ${ew.customSqlSegment}")
    IPage<T> joinSelectPage(Page<T> page, @Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 按条件查询数量
     * @param queryMain 主查询条目
     * @param wrapper 查询条件
     * @return 查询结果
     */
    @Select("${queryMain} ${ew.customSqlSegment}")
    Integer joinSelectCount(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
