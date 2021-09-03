package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import java.util.List;

/**
 * 前端接口适配器DTO
 * @author Bob
 */
public class ApiAdapterConfigDTO {

    @GenComment("接口识别号（同一接口类别和URL下唯一）")
    private String interNo;
    
    @GenComment("接口方法（GET/POST/PUT/DELETE）")
    private String httpMethod;
    
    @GenComment("是否追加默认URL前缀（yes：是  no或不填：否）")
    private String addDefaultPrefix;
    
    @GenComment("接口URL")
    private String httpUrl;
    
    @GenComment("接口描述")
    private String interDescr;
    
    @GenComment("接口参数配置")
    private ApiAdapterParamDTO param;
    
    @GenComment("接口返回数据配置")
    private ApiAdapterResultDTO result;

    @GenComment("将返回配置处理成树状结构的结果")
    private List<ApiAdapterResultFieldDTO> returnConfigTreeList;

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

    public List<ApiAdapterResultFieldDTO> getReturnConfigTreeList() {
        return returnConfigTreeList;
    }

    public void setReturnConfigTreeList(List<ApiAdapterResultFieldDTO> returnConfigTreeList) {
        this.returnConfigTreeList = returnConfigTreeList;
    }
}
