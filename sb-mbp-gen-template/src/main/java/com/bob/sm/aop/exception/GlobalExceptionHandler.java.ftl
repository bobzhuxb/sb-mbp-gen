package ${packageName}.aop.exception;

import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.web.rest.errors.BadRequestAlertException;
import ${packageName}.web.rest.errors.CommonAlertException;
import ${packageName}.web.rest.errors.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

/**
 * Controller统一异常处理
 * @author Bob
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截业务异常（Session过期或无效）
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ReturnCommonDTO handleSessionTimeout(AuthenticationException ex) {
        String message = ex.getMessage();
        log.error(ex.getMessage(), ex);
        return new ReturnCommonDTO("-998", message);
    }

    /**
     * 没有对应的数据（不记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ReturnCommonDTO handleNoDataFault(NoSuchElementException ex) {
        String message = ex.getMessage();
        return new ReturnCommonDTO("-997", "不存在的数据：" + message);
    }

    /**
     * 拦截请求错误（不记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(BadRequestAlertException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleRequestFault(BadRequestAlertException ex) {
        String message = ex.getDefaultMessage();
        return new ReturnCommonDTO("-996", message);
    }

    /**
     * 拦截业务错误（不记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonAlertException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReturnCommonDTO handleBusinessFault(CommonAlertException ex) {
        String code = ex.getCode();
        String message = ex.getMessage();
        return new ReturnCommonDTO(code, message);
    }

    /**
     * 拦截业务异常（记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReturnCommonDTO handleBusinessError(CommonException ex) {
        String code = ex.getCode();
        String message = ex.getMessage();
        log.error(ex.getMessage(), ex);
        return new ReturnCommonDTO(code, message);
    }

    /**
     * 系统异常 预期以外异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ReturnCommonDTO handleUnexpectedServer(Exception ex) {
        log.error("系统异常：", ex);
        return new ReturnCommonDTO("-999", "系统错误");
    }

}