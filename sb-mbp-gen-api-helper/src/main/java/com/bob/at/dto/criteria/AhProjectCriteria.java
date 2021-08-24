package com.bob.at.dto.criteria;

/**
 * 条件查询：项目
 * @author Bob
 */
public class AhProjectCriteria {

    /**
     * 项目名模糊查询
     */
    private String nameLike;

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }
}
