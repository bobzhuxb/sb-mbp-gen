package com.bob.freemarker.dto;

public class DbColumnDescrDTO {

    private String columnName;          // 列名

    private String columnType;          // 列类型

    private String isNullable;          // 列是否可为空

    private String columnDefault;       // 列默认值

    private String columnComment;       // 列注释

    public DbColumnDescrDTO() {
    }

    public DbColumnDescrDTO(String columnName, String columnType, String isNullable, String columnDefault, String columnComment) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.isNullable = isNullable;
        this.columnDefault = columnDefault;
        this.columnComment = columnComment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
