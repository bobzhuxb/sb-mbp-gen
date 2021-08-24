package com.bob.at.domain;

import java.io.Serializable;

/**
 * 基础Domain类
 * @author Bob
 */
public class BaseDomain implements Serializable {

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

    // 数据库列名
    public static final String _insertTime = "insert_time";    // 创建时间
    public static final String _updateTime = "update_time";    // 最后更新时间
}
