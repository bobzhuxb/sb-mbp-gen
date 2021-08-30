package com.bob.at.dto.adapter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 前端接口适配器返回结果字段转换DTO
 * @author Bob
 */
public class ApiAdapterResultFieldDTO {

    @JSONField(ordinal = 1)
    private String name;        // 转换后的参数名称
    @JSONField(ordinal = 2)
    private String type;        // 转换后的参数类型（list：列表  object或不填：Object）
    @JSONField(ordinal = 3)
    private String fromName;    // 转换前的参数名称
    @JSONField(ordinal = 4)
    private String descr;       // 返回参数描述

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
