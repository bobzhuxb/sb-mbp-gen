package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 操作日志
 */
public class SystemLogDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String organizationCode;    // 日志类型

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 日志内容

    private Long insertUserId;    // 创建者用户ID

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

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

    // ================self code:自定义属性的get/set方法start=====================
    // ================self code:自定义属性的get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemLogDTO systemLogDTO = (SystemLogDTO) o;
        if (systemLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemLogDTO{" +
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
