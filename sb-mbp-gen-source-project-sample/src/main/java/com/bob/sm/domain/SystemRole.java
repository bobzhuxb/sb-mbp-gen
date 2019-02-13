package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 角色
 */
@Data
@TableName(value = "system_role")
public class SystemRole extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;    // 角色标识

    private String chineseName;    // 角色中文名称

    private String description;    // 角色描述

    private Long insertUserId;    // 创建者用户ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemRole.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemRole systemRole = (SystemRole) o;
        if (systemRole.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemRole{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", chineseName='" + getChineseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
