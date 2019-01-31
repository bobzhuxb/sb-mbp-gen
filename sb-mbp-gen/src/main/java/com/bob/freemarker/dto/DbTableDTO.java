package com.bob.freemarker.dto;

import java.util.List;

public class DbTableDTO {

    private String tableName;       // 表名
    private String tableComment;    // 表注释
    private List<DbColumnDTO> columnList;   // 列

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<DbColumnDTO> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<DbColumnDTO> columnList) {
        this.columnList = columnList;
    }
}
