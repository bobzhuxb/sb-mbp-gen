package com.bob.at.dto;

import java.io.Serializable;

/**
 * 基础DTO
 * @author Bob
 */
public class BaseDTO implements Serializable {

    private String id;

    private String insertTime;    // 插入时间

    private String updateTime;    // 更新时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
