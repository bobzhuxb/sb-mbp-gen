package com.bob.sm.dto;

import com.bob.sm.annotation.RestFieldAllow;

import java.io.Serializable;

public class BaseDTO implements Serializable {

    private String id;

    @RestFieldAllow(allowSet = false)
    private String insertUserId;    // 创建者用户ID

    @RestFieldAllow(allowSet = false)
    private String operateUserId;    // 操作者用户ID

    @RestFieldAllow(allowSet = false)
    private String insertTime;    // 插入时间

    @RestFieldAllow(allowSet = false)
    private String updateTime;    // 更新时间

    ///////////////////////// 附加关联属性 /////////////////////////

    @RestFieldAllow(allowSet = false)
    private SystemUserDTO insertUser;

    @RestFieldAllow(allowSet = false)
    private SystemUserDTO operateUser;

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

    public SystemUserDTO getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(SystemUserDTO insertUser) {
        this.insertUser = insertUser;
    }

    public SystemUserDTO getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(SystemUserDTO operateUser) {
        this.operateUser = operateUser;
    }
}
