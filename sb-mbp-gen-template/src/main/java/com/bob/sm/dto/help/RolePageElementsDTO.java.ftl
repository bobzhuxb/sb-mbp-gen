package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色与页面关系
 * @author Bob
 */
public class RolePageElementsDTO {

    @GenComment("角色ID")
    private String roleId;

    @GenComment("角色标识")
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @GenComment("角色中文名称")
    @NotBlank
    @Size(min = 1, max = 255)
    private String chineseName;

    @GenComment("角色描述")
    @Size(max = 255)
    private String description;

    @GenComment("页面或页面元素的代码列表")
    private List<String> pageElementCodeList;

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
