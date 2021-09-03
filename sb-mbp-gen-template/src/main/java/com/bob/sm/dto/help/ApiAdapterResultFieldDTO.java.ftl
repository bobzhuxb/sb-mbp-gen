package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import java.util.List;

/**
 * 前端接口适配器返回结果字段转换DTO
 * @author Bob
 */
public class ApiAdapterResultFieldDTO {

    @GenComment("转换后的参数名称")
    private String name;

    @GenComment("转换后的参数类型（list：列表  object或不填：Object）")
    private String type;

    @GenComment("转换前的参数名称")
    private String fromName;

    @GenComment("返回参数描述")
    private String descr;

    @GenComment("转换后所在list层级")
    private Integer level;

    @GenComment("本字段包含的子数据")
    private List<ApiAdapterResultFieldDTO> subFieldList;

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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public List<ApiAdapterResultFieldDTO> getSubFieldList() {
        return subFieldList;
    }

    public void setSubFieldList(List<ApiAdapterResultFieldDTO> subFieldList) {
        this.subFieldList = subFieldList;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
