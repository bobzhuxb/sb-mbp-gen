package com.bob.sm.dto.help;

import com.bob.sm.config.Constants;

public class ReturnUploadCommonDTO<T> extends ReturnCommonDTO<T> {

    // 返回状态
    private String code;

    public ReturnUploadCommonDTO(String code) {
        this.code = String.valueOf(Integer.parseInt(code) - 1);
    }

    public ReturnUploadCommonDTO(String code, String errMsg) {
        this(code);
        super.setErrMsg(errMsg);
    }

    public ReturnUploadCommonDTO(String code, String errMsg, T data) {
        this(code, errMsg);
        super.setData(data);
    }

    public ReturnUploadCommonDTO(T data) {
        this(Constants.commonReturnStatus.SUCCESS.getValue(), null, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
