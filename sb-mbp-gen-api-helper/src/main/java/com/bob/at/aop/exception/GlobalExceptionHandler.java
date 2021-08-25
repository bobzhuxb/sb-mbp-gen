package com.bob.at.aop.exception;

import com.alibaba.fastjson.JSONException;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.web.rest.errors.CommonAlertException;
import com.bob.at.web.rest.errors.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
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
     * 没有对应的数据（不记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ReturnCommonDTO handleNoDataFault(NoSuchElementException ex) {
        String message = "不存在的数据：" + ex.getMessage();
        return new ReturnCommonDTO("-997", message);
    }

    /**
     * 拦截JSON解析错误（记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(JSONException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleJsonParseFault(HttpServletRequest request, JSONException ex) {
        String message = "请求错误，请确认JSON格式";
        log.error(message, ex);
        return new ReturnCommonDTO("-995", message);
    }

    /**
     * 拦截HTTP请求内容错误（记录错误日志）
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleHttpMessageNotReadableFault(HttpServletRequest request, HttpMessageNotReadableException ex) {
        String message = "请求错误，请确认HTTP Body的数据";
        log.error(message, ex);
        return new ReturnCommonDTO("-994", message);
    }

    /**
     * 请求的Content-Type不正确
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleContentTypeFault(HttpServletRequest request, HttpMediaTypeNotSupportedException ex) {
        String message = "请求的Content-Type不正确";
        log.error(message, ex);
        return new ReturnCommonDTO("-993", message);
    }

    /**
     * 请求的Content-Type被禁止
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleContentTypeNotAcceptableFault(HttpServletRequest request, HttpMediaTypeNotAcceptableException ex) {
        String message = "请求的Content-Type被禁止";
        log.error(message, ex);
        return new ReturnCommonDTO("-992", message);
    }

    /**
     * 参数转换错误
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnCommonDTO handleBindFault(HttpServletRequest request, BindException ex) {
        String message = "请求参数转换错误";
        log.error(message, ex);
        return new ReturnCommonDTO("-991", message);
    }

    /**
     * 拦截业务错误
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ReturnCommonDTO handleBusinessFault(CommonException ex) {
        String code = ex.getCode();
        String message = ex.getMessage();
        return new ReturnCommonDTO(code, message);
    }

    /**
     * 系统异常 预期以外异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ReturnCommonDTO handleUnexpectedServer(HttpServletRequest request, Exception ex) {
        String message = "系统错误";
        log.error(message, ex);
        return new ReturnCommonDTO("-999", "系统错误");
    }

}
