package ${packageName}.aop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.annotation.CreateInitValue;
import ${packageName}.annotation.CreateTime;
import ${packageName}.annotation.UpdateTime;
import ${packageName}.domain.JhiUser;
import ${packageName}.mapper.JhiUserMapper;
import ${packageName}.security.SecurityUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
@Order(2)
public class ControllerAspect {

    private final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JhiUserMapper jhiUserMapper;

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
        // 获取当前时间
        Date nowDate = new Date();
        String nowDateStr = new SimpleDateFormat("yyyy-MM-dd").format(nowDate);
        String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
        // 连接点方法返回值
        Object retVal = null;
        // 方法参数
        Object[] parameters = pjp.getArgs();
        for (Object parameter : parameters) {
            MyBeanUtil.setAllFieldValue("allowSet", null, parameter);
            MyBeanUtil.setFieldValueByRestFieldAllow("allowSet", null, parameter);
        }
        // 继续执行后续的操作
        retVal = pjp.proceed(parameters);
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
            JhiUser jhiUser = jhiUserMapper.selectOne(new QueryWrapper<JhiUser>().eq("login", currentUserLogin));
            if (jhiUser != null) {
                currentUserId = jhiUser.getId();
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
            JhiUser jhiUser = jhiUserMapper.selectOne(new QueryWrapper<JhiUser>().eq("login", currentUserLogin));
            if (jhiUser != null) {
                currentUserId = jhiUser.getId();
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
