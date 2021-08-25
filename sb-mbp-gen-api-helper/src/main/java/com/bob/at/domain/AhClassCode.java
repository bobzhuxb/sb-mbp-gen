package com.bob.at.domain;

import java.util.Objects;

/**
 * 可用实体类
 * @author Bob
 */
public class AhClassCode extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String packageName;    // 包名

    private String className;    // 类名

    private String ahProjectId;    // 项目ID

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AhClassCode ahClassCode = (AhClassCode) o;
        if (ahClassCode.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ahClassCode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AhClassCode{" +
                "id=" + getId() +
                ", packageName='" + getPackageName() + "'" +
                ", className='" + getClassName() + "'" +
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                ", ahProjectId=" + getAhProjectId() +
                "}";
    }

    // 数据表名和列名
    public static final String _TableName = "ah_class_code";
    public static final String _packageName = "package_name";    // 包名
    public static final String _className = "class_name";    // 类名
    public static final String _ahProjectId = "ah_project_id";    // 项目ID

}
