package ${packageName}.dto.help;

import ${packageName}.dto.SystemUserDTO;

/**
 * 用户明细信息
 * @author Bob
 */
public class InnerUserInfoDetailDTO {

    private Boolean bureauRole;             // 是否局端人员

    private Boolean streetRole;             // 是否街道人员

    private SystemUserDTO systemUserDTO;    // 用户信息

    public Boolean getBureauRole() {
        return bureauRole;
    }

    public void setBureauRole(Boolean bureauRole) {
        this.bureauRole = bureauRole;
    }

    public Boolean getStreetRole() {
        return streetRole;
    }

    public void setStreetRole(Boolean streetRole) {
        this.streetRole = streetRole;
    }

    public SystemUserDTO getSystemUserDTO() {
        return systemUserDTO;
    }

    public void setSystemUserDTO(SystemUserDTO systemUserDTO) {
        this.systemUserDTO = systemUserDTO;
    }
}
