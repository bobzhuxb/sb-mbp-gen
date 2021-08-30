package com.bob.at.dto.adapter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 前端接口适配器DTO
 * @author Bob
 */
public class ApiAdapterConfigDTO {

    @JSONField(ordinal = 1)
    private String interNo;     // 接口识别号（同一接口类别和URL下唯一）
    @JSONField(ordinal = 2)
    private String httpMethod;  // 接口方法（GET/POST/PUT/DELETE）
    @JSONField(ordinal = 3)
    private String addDefaultPrefix;    // 是否追加默认URL前缀（yes：是  no或不填：否）
    @JSONField(ordinal = 4)
    private String httpUrl;     // 接口URL
    @JSONField(ordinal = 5)
    private String interDescr;  // 接口描述
    @JSONField(ordinal = 6)
    private String returnType;  // 返回类型名
    @JSONField(ordinal = 7)
    private ApiAdapterParamDTO param;       // 接口参数配置
    @JSONField(ordinal = 8)
    private ApiAdapterResultDTO result;     // 接口返回数据配置

    public String getInterNo() {
        return interNo;
    }

    public void setInterNo(String interNo) {
        this.interNo = interNo;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getAddDefaultPrefix() {
        return addDefaultPrefix;
    }

    public void setAddDefaultPrefix(String addDefaultPrefix) {
        this.addDefaultPrefix = addDefaultPrefix;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getInterDescr() {
        return interDescr;
    }

    public void setInterDescr(String interDescr) {
        this.interDescr = interDescr;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public ApiAdapterParamDTO getParam() {
        return param;
    }

    public void setParam(ApiAdapterParamDTO param) {
        this.param = param;
    }

    public ApiAdapterResultDTO getResult() {
        return result;
    }

    public void setResult(ApiAdapterResultDTO result) {
        this.result = result;
    }
}
