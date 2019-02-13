package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 资源许可关系 条件过滤器
 */
@ApiModel(description = "资源许可关系")
public class SystemResourcePermissionCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "资源ID")
    private LongFilter systemResourceId;    // 资源ID

    @ApiModelProperty(value = "许可ID")
    private LongFilter systemPermissionId;    // 许可ID

    private SystemResourceCriteria systemResource;    // 资源

    private SystemPermissionCriteria systemPermission;    // 许可

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemResourcePermissionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSystemResourceId() {
        return systemResourceId;
    }
	
	public void setSystemResourceId(LongFilter systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public LongFilter getSystemPermissionId() {
        return systemPermissionId;
    }
	
	public void setSystemPermissionId(LongFilter systemPermissionId) {
        this.systemPermissionId = systemPermissionId;
    }

    public SystemResourceCriteria getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(SystemResourceCriteria systemResource) {
        this.systemResource = systemResource;
    }

    public SystemPermissionCriteria getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(SystemPermissionCriteria systemPermission) {
        this.systemPermission = systemPermission;
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
        final SystemResourcePermissionCriteria that = (SystemResourcePermissionCriteria) o;
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
        return "SystemResourcePermissionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                "}";
    }

}
