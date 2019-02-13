package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户资源关系 条件过滤器
 */
@ApiModel(description = "用户资源关系")
public class SystemUserResourceCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "用户ID")
    private LongFilter systemUserId;    // 用户ID

    @ApiModelProperty(value = "资源ID")
    private LongFilter systemResourceId;    // 资源ID

    private SystemUserCriteria systemUser;    // 用户

    private SystemResourceCriteria systemResource;    // 资源

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemUserResourceCriteria() {
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

    public LongFilter getSystemResourceId() {
        return systemResourceId;
    }
	
	public void setSystemResourceId(LongFilter systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public SystemUserCriteria getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUserCriteria systemUser) {
        this.systemUser = systemUser;
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
        final SystemUserResourceCriteria that = (SystemUserResourceCriteria) o;
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
        return "SystemUserResourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                "}";
    }

}
