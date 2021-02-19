package ${packageName}.web.rest.errors;

import ${packageName}.config.Constants;

/**
 * 共通异常类（运行时异常）
 * @author Bob
 */
public class CommonException extends RuntimeException {

    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误明细，用于记录日志
     */
    private String errDetail;

    // 另：继承的message字段用于返回给前端提示给用户错误信息

    public CommonException(String message) {
        super(message);
        this.code = Constants.commonReturnStatus.FAIL.getValue();
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static CommonException errWithDetail(String message, String errDetail) {
        CommonException commonException = new CommonException(message);
        commonException.setErrDetail(errDetail);
        return commonException;
    }
    
    public String getCode() {
        return code;
    }

    public String getErrDetail() {
        return errDetail;
    }

    public void setErrDetail(String errDetail) {
        this.errDetail = errDetail;
    }
}
