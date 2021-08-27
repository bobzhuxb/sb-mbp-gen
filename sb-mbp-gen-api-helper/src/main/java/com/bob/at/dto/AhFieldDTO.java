package com.bob.at.dto;

import java.util.Objects;

/**
 * 实体类对应的字段
 * @author Bob
 */
public class AhFieldDTO extends BaseDTO {

    private String typeName;        // 字段类型名

    private String genericTypeName; // 泛型名

    private String fieldName;       // 字段名

    private String ahClassCodeId;   // 实体类ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private AhClassCodeDTO ahClassCode;    // 项目

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getGenericTypeName() {
        return genericTypeName;
    }

    public void setGenericTypeName(String genericTypeName) {
        this.genericTypeName = genericTypeName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAhClassCodeId() {
        return ahClassCodeId;
    }

    public void setAhClassCodeId(String ahClassCodeId) {
        this.ahClassCodeId = ahClassCodeId;
    }

    public AhClassCodeDTO getAhClassCode() {
        return ahClassCode;
    }

    public void setAhClassCode(AhClassCodeDTO ahClassCode) {
        this.ahClassCode = ahClassCode;
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

        AhFieldDTO ahClassCodeDTO = (AhFieldDTO) o;
        if (ahClassCodeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahClassCodeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhClassCodeDTO{" +
                "id=" + getId() +
                ", typeName='" + getTypeName() + "'" +
                ", genericTypeName='" + getGenericTypeName() + "'" +
                ", fieldName='" + getFieldName() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
			    ", ahClassCodeId=" + getAhClassCodeId() +
                "}";
    }
}
