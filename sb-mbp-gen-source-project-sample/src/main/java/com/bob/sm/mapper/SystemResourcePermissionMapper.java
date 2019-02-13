package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 资源许可关系 Mapper
 */
public interface SystemResourcePermissionMapper extends BaseCommonMapper<SystemResourcePermission> {

    // 级联置空system_resource_id字段
    void systemResourceIdCascadeToNull(@Param("systemResourceId")long systemResourceId);

    // 级联置空system_permission_id字段
    void systemPermissionIdCascadeToNull(@Param("systemPermissionId")long systemPermissionId);

}
