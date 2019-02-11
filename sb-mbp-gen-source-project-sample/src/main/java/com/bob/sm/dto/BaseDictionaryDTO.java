package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 数据字典
 */
public class BaseDictionaryDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String dicType;    // 数据字典类别编码

    @NotBlank
    @Size(min = 1, max = 255)
    private String dicCode;    // 数据字典代码

    @NotBlank
    @Size(min = 1, max = 255)
    private String dicValue;    // 数据字典中文值

    @Size(min = 1, max = 255)
    private String dicDiscription;    // 数据字典描述

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父数据字典ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private BaseDictionaryDTO parent;    // 父数据字典

    private List<BaseDictionaryDTO> childList;    // 子数据字典列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }
	
    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }
	
    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }
	
    public String getDicDiscription() {
        return dicDiscription;
    }

    public void setDicDiscription(String dicDiscription) {
        this.dicDiscription = dicDiscription;
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

    public BaseDictionaryDTO getParent() {
        return parent;
    }

    public void setParent(BaseDictionaryDTO parent) {
        this.parent = parent;
    }

    public List<BaseDictionaryDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<BaseDictionaryDTO> childList) {
        this.childList = childList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseDictionaryDTO baseDictionaryDTO = (BaseDictionaryDTO) o;
        if (baseDictionaryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), baseDictionaryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BaseDictionaryDTO{" +
            "id=" + getId() +
            ", dicType='" + getDicType() + "'" +
            ", dicCode='" + getDicCode() + "'" +
            ", dicValue='" + getDicValue() + "'" +
            ", dicDiscription='" + getDicDiscription() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", parentId=" + getParentId() +
            "}";
    }
}
