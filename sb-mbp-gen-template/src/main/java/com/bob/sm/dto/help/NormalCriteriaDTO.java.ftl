package ${packageName}.dto.help;

public class NormalCriteriaDTO {

    private String tableName;   // 表名的别名

    private String fieldName;   // Criteria的成员变量名

    private Object value;       // 参数值

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
