package com.bob.sm.util;

import com.bob.sm.config.Constants;
import com.bob.sm.dto.help.ReturnCommonDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ParamValidatorUtil {

    public static ReturnCommonDTO validateFields(BindingResult bindingResult) {
        ReturnCommonDTO returnCommonDTO = new ReturnCommonDTO();
        returnCommonDTO.setErrMsg("");
        bindingResult.getAllErrors().forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            String fieldName = fieldError.getField();
            String errorMsg = fieldError.getDefaultMessage();
            if (errorMsg != null && !"".equals(errorMsg)) {
                returnCommonDTO.setResultCode(Constants.commonReturnStatus.FAIL.getValue());
                returnCommonDTO.setErrMsg(returnCommonDTO.getErrMsg() + fieldName + "："  + errorMsg + "。");
            }
        });
        return returnCommonDTO;
    }

}
