package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 组织机构
 */
@Data
@TableName(value = "system_organization")
public class SystemOrganization extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String organizationCode;    // 组织机构代码

    private String name;    // 组织机构名称

    private String description;    // 组织机构描述

    private Integer currentLevel;    // 当前层级

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父组织机构ID

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemOrganization.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemOrganization systemOrganization = (SystemOrganization) o;
        if (systemOrganization.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemOrganization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemOrganization{" +
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
