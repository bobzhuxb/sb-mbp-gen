package ${packageName}.aop.controller;

import ${packageName}.annotation.CreateInitValue;
import ${packageName}.annotation.CreateTime;
import ${packageName}.annotation.UpdateTime;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.service.ApiAdapterService;
import ${packageName}.service.CommonUserService;
import ${packageName}.util.MyBeanUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
@Order(2)
public class ControllerAspect {

    private final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private CommonUserService commonUserService;

    @Autowired
    private ApiAdapterService apiAdapterService;

//    @Pointcut("execution(* ${packageName}.web.rest.*Controller.*(..))")
//    public void pointCutAll(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.get*(..))")
    public void pointCutGet(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.create*(..))")
    public void pointCutCreate(){}

    @Pointcut("execution(* ${packageName}.web.rest.*Controller.update*(..))")
    public void pointCutUpdate(){}

    @Around("pointCutGet()")
    public Object getAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller get before...");
        // 获取request参数，拦截并解析成Criteria查询条件，解析不了的继续传下去
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        // 处理查询条件
        apiAdapterService.processQueryParam(request, parameters);
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
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
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 获取当前操作用户
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        Long currentUserId = systemUserDTO == null ? null : systemUserDTO.getId();
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setFieldValueByAnnotationName(CreateTime.class, nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("insertTime", nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("updateTime", nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("insertUserId", currentUserId, parameter);
            MyBeanUtil.setFieldValueByFieldName("insertUser", systemUserDTO, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUserId", currentUserId, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUser", systemUserDTO, parameter);
            MyBeanUtil.setFieldValueByAnnotationNameAttr(CreateInitValue.class, "value", null, parameter);
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
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
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 获取当前操作用户
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        Long currentUserId = systemUserDTO == null ? null : systemUserDTO.getId();
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setFieldValueByAnnotationName(UpdateTime.class, nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("updateTime", nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUserId", currentUserId, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUser", systemUserDTO, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
        // 处理返回结果
        apiAdapterService.processReturn(request, parameters, retVal);
        
        log.debug("AOP Aronud controller update after...");

        return retVal;
    }

}
