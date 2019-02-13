package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 Mapper
 */
public interface SystemUserMapper extends BaseCommonMapper<SystemUser> {

    // 级联置空system_organization_id字段
    void systemOrganizationIdCascadeToNull(@Param("systemOrganizationId")long systemOrganizationId);

}
