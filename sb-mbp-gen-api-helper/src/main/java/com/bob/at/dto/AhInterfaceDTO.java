package com.bob.at.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 接口
 * @author Bob
 */
public class AhInterfaceDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1)
    private String interNo;    // 接口号

    @NotBlank
    @Size(min = 1)
    private String httpUrl;    // URL地址

    @NotBlank
    @Size(min = 1)
    private String httpMethod;    // Http方法

    @NotBlank
    @Size(min = 1)
    private String addDefaultPrefix;    // 是否有默认前缀

    @NotBlank
    @Size(min = 1)
    private String interDescr;    // 描述

    @Size(min = 1)
    private String returnTypeName;    // 返回类型全名

    @Size(min = 1)
    private String dataJson;    // 配置JSON

    @NotBlank
    @Size(min = 1)
    private String ahProjectId;    // 项目ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private AhProjectDTO ahProject;    // 项目

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================
	
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

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
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

    public AhProjectDTO getAhProject() {
        return ahProject;
    }

    public void setAhProject(AhProjectDTO ahProject) {
        this.ahProject = ahProject;
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

        AhInterfaceDTO ahInterfaceDTO = (AhInterfaceDTO) o;
        if (ahInterfaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahInterfaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhInterfaceDTO{" +
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
}
