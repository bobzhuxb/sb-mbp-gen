package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 操作许可 Mapper
 */
public interface SystemPermissionMapper extends BaseCommonMapper<SystemPermission> {

    // 级联置空parent_id字段
    void parentIdCascadeToNull(@Param("parentId")long parentId);

}
