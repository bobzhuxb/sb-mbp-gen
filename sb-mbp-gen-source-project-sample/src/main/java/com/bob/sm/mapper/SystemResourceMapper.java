package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 资源 Mapper
 */
public interface SystemResourceMapper extends BaseCommonMapper<SystemResource> {

    // 级联置空parent_id字段
    void parentIdCascadeToNull(@Param("parentId")long parentId);

}
