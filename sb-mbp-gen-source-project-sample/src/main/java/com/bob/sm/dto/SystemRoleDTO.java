package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 角色
 */
public class SystemRoleDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 角色标识

    @NotBlank
    @Size(min = 1, max = 255)
    private String chineseName;    // 角色中文名称

    @Size(max = 255)
    private String description;    // 角色描述

    private Long insertUserId;    // 创建者用户ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private List<SystemUserRoleDTO> systemUserRoleList;    // 用户列表

    private List<SystemRoleResourceDTO> systemRoleResourceList;    // 资源列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
	
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public List<SystemUserRoleDTO> getSystemUserRoleList() {
        return systemUserRoleList;
    }

    public void setSystemUserRoleList(List<SystemUserRoleDTO> systemUserRoleList) {
        this.systemUserRoleList = systemUserRoleList;
    }

    public List<SystemRoleResourceDTO> getSystemRoleResourceList() {
        return systemRoleResourceList;
    }

    public void setSystemRoleResourceList(List<SystemRoleResourceDTO> systemRoleResourceList) {
        this.systemRoleResourceList = systemRoleResourceList;
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

        SystemRoleDTO systemRoleDTO = (SystemRoleDTO) o;
        if (systemRoleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemRoleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemRoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", chineseName='" + getChineseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
