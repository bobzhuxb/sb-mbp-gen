package ${packageName}.aop.controller;

import ${packageName}.annotation.CreateInitValue;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.service.ApiAdapterService;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.util.ParamValidatorUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller拦截器
 * @author Bob
 */
@Component
@Aspect
@Order(2)
public class ControllerAspect {

    private final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private ApiAdapterService apiAdapterService;

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.*(..))")
    public void pointCutAll(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.get*(..))")
    public void pointCutGet(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.create*(..))")
    public void pointCutCreate(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.update*(..))")
    public void pointCutUpdate(){}

    @Around("pointCutAll()")
    public Object allAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller before...");
        // 方法参数
        Object[] parameters = pjp.getArgs();
        Object retVal = null;
        for (Object parameter : parameters) {
            // 统一参数验证
            if (parameter instanceof BindingResult) {
                ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields((BindingResult)parameter);
                if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                    retVal = ResponseEntity.ok().headers(null).body(returnCommonDTO);
                }
                break;
            }
        }
        if (retVal == null) {
            // 没有被参数验证拦截返回的，继续执行后续的操作
            retVal = pjp.proceed(parameters);
        }
        log.debug("AOP Aronud controller after...");
        return retVal;
    }

    @Around("pointCutGet()")
    public Object getAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller get before...");
        // 获取request参数，拦截并解析成Criteria查询条件，解析不了的继续传下去
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 处理查询条件
        apiAdapterService.processQueryParam(request, parameters);
        // 继续执行后续的操作
        Object retVal = pjp.proceed(parameters);
        // 处理返回结果
        apiAdapterService.processReturn(request, parameters, retVal);
        
        log.debug("AOP Aronud controller get after...");

        return retVal;
    }

    @Around("pointCutCreate()")
    public Object createAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller add before...");
        // 获取request参数，拦截并解析成Criteria查询条件，解析不了的继续传下去
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setFieldValueByAnnotationNameAttr(CreateInitValue.class, "value", null, parameter);
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        Object retVal = pjp.proceed(parameters);
        // 处理返回结果
        apiAdapterService.processReturn(request, parameters, retVal);
        
        log.debug("AOP Aronud controller add after...");

        return retVal;
    }

    @Around("pointCutUpdate()")
    public Object updateAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller update before...");
        // 获取request参数，拦截并解析成Criteria查询条件，解析不了的继续传下去
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        Object retVal = pjp.proceed(parameters);
        // 处理返回结果
        apiAdapterService.processReturn(request, parameters, retVal);
        
        log.debug("AOP Aronud controller update after...");

        return retVal;
    }

}
