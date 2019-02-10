package com.bob.freemarker.dto;

import com.bob.freemarker.util.StringUtil;

import java.util.List;

/**
 * 通用域（成员变量）
 */
public class EntityFieldDTO {

    private String comment;             // 成员变量注释
    private String javaType;            // 成员变量的Java类型
    private String camelName;           // 首字母小写的驼峰标识名
    private String ccamelName;          // 首字母大写的驼峰标识名
    private String camelNameUnderline;  // 下划线连接的标识名
    private String commentDic;          // 对应的数据字典中文名注释
    private String camelNameDic;        // 对应的数据字典中文标识名（首字母小写的驼峰标识名）
    private String ccamelNameDic;       // 对应的数据字典中文标识名（首字母大写的驼峰标识名）
    private String ccamelNameDicUnderline;      // 对应的数据字典中文标识名（下划线连接）
    private String dictionaryType;      // 对应的数据字典类型
    private List<String> annotationList;        // DTO的成员变量的注解

    public EntityFieldDTO() {}

    public EntityFieldDTO(String camelName, String javaType, String comment, String camelNameDic,
                          String dictionaryType, String commentDic) {
        this(camelName, javaType, comment);
        this.camelNameDic = camelNameDic;
        this.dictionaryType = dictionaryType;
        this.commentDic = commentDic;
        this.ccamelNameDic = camelNameDic.substring(0, 1).toUpperCase() + camelNameDic.substring(1);
        this.ccamelNameDicUnderline = StringUtil.camelToUnderline(camelNameDic);
    }

    public EntityFieldDTO(String camelName, String javaType, String comment) {
        this.comment = comment;
        this.javaType = javaType;
        this.camelName = camelName;
        this.ccamelName = camelName.substring(0, 1).toUpperCase() + camelName.substring(1);
        this.camelNameUnderline = StringUtil.camelToUnderline(camelName);
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

    public String getCamelNameUnderline() {
        return camelNameUnderline;
    }

    public void setCamelNameUnderline(String camelNameUnderline) {
        this.camelNameUnderline = camelNameUnderline;
    }

    public String getCommentDic() {
        return commentDic;
    }

    public void setCommentDic(String commentDic) {
        this.commentDic = commentDic;
    }

    public String getCamelNameDic() {
        return camelNameDic;
    }

    public void setCamelNameDic(String camelNameDic) {
        this.camelNameDic = camelNameDic;
    }

    public String getCcamelNameDic() {
        return ccamelNameDic;
    }

    public void setCcamelNameDic(String ccamelNameDic) {
        this.ccamelNameDic = ccamelNameDic;
    }

    public String getCcamelNameDicUnderline() {
        return ccamelNameDicUnderline;
    }

    public void setCcamelNameDicUnderline(String ccamelNameDicUnderline) {
        this.ccamelNameDicUnderline = ccamelNameDicUnderline;
    }

    public String getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(String dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public List<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<String> annotationList) {
        this.annotationList = annotationList;
    }
}
