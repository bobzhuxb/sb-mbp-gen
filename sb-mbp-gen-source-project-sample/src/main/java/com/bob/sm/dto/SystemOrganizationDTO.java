package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 组织机构
 */
public class SystemOrganizationDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String organizationCode;    // 组织机构代码

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 组织机构名称

    @Size(max = 255)
    private String description;    // 组织机构描述

    @Min(0)
    private Integer currentLevel;    // 当前层级

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父组织机构ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemOrganizationDTO parent;    // 父组织机构

    private List<SystemOrganizationDTO> childList;    // 子组织机构列表

    private List<SystemUserDTO> systemUserList;    // 用户列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
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

    public SystemOrganizationDTO getParent() {
        return parent;
    }

    public void setParent(SystemOrganizationDTO parent) {
        this.parent = parent;
    }

    public List<SystemOrganizationDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<SystemOrganizationDTO> childList) {
        this.childList = childList;
    }

    public List<SystemUserDTO> getSystemUserList() {
        return systemUserList;
    }

    public void setSystemUserList(List<SystemUserDTO> systemUserList) {
        this.systemUserList = systemUserList;
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

        SystemOrganizationDTO systemOrganizationDTO = (SystemOrganizationDTO) o;
        if (systemOrganizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemOrganizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemOrganizationDTO{" +
            "id=" + getId() +
            ", organizationCode='" + getOrganizationCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", currentLevel=" + getCurrentLevel() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", parentId=" + getParentId() +
            "}";
    }
}
