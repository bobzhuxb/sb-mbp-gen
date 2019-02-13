package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 资源许可关系
 */
@Data
@TableName(value = "system_resource_permission")
public class SystemResourcePermission extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemResourceId;    // 资源ID

    private Long systemPermissionId;    // 许可ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getSystemResourceId() {
        return systemResourceId;
    }

    public void setSystemResourceId(Long systemResourceId) {
        this.systemResourceId = systemResourceId;
    }

    public Long getSystemPermissionId() {
        return systemPermissionId;
    }

    public void setSystemPermissionId(Long systemPermissionId) {
        this.systemPermissionId = systemPermissionId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemResourcePermission.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemResourcePermission systemResourcePermission = (SystemResourcePermission) o;
        if (systemResourcePermission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemResourcePermission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemResourcePermission{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", systemResourceId=" + getSystemResourceId() +
            ", systemPermissionId=" + getSystemPermissionId() +
            "}";
    }
}
