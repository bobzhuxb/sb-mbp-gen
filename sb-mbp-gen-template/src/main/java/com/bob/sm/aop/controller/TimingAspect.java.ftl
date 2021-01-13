package ${packageName}.aop.controller;

import ${packageName}.config.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 计时拦截器
 * @author Bob
 */
@Component
@Aspect
@Order(2)
public class TimingAspect {

    private final Logger log = LoggerFactory.getLogger(TimingAspect.class);

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.*(..))")
    public void pointCutAll(){}

    @Around("pointCutAll()")
    public Object allAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Around timing before...");
        // 当前时间毫秒
        long startMillis = System.currentTimeMillis();
        // 获取request参数
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 设置请求到达时间
        request.setAttribute(Constants.REQUEST_ATTR_START_TIME, startMillis);
        // 方法参数
        Object[] parameters = pjp.getArgs();
        Object retVal = pjp.proceed(parameters);
        // 当前时间毫秒
        long endMillis = System.currentTimeMillis();
        log.debug("AOP Around timing after... 总耗时" + (endMillis - startMillis) + "毫秒");
        return retVal;
    }

}
