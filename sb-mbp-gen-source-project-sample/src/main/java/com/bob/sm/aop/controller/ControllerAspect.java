package com.bob.sm.aop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.sm.annotation.CreateInitValue;
import com.bob.sm.annotation.CreateTime;
import com.bob.sm.annotation.UpdateTime;
import com.bob.sm.domain.SystemUser;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.mapper.SystemUserMapper;
import com.bob.sm.security.SecurityUtils;
import com.bob.sm.service.aopdeal.BaseDataProcess;
import com.bob.sm.util.MyBeanUtil;
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
    private SystemUserMapper systemUserMapper;

    private BaseDataProcess baseDataProcess;

//    @Pointcut("execution(* com.bob.sm.web.rest.*Controller.*(..))")
//    public void pointCutAll(){}

    @Pointcut("execution(* com.bob.sm.web.rest.*Controller.get*(..))")
    public void pointCutGet(){}

    @Pointcut("execution(* com.bob.sm.web.rest.*Controller.create*(..))")
    public void pointCutCreate(){}

    @Pointcut("execution(* com.bob.sm.web.rest.*Controller.update*(..))")
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
        // 调用方法所在的类
        String classType = pjp.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzSimpleName = clazz.getSimpleName();
        if (clazzSimpleName.endsWith("Controller")) {
            String domainName = clazzSimpleName.substring(0, clazzSimpleName.length() - 10);
            Class dpClass = Class.forName("com.bob.sm.service.aopdeal." + domainName + "DataProcess");
            baseDataProcess = (BaseDataProcess) dpClass.getConstructor().newInstance();
        }
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        // 查询参数统一预处理
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof BaseCriteria && baseDataProcess != null) {
                parameters[i] = baseDataProcess.requestParamToCriteria(request, parameters[i]);
                break;
            }
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
        // 返回结果的统一处理
        if (retVal instanceof ResponseEntity && baseDataProcess != null) {
            retVal = baseDataProcess.processRetData((ResponseEntity)retVal);
        }
        log.debug("AOP Aronud controller get after...");

        return retVal;
    }

    @Around("pointCutCreate()")
    public Object createAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller add before...");
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 获取当前操作用户
        Long currentUserId = null;
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (currentUserLogin != null) {
            SystemUser systemUser = systemUserMapper.selectOne(new QueryWrapper<SystemUser>().eq("login", currentUserLogin));
            if (systemUser != null) {
                currentUserId = systemUser.getId();
            }
        }
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setFieldValueByAnnotationName(CreateTime.class, nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("insertTime", nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUserId", currentUserId, parameter);
            MyBeanUtil.setFieldValueByFieldName("deleteFlag", 0, parameter);
            MyBeanUtil.setFieldValueByAnnotationNameAttr(CreateInitValue.class, "value", null, parameter);
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
        log.debug("AOP Aronud controller add after...");

        return retVal;
    }

    @Around("pointCutUpdate()")
    public Object updateAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("AOP Aronud controller update before...");
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 获取当前操作用户
        Long currentUserId = null;
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (currentUserLogin != null) {
            SystemUser systemUser = systemUserMapper.selectOne(new QueryWrapper<SystemUser>().eq("login", currentUserLogin));
            if (systemUser != null) {
                currentUserId = systemUser.getId();
            }
        }
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setFieldValueByAnnotationName(UpdateTime.class, nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("updateTime", nowTimeStr, parameter);
            MyBeanUtil.setFieldValueByFieldName("operateUserId", currentUserId, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
        log.debug("AOP Aronud controller update after...");

        return retVal;
    }

}
