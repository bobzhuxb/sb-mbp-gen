package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关系 Mapper
 */
public interface SystemUserRoleMapper extends BaseCommonMapper<SystemUserRole> {

    // 级联置空system_user_id字段
    void systemUserIdCascadeToNull(@Param("systemUserId")long systemUserId);

    // 级联置空system_role_id字段
    void systemRoleIdCascadeToNull(@Param("systemRoleId")long systemRoleId);

}
