package com.bob.sm.dto;

import java.util.List;
import java.util.Objects;

/**
 * 用户账户
 */
public class EnhanceUserDTO extends SystemUserDTO {

    private List<SystemResourceDTO> resourcesFromRole;    // 用户对应的角色的资源列表
    private List<SystemResourceDTO> resourcesFromUser;    // 用户自身的资源列表
    private List<SystemResourceDTO> resources;    // 用户所属角色和用户自身的资源合并列表

    public List<SystemResourceDTO> getResourcesFromRole() {
        return resourcesFromRole;
    }

    public void setResourcesFromRole(List<SystemResourceDTO> resourcesFromRole) {
        this.resourcesFromRole = resourcesFromRole;
    }

    public List<SystemResourceDTO> getResourcesFromUser() {
        return resourcesFromUser;
    }

    public void setResourcesFromUser(List<SystemResourceDTO> resourcesFromUser) {
        this.resourcesFromUser = resourcesFromUser;
    }

    public List<SystemResourceDTO> getResources() {
        return resources;
    }

    public void setResources(List<SystemResourceDTO> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnhanceUserDTO systemUserDTO = (EnhanceUserDTO) o;
        if (systemUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EnhanceUserDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", cell='" + getCell() + "'" +
            ", contractInfo='" + getContractInfo() + "'" +
            ", identifyNo='" + getIdentifyNo() + "'" +
            ", email='" + getEmail() + "'" +
            ", imgRelativePath='" + getImgRelativePath() + "'" +
            ", memo='" + getMemo() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", systemOrganizationId=" + getSystemOrganizationId() +
            "}";
    }
}
