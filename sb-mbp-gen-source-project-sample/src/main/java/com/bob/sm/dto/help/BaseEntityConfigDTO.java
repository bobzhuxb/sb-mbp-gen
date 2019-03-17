package com.bob.sm.dto.help;

import java.util.List;

/**
 * 用于共通化Service代码的实体配置DTO
 */
public class BaseEntityConfigDTO {

    private String tableName;       // 当前实体对应的表名

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
