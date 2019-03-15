package com.bob.sm.dto.help;

import java.util.List;

/**
 * 前端接口适配器DTO
 */
public class ApiAdapterConfigDTO {

    private String interNo;     // 接口识别号（同一接口类别和URL下唯一）
    private String httpMethod;  // 接口方法（GET/POST/PUT/DELETE）
    private String httpUrl;     // 接口URL
    private String interDescr;  // 接口描述
    private ApiAdapterParamDTO param;       // 接口参数配置
    private ApiAdapterResultDTO result;     // 接口返回数据配置
    private List<String> sqlColumnList;     // SQL查询的字段

    private List<ApiAdapterResultFieldDTO> returnConfigTreeList;    // 将返回配置处理成树状结构的结果

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

    public List<String> getSqlColumnList() {
        return sqlColumnList;
    }

    public void setSqlColumnList(List<String> sqlColumnList) {
        this.sqlColumnList = sqlColumnList;
    }

    public List<ApiAdapterResultFieldDTO> getReturnConfigTreeList() {
        return returnConfigTreeList;
    }

    public void setReturnConfigTreeList(List<ApiAdapterResultFieldDTO> returnConfigTreeList) {
        this.returnConfigTreeList = returnConfigTreeList;
    }
}
