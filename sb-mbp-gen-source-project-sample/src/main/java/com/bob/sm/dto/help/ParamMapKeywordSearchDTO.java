package com.bob.sm.dto.help;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 地图关键字搜索
 */
public class ParamMapKeywordSearchDTO {

    @NotBlank
    @Size(min = 1, max = 1000)
    private String keyword;         // 关键字

    @Size(max = 255)
    private String region;          // 搜索范围（例如：苏州）

    @Max(20)
    private Integer size = 10;      // 每页条数

    @Min(1)
    private Integer current = 1;    // 页数

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
