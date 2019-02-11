package com.bob.sm.web.rest.errors;

import com.bob.sm.config.Constants;

public class CommonException extends RuntimeException {

    private String code;

    public CommonException(String message) {
        super(message);
        this.code = Constants.commonReturnStatus.FAIL.getValue();
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }

}