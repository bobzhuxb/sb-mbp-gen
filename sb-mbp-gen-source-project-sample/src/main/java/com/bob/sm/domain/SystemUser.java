package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 用户
 */
@Data
@TableName(value = "system_user")
public class SystemUser extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String login;    // 登录账号

    private String password;    // 密码

    private String name;    // 姓名

    private String cell;    // 手机号

    private String contractInfo;    // 其他联系方式

    private String identifyNo;    // 身份证号

    private String email;    // 邮箱

    private String imgRelativePath;    // 头像图片相对路径

    private String memo;    // 备注

    private Long insertUserId;    // 创建者用户ID

    private Long systemOrganizationId;    // 组织机构ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(String contractInfo) {
        this.contractInfo = contractInfo;
    }

    public String getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(String identifyNo) {
        this.identifyNo = identifyNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgRelativePath() {
        return imgRelativePath;
    }

    public void setImgRelativePath(String imgRelativePath) {
        this.imgRelativePath = imgRelativePath;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getSystemOrganizationId() {
        return systemOrganizationId;
    }

    public void setSystemOrganizationId(Long systemOrganizationId) {
        this.systemOrganizationId = systemOrganizationId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SystemUser.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemUser systemUser = (SystemUser) o;
        if (systemUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemUser{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", cell='" + getCell() + "'" +
            ", contractInfo='" + getContractInfo() + "'" +
            ", identifyNo='" + getIdentifyNo() + "'" +
            ", email='" + getEmail() + "'" +
            ", imgRelativePath='" + getImgRelativePath() + "'" +
            ", memo='" + getMemo() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", systemOrganizationId=" + getSystemOrganizationId() +
            "}";
    }
}
