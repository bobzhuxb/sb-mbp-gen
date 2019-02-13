package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户资源关系 Mapper
 */
public interface SystemUserResourceMapper extends BaseCommonMapper<SystemUserResource> {

    // 级联置空system_user_id字段
    void systemUserIdCascadeToNull(@Param("systemUserId")long systemUserId);

    // 级联置空system_resource_id字段
    void systemResourceIdCascadeToNull(@Param("systemResourceId")long systemResourceId);

}
