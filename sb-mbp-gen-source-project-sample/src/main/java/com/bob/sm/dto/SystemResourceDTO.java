package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 资源
 */
public class SystemResourceDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String resourceType;    // 资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 资源名称

    @Size(max = 255)
    private String description;    // 资源描述

    @Size(max = 255)
    private String systemCode;    // 资源所属系统代码（只有在多系统联合配置权限时使用）

    @Size(max = 255)
    private String identify;    // 资源特性标识（同一系统，同一类别内资源特性标识）

    @Min(0)
    private Integer currentLevel;    // 当前层级

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父资源ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemResourceDTO parent;    // 父资源

    private List<SystemResourceDTO> childList;    // 子资源列表

    private List<SystemResourcePermissionDTO> systemResourcePermissionList;    // 许可列表

    private List<SystemRoleResourceDTO> systemRoleResourceList;    // 角色列表

    private List<SystemUserResourceDTO> systemUserResourceList;    // 用户列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	
    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
	
    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
	
    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public SystemResourceDTO getParent() {
        return parent;
    }

    public void setParent(SystemResourceDTO parent) {
        this.parent = parent;
    }

    public List<SystemResourceDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<SystemResourceDTO> childList) {
        this.childList = childList;
    }

    public List<SystemResourcePermissionDTO> getSystemResourcePermissionList() {
        return systemResourcePermissionList;
    }

    public void setSystemResourcePermissionList(List<SystemResourcePermissionDTO> systemResourcePermissionList) {
        this.systemResourcePermissionList = systemResourcePermissionList;
    }

    public List<SystemRoleResourceDTO> getSystemRoleResourceList() {
        return systemRoleResourceList;
    }

    public void setSystemRoleResourceList(List<SystemRoleResourceDTO> systemRoleResourceList) {
        this.systemRoleResourceList = systemRoleResourceList;
    }

    public List<SystemUserResourceDTO> getSystemUserResourceList() {
        return systemUserResourceList;
    }

    public void setSystemUserResourceList(List<SystemUserResourceDTO> systemUserResourceList) {
        this.systemUserResourceList = systemUserResourceList;
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

        SystemResourceDTO systemResourceDTO = (SystemResourceDTO) o;
        if (systemResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemResourceDTO{" +
            "id=" + getId() +
            ", resourceType='" + getResourceType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", systemCode='" + getSystemCode() + "'" +
            ", identify='" + getIdentify() + "'" +
            ", currentLevel=" + getCurrentLevel() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", parentId=" + getParentId() +
            "}";
    }
}
