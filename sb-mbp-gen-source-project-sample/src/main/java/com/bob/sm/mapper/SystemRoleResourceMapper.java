package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 角色资源关系 Mapper
 */
public interface SystemRoleResourceMapper extends BaseCommonMapper<SystemRoleResource> {

    // 级联置空system_role_id字段
    void systemRoleIdCascadeToNull(@Param("systemRoleId")long systemRoleId);

    // 级联置空system_resource_id字段
    void systemResourceIdCascadeToNull(@Param("systemResourceId")long systemResourceId);

}
