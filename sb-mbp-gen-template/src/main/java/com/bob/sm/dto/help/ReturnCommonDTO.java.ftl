package ${packageName}.dto.help;

import ${packageName}.config.Constants;

/**
 * 数据返回的共通类
 * @author Bob
 */
public class ReturnCommonDTO<T> {

    // 返回状态
    private String resultCode;

    // 返回消息
    private String errMsg;

    // 返回数据
    private T data;

    public ReturnCommonDTO() {
        this(Constants.commonReturnStatus.SUCCESS.getValue());
    }

    public ReturnCommonDTO(String resultCode) {
        this.resultCode = resultCode;
    }

    public ReturnCommonDTO(String resultCode, String errMsg) {
        this(resultCode);
        this.errMsg = errMsg;
    }

    public ReturnCommonDTO(String resultCode, String errMsg, T data) {
        this(resultCode, errMsg);
        this.data = data;
    }

    public ReturnCommonDTO(T data) {
        this(Constants.commonReturnStatus.SUCCESS.getValue(), null, data);
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
