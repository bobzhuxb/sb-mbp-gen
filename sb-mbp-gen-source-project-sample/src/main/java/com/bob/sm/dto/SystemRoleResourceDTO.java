package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 角色资源关系
 */
public class SystemRoleResourceDTO extends BaseDTO {

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemRoleId;    // 角色ID

    private Long systemResourceId;    // 资源ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemRoleDTO systemRole;    // 角色

    private SystemResourceDTO systemResource;    // 资源

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(Long systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public Long getSystemResourceId() {
        return systemResourceId;
    }

    public void setSystemResourceId(Long systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public SystemRoleDTO getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRoleDTO systemRole) {
        this.systemRole = systemRole;
    }

    public SystemResourceDTO getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(SystemResourceDTO systemResource) {
        this.systemResource = systemResource;
    }

    // ================self code:自定义属性的get/set方法start=====================
    // ================self code:自定义属性的get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemRoleResourceDTO systemRoleResourceDTO = (SystemRoleResourceDTO) o;
        if (systemRoleResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemRoleResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemRoleResourceDTO{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", systemRoleId=" + getSystemRoleId() +
			", systemResourceId=" + getSystemResourceId() +
            "}";
    }
}
