package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import com.bob.sm.config.Constants;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 用户
 */
public class SystemUserDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String login;    // 登录账号

    @RestFieldAllow(allowGet = false)
    private String password;    // 密码

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 姓名

    @ValidCell
    private String cell;    // 手机号

    @Size(max = 255)
    private String contractInfo;    // 其他联系方式

    @ValidCid
    private String identifyNo;    // 身份证号

    @Size(max = 255)
    private String email;    // 邮箱

    @Size(max = 255)
    private String imgRelativePath;    // 头像图片相对路径

    @Size(max = 255)
    private String memo;    // 备注

    private Long insertUserId;    // 创建者用户ID

    private Long systemOrganizationId;    // 组织机构ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SystemOrganizationDTO systemOrganization;    // 组织机构

    private List<SystemUserRoleDTO> systemUserRoleList;    // 角色列表

    private List<SystemUserResourceDTO> systemUserResourceList;    // 资源列表

    // ================self code:自定义属性start=====================
    // ================self code:自定义属性end=====================

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

    public SystemOrganizationDTO getSystemOrganization() {
        return systemOrganization;
    }

    public void setSystemOrganization(SystemOrganizationDTO systemOrganization) {
        this.systemOrganization = systemOrganization;
    }

    public List<SystemUserRoleDTO> getSystemUserRoleList() {
        return systemUserRoleList;
    }

    public void setSystemUserRoleList(List<SystemUserRoleDTO> systemUserRoleList) {
        this.systemUserRoleList = systemUserRoleList;
    }

    public List<SystemUserResourceDTO> getSystemUserResourceList() {
        return systemUserResourceList;
    }

    public void setSystemUserResourceList(List<SystemUserResourceDTO> systemUserResourceList) {
        this.systemUserResourceList = systemUserResourceList;
    }

    // ================self code:自定义属性的get/set方法start=====================
    // ================self code:自定义属性的get/set方法end=====================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemUserDTO systemUserDTO = (SystemUserDTO) o;
        if (systemUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemUserDTO{" +
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
