package ${packageName}.dto.help;

import ${packageName}.config.Constants;

public class ReturnCommonDTO {

    // 返回状态
    private String resultCode;

    // 返回消息
    private String errMsg;

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
}
