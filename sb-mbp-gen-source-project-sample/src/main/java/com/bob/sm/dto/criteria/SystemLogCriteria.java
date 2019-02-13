package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 操作日志 条件过滤器
 */
@ApiModel(description = "操作日志")
public class SystemLogCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "日志类型")
    private StringFilter organizationCode;    // 日志类型

    @ApiModelProperty(value = "日志内容")
    private StringFilter name;    // 日志内容

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemLogCriteria() {
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
        final SystemLogCriteria that = (SystemLogCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(organizationCode, that.organizationCode)
            && Objects.equals(name, that.name)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , organizationCode
        , name
        );
    }

    @Override
    public String toString() {
        return "SystemLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (organizationCode != null ? "organizationCode=" + organizationCode + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                "}";
    }

}
