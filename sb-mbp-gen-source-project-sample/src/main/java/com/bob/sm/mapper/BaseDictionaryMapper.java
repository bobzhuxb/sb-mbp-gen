package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 数据字典 Mapper
 */
public interface BaseDictionaryMapper extends BaseCommonMapper<BaseDictionary> {

    // 级联置空parent_id字段
    void parentIdCascadeToNull(@Param("parentId")long parentId);

}
