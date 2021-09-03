package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapAddressReturnDTO {

    @GenComment("状态码")
    private Integer status;

    @GenComment("状态说明")
    private String message;

    @GenComment("本次请求的唯一标识")
    @JSONField(name = "request_id")
    private String requestId;

    @GenComment("返回结果")
    private TxMapAddressDTO result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public TxMapAddressDTO getResult() {
        return result;
    }

    public void setResult(TxMapAddressDTO result) {
        this.result = result;
    }
}
