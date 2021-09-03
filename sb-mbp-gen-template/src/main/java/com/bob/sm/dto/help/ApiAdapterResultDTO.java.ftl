package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import java.util.List;

/**
 * 前端接口适配器返回结果DTO
 * @author Bob
 */
public class ApiAdapterResultDTO {

    @GenComment("返回码说明")
    private String resultCode;

    @GenComment("错误消息")
    private String errMsg;

    @GenComment("data说明")
    private String data;

    @GenComment("返回数据转换的字段配置")
    private List<ApiAdapterResultFieldDTO> fieldList;

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
