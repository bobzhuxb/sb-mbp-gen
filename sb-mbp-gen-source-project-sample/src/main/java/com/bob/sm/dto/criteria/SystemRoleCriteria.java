package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 角色 条件过滤器
 */
@ApiModel(description = "角色")
public class SystemRoleCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "角色标识")
    private StringFilter name;    // 角色标识

    @ApiModelProperty(value = "角色中文名称")
    private StringFilter chineseName;    // 角色中文名称

    @ApiModelProperty(value = "角色描述")
    private StringFilter description;    // 角色描述

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemRoleCriteria() {
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
    
    public StringFilter getChineseName() {
        return chineseName;
    }

    public void setChineseName(StringFilter chineseName) {
        this.chineseName = chineseName;
    }
    
    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final SystemRoleCriteria that = (SystemRoleCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(chineseName, that.chineseName)
            && Objects.equals(description, that.description)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , name
        , chineseName
        , description
        );
    }

    @Override
    public String toString() {
        return "SystemRoleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (chineseName != null ? "chineseName=" + chineseName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                "}";
    }

}
