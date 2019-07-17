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

public interface BaseCommonMapper<T> extends BaseMapper<T> {

    @Update("UPDATE ${tableName} SET ${relationColumnName} = null WHERE ${relationColumnName} = #{relationId}")
    void cascadeToNull(@Param("tableName") String tableName, @Param("relationColumnName") String relationColumnName,
                       @Param("relationId") long relationId);

    @Select("<script>"
            + "${queryMain} ${ew.customSqlSegment}"
            + "<if test='limit != null'>"
            + "LIMIT ${limit}"
            + "</if>"
            + "</script>")
    List<T> joinSelectList(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper,
                           @Param("limit") Integer limit);

    @Select("${queryMain} ${ew.customSqlSegment}")
    IPage<T> joinSelectPage(Page<T> page, @Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("${queryMain} ${ew.customSqlSegment}")
    Integer joinSelectCount(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
