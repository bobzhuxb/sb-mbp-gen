package com.bob.at.domain;

import java.util.Objects;

/**
 * 项目
 * @author Bob
 */
public class AhProject extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String name;    // 名称

    private String descr;    // 描述

    private String urlPrefix;    // URL前缀

    private String basePackage;     // 基础包名

    private String yapiToken;     // Yapi的Token

    private String yapiUrl;     // Yapi的URL

    private String exceptClassNames;     // 导入排除的实体类

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getYapiToken() {
        return yapiToken;
    }

    public void setYapiToken(String yapiToken) {
        this.yapiToken = yapiToken;
    }

    public String getYapiUrl() {
        return yapiUrl;
    }

    public void setYapiUrl(String yapiUrl) {
        this.yapiUrl = yapiUrl;
    }

    public String getExceptClassNames() {
        return exceptClassNames;
    }

    public void setExceptClassNames(String exceptClassNames) {
        this.exceptClassNames = exceptClassNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AhProject ahProject = (AhProject) o;
        if (ahProject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahProject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhProject{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", descr='" + getDescr() + "'" +
                ", urlPrefix='" + getUrlPrefix() + "'" +
                ", basePackage='" + getBasePackage() + "'" +
                ", yapiToken='" + getYapiToken() + "'" +
                ", yapiUrl='" + getYapiUrl() + "'" +
                ", exceptClassNames='" + getExceptClassNames() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", exceptClassNames='" + getExceptClassNames() + "'" +
                "}";
    }

    // 数据表名和列名
    public static final String _TableName = "ah_project";
    public static final String _name = "name";    // 名称
    public static final String _descr = "descr";    // 描述
    public static final String _urlPrefix = "url_prefix";    // URL前缀
    public static final String _basePackage = "base_package";    // 基础包名
    public static final String _yapiToken = "yapi_token";    // Yapi的Token
    public static final String _yapiUrl = "yapi_url";    // Yapi的URL
    public static final String _exceptClassNames = "except_class_names";    // 导入排除的实体类

}
