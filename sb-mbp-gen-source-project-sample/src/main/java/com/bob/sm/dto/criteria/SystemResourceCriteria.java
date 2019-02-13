package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 资源 条件过滤器
 */
@ApiModel(description = "资源")
public class SystemResourceCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）")
    private StringFilter resourceType;    // 资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）

    @ApiModelProperty(value = "资源名称")
    private StringFilter name;    // 资源名称

    @ApiModelProperty(value = "资源描述")
    private StringFilter description;    // 资源描述

    @ApiModelProperty(value = "资源所属系统代码（只有在多系统联合配置权限时使用）")
    private StringFilter systemCode;    // 资源所属系统代码（只有在多系统联合配置权限时使用）

    @ApiModelProperty(value = "资源特性标识（同一系统，同一类别内资源特性标识）")
    private StringFilter identify;    // 资源特性标识（同一系统，同一类别内资源特性标识）

    @ApiModelProperty(value = "当前层级")
    private IntegerFilter currentLevel;    // 当前层级

    @ApiModelProperty(value = "父资源ID")
    private LongFilter parentId;    // 父资源ID

    private SystemResourceCriteria parent;    // 父资源

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemResourceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getResourceType() {
        return resourceType;
    }

    public void setResourceType(StringFilter resourceType) {
        this.resourceType = resourceType;
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
    
    public StringFilter getIdentify() {
        return identify;
    }

    public void setIdentify(StringFilter identify) {
        this.identify = identify;
    }
    
    public IntegerFilter getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(IntegerFilter currentLevel) {
        this.currentLevel = currentLevel;
    }

    public LongFilter getParentId() {
        return parentId;
    }
	
	public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public SystemResourceCriteria getParent() {
        return parent;
    }

    public void setParent(SystemResourceCriteria parent) {
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
        final SystemResourceCriteria that = (SystemResourceCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(resourceType, that.resourceType)
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(systemCode, that.systemCode)
            && Objects.equals(identify, that.identify)
            && Objects.equals(currentLevel, that.currentLevel)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , resourceType
        , name
        , description
        , systemCode
        , identify
        , currentLevel
        );
    }

    @Override
    public String toString() {
        return "SystemResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (resourceType != null ? "resourceType=" + resourceType + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (systemCode != null ? "systemCode=" + systemCode + ", " : "") +
                (identify != null ? "identify=" + identify + ", " : "") +
                (currentLevel != null ? "currentLevel=" + currentLevel + ", " : "") +
                "}";
    }

}
