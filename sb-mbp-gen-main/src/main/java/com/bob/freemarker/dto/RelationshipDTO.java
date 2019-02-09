package com.bob.freemarker.dto;

import java.util.List;

/**
 * 通用关系
 */
public class RelationshipDTO {

    private String relationType;        // 关系类型（OneToOne、OneToMany）
    private String toFromEntityType;    // 关系from的类型
    private String toFromEntityName;    // 关系from的名称（单数）
    private String toFromEntityUName;   // 关系from的名称（首字母大写）
    private String toFromEntityTable;   // 关系from的表名
    private String toFromEntityUrl;     // 关系from的URL
    private String fromToEntityType;    // 关系to的类型
    private String fromToEntityName;    // 关系to的名称（单数）
    private String fromToEntityUName;   // 关系to的名称（首字母大写）
    private String fromToEntityTable;   // 关系to的表名
    private String fromToEntityUrl;     // 关系to的URL
    private String fromToDeleteType;    // 关系to的删除模式（DELETE：级联删除、NULL：级联置空）
    private String fromColumnName;      // 关系from的数据库列名
    private String fromToComment;       // to在from中的注释
    private String toFromComment;       // from在to中的注释
    private List<String> annotationList;        // DTO的成员变量的注解

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getToFromEntityType() {
        return toFromEntityType;
    }

    public void setToFromEntityType(String toFromEntityType) {
        this.toFromEntityType = toFromEntityType;
    }

    public String getToFromEntityName() {
        return toFromEntityName;
    }

    public void setToFromEntityName(String toFromEntityName) {
        this.toFromEntityName = toFromEntityName;
    }

    public String getToFromEntityUName() {
        return toFromEntityUName;
    }

    public void setToFromEntityUName(String toFromEntityUName) {
        this.toFromEntityUName = toFromEntityUName;
    }

    public String getToFromEntityTable() {
        return toFromEntityTable;
    }

    public void setToFromEntityTable(String toFromEntityTable) {
        this.toFromEntityTable = toFromEntityTable;
    }

    public String getToFromEntityUrl() {
        return toFromEntityUrl;
    }

    public void setToFromEntityUrl(String toFromEntityUrl) {
        this.toFromEntityUrl = toFromEntityUrl;
    }

    public String getFromToEntityType() {
        return fromToEntityType;
    }

    public void setFromToEntityType(String fromToEntityType) {
        this.fromToEntityType = fromToEntityType;
    }

    public String getFromToEntityName() {
        return fromToEntityName;
    }

    public void setFromToEntityName(String fromToEntityName) {
        this.fromToEntityName = fromToEntityName;
    }

    public String getFromToEntityUName() {
        return fromToEntityUName;
    }

    public void setFromToEntityUName(String fromToEntityUName) {
        this.fromToEntityUName = fromToEntityUName;
    }

    public String getFromToEntityTable() {
        return fromToEntityTable;
    }

    public void setFromToEntityTable(String fromToEntityTable) {
        this.fromToEntityTable = fromToEntityTable;
    }

    public String getFromToEntityUrl() {
        return fromToEntityUrl;
    }

    public void setFromToEntityUrl(String fromToEntityUrl) {
        this.fromToEntityUrl = fromToEntityUrl;
    }

    public String getFromToDeleteType() {
        return fromToDeleteType;
    }

    public void setFromToDeleteType(String fromToDeleteType) {
        this.fromToDeleteType = fromToDeleteType;
    }

    public String getFromColumnName() {
        return fromColumnName;
    }

    public void setFromColumnName(String fromColumnName) {
        this.fromColumnName = fromColumnName;
    }

    public String getFromToComment() {
        return fromToComment;
    }

    public void setFromToComment(String fromToComment) {
        this.fromToComment = fromToComment;
    }

    public String getToFromComment() {
        return toFromComment;
    }

    public void setToFromComment(String toFromComment) {
        this.toFromComment = toFromComment;
    }

    public List<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<String> annotationList) {
        this.annotationList = annotationList;
    }
}
