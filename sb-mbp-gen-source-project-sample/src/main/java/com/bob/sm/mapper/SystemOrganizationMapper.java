package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 组织机构 Mapper
 */
public interface SystemOrganizationMapper extends BaseCommonMapper<SystemOrganization> {

    // 级联置空parent_id字段
    void parentIdCascadeToNull(@Param("parentId")long parentId);

}
