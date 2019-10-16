package com.bob.sm.util;

import com.bob.sm.dto.help.ReturnCommonDTO;

/**
 * Controller的异常共通处理
 */
@FunctionalInterface
public interface ControllerExceptionHandler<O> {

    ReturnCommonDTO<O> handle(String a);

}
