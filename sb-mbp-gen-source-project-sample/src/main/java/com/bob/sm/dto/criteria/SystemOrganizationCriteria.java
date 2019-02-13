package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 组织机构 条件过滤器
 */
@ApiModel(description = "组织机构")
public class SystemOrganizationCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "组织机构代码")
    private StringFilter organizationCode;    // 组织机构代码

    @ApiModelProperty(value = "组织机构名称")
    private StringFilter name;    // 组织机构名称

    @ApiModelProperty(value = "组织机构描述")
    private StringFilter description;    // 组织机构描述

    @ApiModelProperty(value = "当前层级")
    private IntegerFilter currentLevel;    // 当前层级

    @ApiModelProperty(value = "父组织机构ID")
    private LongFilter parentId;    // 父组织机构ID

    private SystemOrganizationCriteria parent;    // 父组织机构

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemOrganizationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(StringFilter organizationCode) {
        this.organizationCode = organizationCode;
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

    public SystemOrganizationCriteria getParent() {
        return parent;
    }

    public void setParent(SystemOrganizationCriteria parent) {
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
        final SystemOrganizationCriteria that = (SystemOrganizationCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(organizationCode, that.organizationCode)
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(currentLevel, that.currentLevel)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , organizationCode
        , name
        , description
        , currentLevel
        );
    }

    @Override
    public String toString() {
        return "SystemOrganizationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (organizationCode != null ? "organizationCode=" + organizationCode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (currentLevel != null ? "currentLevel=" + currentLevel + ", " : "") +
                "}";
    }

}
