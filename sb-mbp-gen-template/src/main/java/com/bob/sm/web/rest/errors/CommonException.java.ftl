package ${packageName}.web.rest.errors;

import ${packageName}.config.Constants;

/**
 * 共通异常类
 * @author Bob
 */
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
