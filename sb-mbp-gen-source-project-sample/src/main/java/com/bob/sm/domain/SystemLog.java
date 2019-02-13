package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 操作日志
 */
@Data
@TableName(value = "system_log")
public class SystemLog extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String organizationCode;    // 日志类型

    private String name;    // 日志内容

    private Long insertUserId;    // 创建者用户ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return (SystemLog.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemLog systemLog = (SystemLog) o;
        if (systemLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemLog{" +
            "id=" + getId() +
            ", organizationCode='" + getOrganizationCode() + "'" +
            ", name='" + getName() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
