package com.bob.freemarker.dto;

/**
 * 通用关系
 */
public class RelationshipDTO {

    private String relationType;        // 关系类型（OneToOne、OneToMany）
    private String toFromEntityType;    // 关系from的类型
    private String toFromEntityName;    // 关系from的名称（单数）
    private String toFromEntityTable;   // 关系from的表名
    private String fromToEntityType;    // 关系to的类型
    private String fromToEntityName;    // 关系to的名称（单数）
    private String fromToEntityTable;   // 关系to的表名
    private String fromColumnName;      // 关系from的数据库列名
    private String fromToComment;       // to在from中的注释
    private String toFromComment;       // from在to中的注释

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

    public String getToFromEntityTable() {
        return toFromEntityTable;
    }

    public void setToFromEntityTable(String toFromEntityTable) {
        this.toFromEntityTable = toFromEntityTable;
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

    public String getFromToEntityTable() {
        return fromToEntityTable;
    }

    public void setFromToEntityTable(String fromToEntityTable) {
        this.fromToEntityTable = fromToEntityTable;
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
}
