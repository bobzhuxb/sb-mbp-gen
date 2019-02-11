package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 数据字典 条件过滤器
 */
@ApiModel(description = "数据字典")
public class BaseDictionaryCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "数据字典类别编码")
    private StringFilter dicType;    // 数据字典类别编码

    @ApiModelProperty(value = "数据字典代码")
    private StringFilter dicCode;    // 数据字典代码

    @ApiModelProperty(value = "数据字典中文值")
    private StringFilter dicValue;    // 数据字典中文值

    @ApiModelProperty(value = "数据字典描述")
    private StringFilter dicDiscription;    // 数据字典描述

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "父数据字典ID")
    private LongFilter parentId;    // 父数据字典ID

    private BaseDictionaryCriteria parent;    // 父数据字典

    public BaseDictionaryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getDicType() {
        return dicType;
    }

    public void setDicType(StringFilter dicType) {
        this.dicType = dicType;
    }
    
    public StringFilter getDicCode() {
        return dicCode;
    }

    public void setDicCode(StringFilter dicCode) {
        this.dicCode = dicCode;
    }
    
    public StringFilter getDicValue() {
        return dicValue;
    }

    public void setDicValue(StringFilter dicValue) {
        this.dicValue = dicValue;
    }
    
    public StringFilter getDicDiscription() {
        return dicDiscription;
    }

    public void setDicDiscription(StringFilter dicDiscription) {
        this.dicDiscription = dicDiscription;
    }
    
    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LongFilter getParentId() {
        return parentId;
    }
	
	public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public BaseDictionaryCriteria getParent() {
        return parent;
    }

    public void setParent(BaseDictionaryCriteria parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseDictionaryCriteria that = (BaseDictionaryCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(dicType, that.dicType)
            && Objects.equals(dicCode, that.dicCode)
            && Objects.equals(dicValue, that.dicValue)
            && Objects.equals(dicDiscription, that.dicDiscription)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , dicType
        , dicCode
        , dicValue
        , dicDiscription
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "BaseDictionaryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dicType != null ? "dicType=" + dicType + ", " : "") +
                (dicCode != null ? "dicCode=" + dicCode + ", " : "") +
                (dicValue != null ? "dicValue=" + dicValue + ", " : "") +
                (dicDiscription != null ? "dicDiscription=" + dicDiscription + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
