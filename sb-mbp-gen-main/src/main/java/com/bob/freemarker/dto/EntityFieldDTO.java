package com.bob.freemarker.dto;

/**
 * 通用域（成员变量）
 */
public class EntityFieldDTO {

    private String comment;     // 成员变量注释
    private String javaType;    // 成员变量的Java类型
    private String camelName;   // 首字母小写的驼峰标识名
    private String ccamelName;  // 首字母大写的驼峰标识名

    public EntityFieldDTO(String camelName, String javaType, String comment) {
        this.comment = comment;
        this.javaType = javaType;
        this.camelName = camelName;
        this.ccamelName = camelName.substring(0, 1).toUpperCase() + camelName.substring(1);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getCamelName() {
        return camelName;
    }

    public void setCamelName(String camelName) {
        this.camelName = camelName;
    }

    public String getCcamelName() {
        return ccamelName;
    }

    public void setCcamelName(String ccamelName) {
        this.ccamelName = ccamelName;
    }
}
