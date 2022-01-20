package com.bob.at.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * 项目
 * @author Bob
 */
public class AhProjectDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1)
    private String name;    // 名称

    @NotBlank
    @Size(min = 1)
    private String descr;    // 描述

    @NotBlank
    @Size(min = 1)
    private String urlPrefix;    // URL前缀

    @NotBlank
    @Size(min = 1)
    private String basePackage;     // 基础包名

    @NotBlank
    @Size(min = 1)
    private String yapiToken;     // Yapi的Token

    @NotBlank
    @Size(min = 1)
    private String yapiUrl;     // Yapi的URL

    private String exceptClassNames;     // 导入排除的实体类
    
	///////////////////////// 附加关联属性 /////////////////////////

    private List<AhInterfaceDTO> ahInterfaceList;    // 接口列表

    private List<AhClassCodeDTO> ahClassCodeList;    // 可用实体类列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================
	
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

    public List<AhInterfaceDTO> getAhInterfaceList() {
        return ahInterfaceList;
    }

    public void setAhInterfaceList(List<AhInterfaceDTO> ahInterfaceList) {
        this.ahInterfaceList = ahInterfaceList;
    }

    public List<AhClassCodeDTO> getAhClassCodeList() {
        return ahClassCodeList;
    }

    public void setAhClassCodeList(List<AhClassCodeDTO> ahClassCodeList) {
        this.ahClassCodeList = ahClassCodeList;
    }
    // ================self code:自定义属性的get/set方法start=====================
    // ================self code:自定义属性的get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AhProjectDTO ahProjectDTO = (AhProjectDTO) o;
        if (ahProjectDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahProjectDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhProjectDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", descr='" + getDescr() + "'" +
                ", urlPrefix='" + getUrlPrefix() + "'" +
                ", basePackage='" + getBasePackage() + "'" +
                ", yapiToken='" + getYapiToken() + "'" +
                ", yapiUrl='" + getYapiUrl() + "'" +
                ", exceptClassNames='" + getExceptClassNames() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                "}";
    }
}
