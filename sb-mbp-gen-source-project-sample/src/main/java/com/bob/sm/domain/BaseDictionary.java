package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 数据字典
 */
@Data
@TableName(value = "base_dictionary")
public class BaseDictionary extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String dicType;    // 数据字典类别编码

    private String dicCode;    // 数据字典代码

    private String dicValue;    // 数据字典中文值

    private String dicDiscription;    // 数据字典描述

    private Long insertUserId;    // 创建者用户ID

    private Long parentId;    // 父数据字典ID

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (BaseDictionary.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseDictionary baseDictionary = (BaseDictionary) o;
        if (baseDictionary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), baseDictionary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BaseDictionary{" +
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
