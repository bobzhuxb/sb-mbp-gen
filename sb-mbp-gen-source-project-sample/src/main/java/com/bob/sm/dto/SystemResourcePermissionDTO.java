package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 资源许可关系
 */
public class SystemResourcePermissionDTO extends BaseDTO {

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemResourceId;    // 资源ID

    private Long systemPermissionId;    // 许可ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemResourceDTO systemResource;    // 资源

    private SystemPermissionDTO systemPermission;    // 许可

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

    public Long getSystemResourceId() {
        return systemResourceId;
    }

    public void setSystemResourceId(Long systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public Long getSystemPermissionId() {
        return systemPermissionId;
    }

    public void setSystemPermissionId(Long systemPermissionId) {
        this.systemPermissionId = systemPermissionId;
    }

    public SystemResourceDTO getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(SystemResourceDTO systemResource) {
        this.systemResource = systemResource;
    }

    public SystemPermissionDTO getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(SystemPermissionDTO systemPermission) {
        this.systemPermission = systemPermission;
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

        SystemResourcePermissionDTO systemResourcePermissionDTO = (SystemResourcePermissionDTO) o;
        if (systemResourcePermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemResourcePermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemResourcePermissionDTO{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", systemResourceId=" + getSystemResourceId() +
			", systemPermissionId=" + getSystemPermissionId() +
            "}";
    }
}
