package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 普通查询条件DTO
 * @author Bob
 */
public class NormalCriteriaDTO {

    @GenComment("表名的别名")
    private String tableName;

    @GenComment("Criteria的成员变量名")
    private String fieldName;

    @GenComment("参数值")
    private Object value;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NormalCriteriaDTO{" +
                "tableName='" + tableName + "\'" +
                ", fieldName='" + fieldName + "\'" +
                ", value=" + value +
                '}';
    }
}
