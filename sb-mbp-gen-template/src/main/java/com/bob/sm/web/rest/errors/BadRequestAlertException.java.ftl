package ${packageName}.web.rest.errors;

/**
 * 错误请求异常处理
 * @author Bob
 */
public class BadRequestAlertException extends RuntimeException {

    private String defaultMessage;

    private String entityName;

    private String errorKey;

    public BadRequestAlertException(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this.defaultMessage = defaultMessage;
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

}
