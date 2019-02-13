package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户角色关系 条件过滤器
 */
@ApiModel(description = "用户角色关系")
public class SystemUserRoleCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "用户ID")
    private LongFilter systemUserId;    // 用户ID

    @ApiModelProperty(value = "角色ID")
    private LongFilter systemRoleId;    // 角色ID

    private SystemUserCriteria systemUser;    // 用户

    private SystemRoleCriteria systemRole;    // 角色

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemUserRoleCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSystemUserId() {
        return systemUserId;
    }
	
	public void setSystemUserId(LongFilter systemUserId) {
        this.systemUserId = systemUserId;
    }

    public LongFilter getSystemRoleId() {
        return systemRoleId;
    }
	
	public void setSystemRoleId(LongFilter systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public SystemUserCriteria getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUserCriteria systemUser) {
        this.systemUser = systemUser;
    }

    public SystemRoleCriteria getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRoleCriteria systemRole) {
        this.systemRole = systemRole;
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
        final SystemUserRoleCriteria that = (SystemUserRoleCriteria) o;
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
        return "SystemUserRoleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                "}";
    }

}
