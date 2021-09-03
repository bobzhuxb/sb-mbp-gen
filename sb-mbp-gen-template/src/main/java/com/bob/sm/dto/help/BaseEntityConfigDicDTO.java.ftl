package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 用于共通化Service代码的实体配置DTO
 * @author Bob
 */
public class BaseEntityConfigDicDTO {

    @GenComment("域名")
    private String fieldName;

    @GenComment("值的域名")
    private String fieldValueName;

    @GenComment("数据表中的列名")
    private String columnName;

    @GenComment("值的列名")
    private String columnValueName;

    @GenComment("数据字典中的Type标识")
    private String dicType;

    @GenComment("数据字典类别名称")
    private String dicTypeName;

    public BaseEntityConfigDicDTO() {

    }

    public BaseEntityConfigDicDTO(String fieldName, String fieldValueName, String columnName, String columnValueName,
                                  String dicType, String dicTypeName) {
        this.fieldName = fieldName;
        this.fieldValueName = fieldValueName;
        this.columnName = columnName;
        this.columnValueName = columnValueName;
        this.dicType = dicType;
        this.dicTypeName = dicTypeName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValueName() {
        return fieldValueName;
    }

    public void setFieldValueName(String fieldValueName) {
        this.fieldValueName = fieldValueName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValueName() {
        return columnValueName;
    }

    public void setColumnValueName(String columnValueName) {
        this.columnValueName = columnValueName;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicTypeName() {
        return dicTypeName;
    }

    public void setDicTypeName(String dicTypeName) {
        this.dicTypeName = dicTypeName;
    }
}
