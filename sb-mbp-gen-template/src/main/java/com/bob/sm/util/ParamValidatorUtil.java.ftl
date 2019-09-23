package ${packageName}.util;

import ${packageName}.config.Constants;
import ${packageName}.dto.help.ReturnCommonDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * 参数验证工具类
 * @author Bob
 */
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
        if (Constants.commonReturnStatus.SUCCESS.equals(returnCommonDTO.getResultCode())) {
            return null;
        }
        return returnCommonDTO;
    }

}
