package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 用户角色关系
 */
@Data
@TableName(value = "system_user_role")
public class SystemUserRole extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long insertUserId;    // 创建者用户ID

    private Long systemUserId;    // 用户ID

    private Long systemRoleId;    // 角色ID

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

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Long getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(Long systemRoleId) {
        this.systemRoleId = systemRoleId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemUserRole.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemUserRole systemUserRole = (SystemUserRole) o;
        if (systemUserRole.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUserRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemUserRole{" +
            "id=" + getId() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", systemUserId=" + getSystemUserId() +
            ", systemRoleId=" + getSystemRoleId() +
            "}";
    }
}
