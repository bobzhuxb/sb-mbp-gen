package com.bob.at.domain;

import com.baomidou.mybatisplus.annotation.TableField;

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
                ", insertTime='" + getInsertTime() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                "}";
    }

    // 数据表名和列名
    public static final String _TableName = "ah_project";
    public static final String _name = "name";    // 名称
    public static final String _descr = "descr";    // 描述
    public static final String _urlPrefix = "url_prefix";    // URL前缀

}
