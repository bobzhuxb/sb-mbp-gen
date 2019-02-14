package com.bob.sm.dto;

import com.bob.sm.annotation.RestFieldAllow;

import java.io.Serializable;

public class BaseDTO implements Serializable {

    @RestFieldAllow(allowSet = false)
    private Long insertUserId;    // 创建者用户ID

    @RestFieldAllow(allowSet = false)
    private Long operateUserId;    // 操作者用户ID

    @RestFieldAllow(allowSet = false)
    private String insertTime;    // 插入时间

    @RestFieldAllow(allowSet = false)
    private String updateTime;    // 更新时间

    ///////////////////////// 附加关联属性 /////////////////////////

    private SystemUserDTO insertUser;    // 创建者用户

    private SystemUserDTO operateUser;    // 操作者用户

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
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
