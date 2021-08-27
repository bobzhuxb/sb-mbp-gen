package com.bob.at.dto;

import java.util.List;
import java.util.Objects;

/**
 * 项目
 * @author Bob
 */
public class AhProjectDTO extends BaseDTO {

    public static void main(String[] args) {

    }

    private String name;    // 名称

    private String descr;    // 描述

    private String urlPrefix;    // URL前缀

    private String basePackage;     // 基础包名
    
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
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                "}";
    }
}
