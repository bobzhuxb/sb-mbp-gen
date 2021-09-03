package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapSearchReturnDTO {

    @GenComment("状态码")
    private Integer status;

    @GenComment("状态说明")
    private String message;

    @GenComment("本次请求的唯一标识")
    @JSONField(name = "request_id")
    private String requestId;

    @GenComment("结果总数")
    private Integer count;

    @GenComment("返回结果")
    private List<TxMapSearchDataDTO> data;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<TxMapSearchDataDTO> getData() {
        return data;
    }

    public void setData(List<TxMapSearchDataDTO> data) {
        this.data = data;
    }
}
