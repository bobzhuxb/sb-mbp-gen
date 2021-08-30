package com.bob.at.dto.adapter;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 前端接口适配器返回结果DTO
 * @author Bob
 */
public class ApiAdapterResultDTO {

    @JSONField(ordinal = 1)
    private String resultCode;                          // 返回码说明
    @JSONField(ordinal = 2)
    private String errMsg;                              // 错误消息
    @JSONField(ordinal = 3)
    private String data;                                // data说明
    @JSONField(ordinal = 4)
    private List<ApiAdapterResultFieldDTO> fieldList;   // 返回数据转换的字段配置

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<ApiAdapterResultFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ApiAdapterResultFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
