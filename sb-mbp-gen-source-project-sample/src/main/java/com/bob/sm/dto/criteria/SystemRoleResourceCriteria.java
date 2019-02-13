package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 角色资源关系 条件过滤器
 */
@ApiModel(description = "角色资源关系")
public class SystemRoleResourceCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "角色ID")
    private LongFilter systemRoleId;    // 角色ID

    @ApiModelProperty(value = "资源ID")
    private LongFilter systemResourceId;    // 资源ID

    private SystemRoleCriteria systemRole;    // 角色

    private SystemResourceCriteria systemResource;    // 资源

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemRoleResourceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSystemRoleId() {
        return systemRoleId;
    }
	
	public void setSystemRoleId(LongFilter systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public LongFilter getSystemResourceId() {
        return systemResourceId;
    }
	
	public void setSystemResourceId(LongFilter systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public SystemRoleCriteria getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRoleCriteria systemRole) {
        this.systemRole = systemRole;
    }

    public SystemResourceCriteria getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(SystemResourceCriteria systemResource) {
        this.systemResource = systemResource;
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
        final SystemRoleResourceCriteria that = (SystemRoleResourceCriteria) o;
        return
            Objects.equals(id, that.id)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        );
    }

    @Override
    public String toString() {
        return "SystemRoleResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                "}";
    }

}
