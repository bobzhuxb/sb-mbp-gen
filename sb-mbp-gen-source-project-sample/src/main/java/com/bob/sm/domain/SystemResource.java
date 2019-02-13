package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 资源
 */
@Data
@TableName(value = "system_resource")
public class SystemResource extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String resourceType;    // 资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）

    private String name;    // 资源名称

    private String description;    // 资源描述

    private String systemCode;    // 资源所属系统代码（只有在多系统联合配置权限时使用）

    private String identify;    // 资源特性标识（同一系统，同一类别内资源特性标识）

    private Integer currentLevel;    // 当前层级

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父资源ID

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemResource.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemResource systemResource = (SystemResource) o;
        if (systemResource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemResource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemResource{" +
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
