package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import java.util.List;

/**
 * 页面元素DTO
 * @author Bob
 */
public class PageElementDTO implements Cloneable {

    @GenComment("页面或页面元素的代码")
    private String code;

    @GenComment("页面或页面元素的名称")
    private String name;

    @GenComment("类型（PAGE：页面  ELEMENT：页面元素）")
    private String type;

    @GenComment("子元素")
    private List<PageElementDTO> childList;

    @GenComment("是否选中（1：是  2或不填：否）")
    private Integer selected;

    public PageElementDTO() {
    }

    public PageElementDTO(String code, String name, String type, List<PageElementDTO> childList) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.childList = childList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PageElementDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<PageElementDTO> childList) {
        this.childList = childList;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Object clone(){
        PageElementDTO ped = null;
        try {
            ped = (PageElementDTO)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ped;
    }
}
