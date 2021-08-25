package com.bob.at.domain;

import java.util.Objects;

/**
 * 实体类对应的字段
 * @author Bob
 */
public class AhField extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String typeName;        // 类型名

    private String fieldName;       // 字段名

    private String ahClassCodeId;   // 实体类ID

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AhField ahField = (AhField) o;
        if (ahField.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahField.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhField{" +
                "id=" + getId() +
                ", typeName='" + getTypeName() + "'" +
                ", fieldName='" + getFieldName() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                ", ahClassCodeId=" + getAhClassCodeId() +
                "}";
    }

    // 数据表名和列名
    public static final String _TableName = "ah_field";
    public static final String _typeName = "type_name";    // 字段类型名
    public static final String _fieldName = "field_name";    // 字段名
    public static final String _ahClassCodeId = "ah_class_code_id";    // 类ID

}
