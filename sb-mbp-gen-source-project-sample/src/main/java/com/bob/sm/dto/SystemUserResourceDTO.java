package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 用户资源关系
 */
public class SystemUserResourceDTO extends BaseDTO {

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemUserId;    // 用户ID

    private Long systemResourceId;    // 资源ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemUserDTO systemUser;    // 用户

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

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Long getSystemResourceId() {
        return systemResourceId;
    }

    public void setSystemResourceId(Long systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public SystemUserDTO getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUserDTO systemUser) {
        this.systemUser = systemUser;
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

        SystemUserResourceDTO systemUserResourceDTO = (SystemUserResourceDTO) o;
        if (systemUserResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUserResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemUserResourceDTO{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", systemUserId=" + getSystemUserId() +
			", systemResourceId=" + getSystemResourceId() +
            "}";
    }
}
