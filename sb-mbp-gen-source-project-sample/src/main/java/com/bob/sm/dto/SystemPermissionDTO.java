package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 操作许可
 */
public class SystemPermissionDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 操作许可名称

    @Size(max = 255)
    private String description;    // 操作许可描述

    @Size(max = 255)
    private String systemCode;    // 许可所属系统代码（只有在多系统联合配置权限时使用）

    @Size(max = 255)
    private String httpType;    // 操作许可类别（例如：GET/POST/PUT/DELETE等）

    @Size(max = 255)
    private String httpUrl;    // 访问URL

    @Size(max = 255)
    private String functionCategroy;    // 功能归类

    @Min(1)
    @Max(2)
    private Integer nameModified;    // 名称是否已更改（1：未更改  2：已更改）

    @Min(0)
    private Integer currentLevel;    // 当前层级

    @ValidEnum(target = Constants.yesNo.class)
    private Integer allowConfig;    // 是否允许配置（1：是   2：否）

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父操作许可ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemPermissionDTO parent;    // 父操作许可

    private List<SystemPermissionDTO> childList;    // 子操作许可列表

    private List<SystemResourcePermissionDTO> systemResourcePermissionList;    // 资源列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

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
	
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	
    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
	
    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }
	
    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
	
    public String getFunctionCategroy() {
        return functionCategroy;
    }

    public void setFunctionCategroy(String functionCategroy) {
        this.functionCategroy = functionCategroy;
    }
	
    public Integer getNameModified() {
        return nameModified;
    }

    public void setNameModified(Integer nameModified) {
        this.nameModified = nameModified;
    }
	
    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }
	
    public Integer getAllowConfig() {
        return allowConfig;
    }

    public void setAllowConfig(Integer allowConfig) {
        this.allowConfig = allowConfig;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public SystemPermissionDTO getParent() {
        return parent;
    }

    public void setParent(SystemPermissionDTO parent) {
        this.parent = parent;
    }

    public List<SystemPermissionDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<SystemPermissionDTO> childList) {
        this.childList = childList;
    }

    public List<SystemResourcePermissionDTO> getSystemResourcePermissionList() {
        return systemResourcePermissionList;
    }

    public void setSystemResourcePermissionList(List<SystemResourcePermissionDTO> systemResourcePermissionList) {
        this.systemResourcePermissionList = systemResourcePermissionList;
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

        SystemPermissionDTO systemPermissionDTO = (SystemPermissionDTO) o;
        if (systemPermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemPermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemPermissionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", systemCode='" + getSystemCode() + "'" +
            ", httpType='" + getHttpType() + "'" +
            ", httpUrl='" + getHttpUrl() + "'" +
            ", functionCategroy='" + getFunctionCategroy() + "'" +
            ", nameModified=" + getNameModified() +
            ", currentLevel=" + getCurrentLevel() +
            ", allowConfig=" + getAllowConfig() +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", parentId=" + getParentId() +
            "}";
    }
}
