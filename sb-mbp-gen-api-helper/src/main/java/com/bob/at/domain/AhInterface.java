package com.bob.at.domain;

import java.util.Objects;

/**
 * 接口
 * @author Bob
 */
public class AhInterface extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String interNo;    // 接口号

    private String httpUrl;    // URL地址

    private String httpMethod;    // Http方法

    private String addDefaultPrefix;    // 是否有默认前缀

    private String interDescr;    // 描述

    private String dataJson;    // 配置JSON

    private String ahProjectId;    // 项目ID

    public String getInterNo() {
        return interNo;
    }

    public void setInterNo(String interNo) {
        this.interNo = interNo;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getAddDefaultPrefix() {
        return addDefaultPrefix;
    }

    public void setAddDefaultPrefix(String addDefaultPrefix) {
        this.addDefaultPrefix = addDefaultPrefix;
    }

    public String getInterDescr() {
        return interDescr;
    }

    public void setInterDescr(String interDescr) {
        this.interDescr = interDescr;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getAhProjectId() {
        return ahProjectId;
    }

    public void setAhProjectId(String ahProjectId) {
        this.ahProjectId = ahProjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AhInterface ahInterface = (AhInterface) o;
        if (ahInterface.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahInterface.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhInterface{" +
                "id=" + getId() +
                ", interNo='" + getInterNo() + "'" +
                ", httpUrl='" + getHttpUrl() + "'" +
                ", httpMethod='" + getHttpMethod() + "'" +
                ", addDefaultPrefix='" + getAddDefaultPrefix() + "'" +
                ", interDescr='" + getInterDescr() + "'" +
                ", dataJson='" + getDataJson() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                ", ahProjectId=" + getAhProjectId() +
                "}";
    }

    // 数据表名和列名
    public static final String _TableName = "ah_interface";
    public static final String _interNo = "inter_no";    // 接口号
    public static final String _httpUrl = "http_url";    // URL地址
    public static final String _httpMethod = "http_method";    // Http方法
    public static final String _addDefaultPrefix = "add_default_prefix";    // 是否有默认前缀
    public static final String _interDescr = "inter_descr";    // 描述
    public static final String _dataJson = "data_json";    // 配置JSON
    public static final String _ahProjectId = "ah_project_id";    // 项目ID

}
