package com.bob.sm.domain;

import java.io.Serializable;

public class BaseDomain implements Serializable {

    private String id;

    private String insertUserId;    // 创建者用户ID

    private String operateUserId;    // 操作者用户ID

    private String insertTime;    // 插入时间

    private String updateTime;    // 更新时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
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
    public static final String _insertUserId = "insert_user_id";    // 创建者用户ID
    public static final String _operateUserId = "operate_user_id";    // 最后更新者用户ID
    public static final String _insertTime = "insertTime";    // 创建时间
    public static final String _updateTime = "updateTime";    // 最后更新时间
}
