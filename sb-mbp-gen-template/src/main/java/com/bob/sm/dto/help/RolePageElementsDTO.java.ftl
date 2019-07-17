package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RolePageElementsDTO {

    private String roleId;               // 角色ID

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 角色标识

    @NotBlank
    @Size(min = 1, max = 255)
    private String chineseName;    // 角色中文名称

    @Size(max = 255)
    private String description;    // 角色描述

    private List<String> pageElementCodeList;   // 页面或页面元素的代码列表

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPageElementCodeList() {
        return pageElementCodeList;
    }

    public void setPageElementCodeList(List<String> pageElementCodeList) {
        this.pageElementCodeList = pageElementCodeList;
    }
}
