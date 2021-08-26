package com.bob.at.dto;

import java.util.List;
import java.util.Objects;

/**
 * 可用实体类
 * @author Bob
 */
public class AhClassCodeDTO extends BaseDTO {

    private String packageName;    // 包名

    private String className;    // 类名

    private String ahProjectId;    // 项目ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private AhProjectDTO ahProject;    // 项目

    private List<AhFieldDTO> ahFieldList;    // 字段列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================
	
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
	
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public List<AhFieldDTO> getAhFieldList() {
        return ahFieldList;
    }

    public void setAhFieldList(List<AhFieldDTO> ahFieldList) {
        this.ahFieldList = ahFieldList;
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

        AhClassCodeDTO ahClassCodeDTO = (AhClassCodeDTO) o;
        if (ahClassCodeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahClassCodeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhClassCodeDTO{" +
                "id=" + getId() +
                ", packageName='" + getPackageName() + "'" +
                ", className='" + getClassName() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
			    ", ahProjectId=" + getAhProjectId() +
                "}";
    }
}
