package com.bob.at.dto.adapter;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 前端接口适配器条件查询DTO
 * @author Bob
 */
public class ApiAdapterCriteriaDTO {

    @JSONField(ordinal = 1)
    private String fromParam;               // 来源参数名
    @JSONField(ordinal = 2)
    private String descr;                   // 参数描述
    @JSONField(ordinal = 3)
    private List<String> toCriteriaList;    // 转换后的条件参数
    @JSONField(ordinal = 4)
    private Object fixedValue;              // 固定值
    @JSONField(ordinal = 5)
    private Boolean fixedValueIsDigit;      // 固定值是否为数字
    @JSONField(ordinal = 6)
    private Integer emptyToNull;            // 空字符串是否转为null（1或不填：是  2：否）

    public String getFromParam() {
        return fromParam;
    }

    public void setFromParam(String fromParam) {
        this.fromParam = fromParam;
    }

    public List<String> getToCriteriaList() {
        return toCriteriaList;
    }

    public void setToCriteriaList(List<String> toCriteriaList) {
        this.toCriteriaList = toCriteriaList;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        this.fixedValue = fixedValue;
    }

    public Boolean getFixedValueIsDigit() {
        return fixedValueIsDigit;
    }

    public void setFixedValueIsDigit(Boolean fixedValueIsDigit) {
        this.fixedValueIsDigit = fixedValueIsDigit;
    }

    public Integer getEmptyToNull() {
        return emptyToNull;
    }

    public void setEmptyToNull(Integer emptyToNull) {
        this.emptyToNull = emptyToNull;
    }
}
