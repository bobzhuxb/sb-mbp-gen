package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 操作许可
 */
@Data
@TableName(value = "system_permission")
public class SystemPermission extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;    // 操作许可名称

    private String description;    // 操作许可描述

    private String systemCode;    // 许可所属系统代码（只有在多系统联合配置权限时使用）

    private String httpType;    // 操作许可类别（例如：GET/POST/PUT/DELETE等）

    private String httpUrl;    // 访问URL

    private String functionCategroy;    // 功能归类

    private Integer nameModified;    // 名称是否已更改（1：未更改  2：已更改）

    private Integer currentLevel;    // 当前层级

    private Integer allowConfig;    // 是否允许配置（1：是   2：否）

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父操作许可ID

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
        return (SystemPermission.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemPermission systemPermission = (SystemPermission) o;
        if (systemPermission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemPermission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemPermission{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", systemCode='" + getSystemCode() + "'" +
            ", httpType='" + getHttpType() + "'" +
            ", httpUrl='" + getHttpUrl() + "'" +
            ", functionCategroy='" + getFunctionCategroy() + "'" +
            ", nameModified=" + getNameModified() +
            ", currentLevel=" + getCurrentLevel() +
            ", allowConfig=" + getAllowConfig() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", parentId=" + getParentId() +
            "}";
    }
}
