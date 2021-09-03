package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 地图关键字搜索
 * @author Bob
 */
public class ParamMapKeywordSearchDTO {

    @GenComment("关键字")
    @NotBlank
    @Size(min = 1, max = 1000)
    private String keyword;

    @GenComment("搜索范围（例如：苏州）")
    @Size(max = 255)
    private String region;

    @GenComment("每页条数")
    @Max(20)
    private Integer size = 10;

    @GenComment("页数")
    @Min(1)
    private Integer current = 1;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
