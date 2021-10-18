package ${packageName}.dto;

import ${packageName}.annotation.GenComment;
import ${packageName}.annotation.RestFieldAllow;

import java.io.Serializable;

/**
 * 基础DTO
 * @author Bob
 */
public class BaseDTO implements Serializable {

    @GenComment("主键ID")
    private String id;

    @GenComment("创建者用户ID")
    @RestFieldAllow(allowSet = false)
    private String insertUserId;

    @GenComment("操作者用户ID")
    @RestFieldAllow(allowSet = false)
    private String operateUserId;

    @GenComment("插入时间")
    @RestFieldAllow(allowSet = false)
    private String insertTime;

    @GenComment("更新时间")
    @RestFieldAllow(allowSet = false)
    private String updateTime;

    ///////////////////////// 附加关联属性 /////////////////////////

    @GenComment("创建者用户")
    @RestFieldAllow(allowSet = false)
    private SystemUserDTO insertUser;

    @GenComment("操作者用户")
    @RestFieldAllow(allowSet = false)
    private SystemUserDTO operateUser;

    ///////////////////////// 其他属性 /////////////////////////

    @GenComment("是否在级联保存或删除（1：是  2或不填：否）")
    @RestFieldAllow(allowSet = false)
    private Integer cascadeSave;

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

    public Integer getCascadeSave() {
        return cascadeSave;
    }

    public void setCascadeSave(Integer cascadeSave) {
        this.cascadeSave = cascadeSave;
    }
}
