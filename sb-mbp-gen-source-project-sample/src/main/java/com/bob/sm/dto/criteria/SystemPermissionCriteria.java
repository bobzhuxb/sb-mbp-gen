package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 操作许可 条件过滤器
 */
@ApiModel(description = "操作许可")
public class SystemPermissionCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "操作许可名称")
    private StringFilter name;    // 操作许可名称

    @ApiModelProperty(value = "操作许可描述")
    private StringFilter description;    // 操作许可描述

    @ApiModelProperty(value = "许可所属系统代码（只有在多系统联合配置权限时使用）")
    private StringFilter systemCode;    // 许可所属系统代码（只有在多系统联合配置权限时使用）

    @ApiModelProperty(value = "操作许可类别（例如：GET/POST/PUT/DELETE等）")
    private StringFilter httpType;    // 操作许可类别（例如：GET/POST/PUT/DELETE等）

    @ApiModelProperty(value = "访问URL")
    private StringFilter httpUrl;    // 访问URL

    @ApiModelProperty(value = "功能归类")
    private StringFilter functionCategroy;    // 功能归类

    @ApiModelProperty(value = "名称是否已更改（1：未更改  2：已更改）")
    private IntegerFilter nameModified;    // 名称是否已更改（1：未更改  2：已更改）

    @ApiModelProperty(value = "当前层级")
    private IntegerFilter currentLevel;    // 当前层级

    @ApiModelProperty(value = "是否允许配置（1：是   2：否）")
    private IntegerFilter allowConfig;    // 是否允许配置（1：是   2：否）

    @ApiModelProperty(value = "父操作许可ID")
    private LongFilter parentId;    // 父操作许可ID

    private SystemPermissionCriteria parent;    // 父操作许可

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemPermissionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }
    
    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }
    
    public StringFilter getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(StringFilter systemCode) {
        this.systemCode = systemCode;
    }
    
    public StringFilter getHttpType() {
        return httpType;
    }

    public void setHttpType(StringFilter httpType) {
        this.httpType = httpType;
    }
    
    public StringFilter getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(StringFilter httpUrl) {
        this.httpUrl = httpUrl;
    }
    
    public StringFilter getFunctionCategroy() {
        return functionCategroy;
    }

    public void setFunctionCategroy(StringFilter functionCategroy) {
        this.functionCategroy = functionCategroy;
    }
    
    public IntegerFilter getNameModified() {
        return nameModified;
    }

    public void setNameModified(IntegerFilter nameModified) {
        this.nameModified = nameModified;
    }
    
    public IntegerFilter getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(IntegerFilter currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public IntegerFilter getAllowConfig() {
        return allowConfig;
    }

    public void setAllowConfig(IntegerFilter allowConfig) {
        this.allowConfig = allowConfig;
    }

    public LongFilter getParentId() {
        return parentId;
    }
	
	public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public SystemPermissionCriteria getParent() {
        return parent;
    }

    public void setParent(SystemPermissionCriteria parent) {
        this.parent = parent;
    }

    // ================self code:增强的查询条件get/set方法start=====================
    // ================self code:增强的查询条件get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemPermissionCriteria that = (SystemPermissionCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(systemCode, that.systemCode)
            && Objects.equals(httpType, that.httpType)
            && Objects.equals(httpUrl, that.httpUrl)
            && Objects.equals(functionCategroy, that.functionCategroy)
            && Objects.equals(nameModified, that.nameModified)
            && Objects.equals(currentLevel, that.currentLevel)
            && Objects.equals(allowConfig, that.allowConfig)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , name
        , description
        , systemCode
        , httpType
        , httpUrl
        , functionCategroy
        , nameModified
        , currentLevel
        , allowConfig
        );
    }

    @Override
    public String toString() {
        return "SystemPermissionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (systemCode != null ? "systemCode=" + systemCode + ", " : "") +
                (httpType != null ? "httpType=" + httpType + ", " : "") +
                (httpUrl != null ? "httpUrl=" + httpUrl + ", " : "") +
                (functionCategroy != null ? "functionCategroy=" + functionCategroy + ", " : "") +
                (nameModified != null ? "nameModified=" + nameModified + ", " : "") +
                (currentLevel != null ? "currentLevel=" + currentLevel + ", " : "") +
                (allowConfig != null ? "allowConfig=" + allowConfig + ", " : "") +
                "}";
    }

}
