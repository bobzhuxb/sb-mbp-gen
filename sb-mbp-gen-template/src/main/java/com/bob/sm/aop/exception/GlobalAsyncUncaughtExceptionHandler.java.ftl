package ${packageName}.aop.exception;

import com.ts.bpoi.error.BpoiAlertException;
import ${packageName}.web.rest.errors.BadRequestAlertException;
import ${packageName}.web.rest.errors.CommonAlertException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.security.core.AuthenticationException;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;

/**
 * 用于替代Spring的SimpleAsyncUncaughtExceptionHandler，实现自己的异常处理
 * @author Bob
 */
public class GlobalAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Log logger = LogFactory.getLog(GlobalAsyncUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof AuthenticationException || ex instanceof NoSuchElementException
                || ex instanceof BadRequestAlertException || ex instanceof CommonAlertException
                || ex instanceof BpoiAlertException) {
            // Session过期或无效、没有对应的数据、请求提示错误、业务提示错误
            logger.warn("Unexpected Exception occurred invoking async method: " + method, ex);
        } else {
            // 其他错误（业务异常、其他异常）
            logger.error("Unexpected Exception occurred invoking async method: " + method, ex);
        }
    }

}
