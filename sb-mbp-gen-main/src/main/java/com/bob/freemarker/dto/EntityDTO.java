package com.bob.freemarker.dto;

import java.util.List;

/**
 * 通用实体
 */
public class EntityDTO {

    private String entityComment;   // 类注释
    private String eentityName;     // 首字母大写（驼峰标识）的实体名称
    private String entityName;      // 首字母小写（驼峰标识）的实体名称
    private String entityUrl;       // 实体在URL中的名称
    private String tableName;       // 实体对应的数据库表名
    private String lowerName;       // 实体全小写名称（不包括中划线或下划线）
    private List<String> annotationList;        // 实体DTO的注解
    private List<EntityFieldDTO> fieldList;     // 域（成员变量）

    public String getEntityComment() {
        return entityComment;
    }

    public void setEntityComment(String entityComment) {
        this.entityComment = entityComment;
    }

    public String getEentityName() {
        return eentityName;
    }

    public void setEentityName(String eentityName) {
        this.eentityName = eentityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public void setEntityUrl(String entityUrl) {
        this.entityUrl = entityUrl;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLowerName() {
        return lowerName;
    }

    public void setLowerName(String lowerName) {
        this.lowerName = lowerName;
    }

    public List<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<String> annotationList) {
        this.annotationList = annotationList;
    }

    public List<EntityFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<EntityFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
