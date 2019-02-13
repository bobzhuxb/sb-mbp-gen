package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 用户角色关系
 */
public class SystemUserRoleDTO extends BaseDTO {

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemUserId;    // 用户ID

    private Long systemRoleId;    // 角色ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemUserDTO systemUser;    // 用户

    private SystemRoleDTO systemRole;    // 角色

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

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Long getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(Long systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public SystemUserDTO getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUserDTO systemUser) {
        this.systemUser = systemUser;
    }

    public SystemRoleDTO getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRoleDTO systemRole) {
        this.systemRole = systemRole;
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

        SystemUserRoleDTO systemUserRoleDTO = (SystemUserRoleDTO) o;
        if (systemUserRoleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUserRoleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemUserRoleDTO{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", systemUserId=" + getSystemUserId() +
			", systemRoleId=" + getSystemRoleId() +
            "}";
    }
}
