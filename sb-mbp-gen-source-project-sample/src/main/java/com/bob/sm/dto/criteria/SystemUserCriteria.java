package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户 条件过滤器
 */
@ApiModel(description = "用户")
public class SystemUserCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "登录账号")
    private StringFilter login;    // 登录账号

    @ApiModelProperty(value = "密码")
    private StringFilter password;    // 密码

    @ApiModelProperty(value = "姓名")
    private StringFilter name;    // 姓名

    @ApiModelProperty(value = "手机号")
    private StringFilter cell;    // 手机号

    @ApiModelProperty(value = "其他联系方式")
    private StringFilter contractInfo;    // 其他联系方式

    @ApiModelProperty(value = "身份证号")
    private StringFilter identifyNo;    // 身份证号

    @ApiModelProperty(value = "邮箱")
    private StringFilter email;    // 邮箱

    @ApiModelProperty(value = "头像图片相对路径")
    private StringFilter imgRelativePath;    // 头像图片相对路径

    @ApiModelProperty(value = "备注")
    private StringFilter memo;    // 备注

    @ApiModelProperty(value = "组织机构ID")
    private LongFilter systemOrganizationId;    // 组织机构ID

    private SystemOrganizationCriteria systemOrganization;    // 组织机构

    // ================self code:增强的查询条件参数start=====================
    // ================self code:增强的查询条件参数end=====================

    public SystemUserCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }
    
    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }
    
    public StringFilter getCell() {
        return cell;
    }

    public void setCell(StringFilter cell) {
        this.cell = cell;
    }
    
    public StringFilter getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(StringFilter contractInfo) {
        this.contractInfo = contractInfo;
    }
    
    public StringFilter getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(StringFilter identifyNo) {
        this.identifyNo = identifyNo;
    }
    
    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }
    
    public StringFilter getImgRelativePath() {
        return imgRelativePath;
    }

    public void setImgRelativePath(StringFilter imgRelativePath) {
        this.imgRelativePath = imgRelativePath;
    }
    
    public StringFilter getMemo() {
        return memo;
    }

    public void setMemo(StringFilter memo) {
        this.memo = memo;
    }

    public LongFilter getSystemOrganizationId() {
        return systemOrganizationId;
    }
	
	public void setSystemOrganizationId(LongFilter systemOrganizationId) {
        this.systemOrganizationId = systemOrganizationId;
    }

    public SystemOrganizationCriteria getSystemOrganization() {
        return systemOrganization;
    }

    public void setSystemOrganization(SystemOrganizationCriteria systemOrganization) {
        this.systemOrganization = systemOrganization;
    }

    // ================self code:增强的查询条件get/set方法start=====================
    // ================self code:增强的查询条件get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemUserCriteria that = (SystemUserCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(login, that.login)
            && Objects.equals(password, that.password)
            && Objects.equals(name, that.name)
            && Objects.equals(cell, that.cell)
            && Objects.equals(contractInfo, that.contractInfo)
            && Objects.equals(identifyNo, that.identifyNo)
            && Objects.equals(email, that.email)
            && Objects.equals(imgRelativePath, that.imgRelativePath)
            && Objects.equals(memo, that.memo)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , login
        , password
        , name
        , cell
        , contractInfo
        , identifyNo
        , email
        , imgRelativePath
        , memo
        );
    }

    @Override
    public String toString() {
        return "SystemUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (cell != null ? "cell=" + cell + ", " : "") +
                (contractInfo != null ? "contractInfo=" + contractInfo + ", " : "") +
                (identifyNo != null ? "identifyNo=" + identifyNo + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (imgRelativePath != null ? "imgRelativePath=" + imgRelativePath + ", " : "") +
                (memo != null ? "memo=" + memo + ", " : "") +
                "}";
    }

}
