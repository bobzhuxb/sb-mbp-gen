package com.bob.at.dto.criteria;

/**
 * 条件查询：可用实体类
 * @author Bob
 */
public class AhClassCodeCriteria {

    /**
     * 项目ID精确查询
     */
    private String projectIdEq;

    /**
     * 全路径名精确查询查询
     */
    private String fullNameEq;

    public String getProjectIdEq() {
        return projectIdEq;
    }

    public void setProjectIdEq(String projectIdEq) {
        this.projectIdEq = projectIdEq;
    }

    public String getFullNameEq() {
        return fullNameEq;
    }

    public void setFullNameEq(String fullNameEq) {
        this.fullNameEq = fullNameEq;
    }
}
