package com.bob.sm.dto.help;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the BasePermission entity.
 */
public class BasePermissionDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String systemCode;

    private String httpType;

    private String httpUrl;

    private String functionCategroy;

    private Integer nameModified;

    private Long parentId;

    private Integer currentLevel;

    private Integer allowConfig;

    private String insertTime;

    private String updateTime;

    private List<BasePermissionDTO> childPermissionList;

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

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getFunctionCategroy() {
        return functionCategroy;
    }

    public void setFunctionCategroy(String functionCategroy) {
        this.functionCategroy = functionCategroy;
    }

    public Integer getNameModified() {
        return nameModified;
    }

    public void setNameModified(Integer nameModified) {
        this.nameModified = nameModified;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getAllowConfig() {
        return allowConfig;
    }

    public void setAllowConfig(Integer allowConfig) {
        this.allowConfig = allowConfig;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<BasePermissionDTO> getChildPermissionList() {
        return childPermissionList;
    }

    public void setChildPermissionList(List<BasePermissionDTO> childPermissionList) {
        this.childPermissionList = childPermissionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BasePermissionDTO basePermissionDTO = (BasePermissionDTO) o;
        if (basePermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), basePermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BasePermissionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", systemCode='" + getSystemCode() + "'" +
            ", httpType='" + getHttpType() + "'" +
            ", httpUrl='" + getHttpUrl() + "'" +
            ", functionCategroy='" + getFunctionCategroy() + "'" +
            ", nameModified=" + getNameModified() +
            ", parentId=" + getParentId() +
            ", currentLevel=" + getCurrentLevel() +
            ", allowConfig=" + getAllowConfig() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", childPermissionList='" + getChildPermissionList() + "'" +
            "}";
    }
}
