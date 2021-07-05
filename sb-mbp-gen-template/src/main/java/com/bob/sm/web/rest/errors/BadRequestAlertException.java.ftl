package ${packageName}.web.rest.errors;

/**
 * 错误请求异常处理
 * @author Bob
 */
public class BadRequestAlertException extends RuntimeException {

    private String entityName;

    private String errorKey;

    public BadRequestAlertException(String message) {
        super(message);
    }

    public BadRequestAlertException(String message, String entityName, String errorKey) {
        this(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

}
