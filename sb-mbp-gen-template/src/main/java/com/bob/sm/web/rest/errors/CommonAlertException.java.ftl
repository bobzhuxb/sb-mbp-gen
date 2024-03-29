package ${packageName}.web.rest.errors;

import ${packageName}.config.Constants;

/**
 * 提示性的异常（运行时异常）
 * @author Bob
 */
public class CommonAlertException extends CommonException {

    private String code;

    public CommonAlertException(String message) {
        super(message);
        this.code = Constants.commonReturnStatus.FAIL.getValue();
    }

    public CommonAlertException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }

}
