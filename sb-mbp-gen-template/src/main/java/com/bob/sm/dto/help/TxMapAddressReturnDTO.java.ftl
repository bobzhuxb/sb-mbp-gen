package ${packageName}.dto.help;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapAddressReturnDTO {

    private Integer status;             // 状态码

    private String message;             // 状态说明

    @JSONField(name = "request_id")
    private String requestId;           // 本次请求的唯一标识

    private TxMapAddressDTO result;     // 返回结果

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
