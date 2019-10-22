package ${packageName}.util;

import ${packageName}.dto.help.ReturnCommonDTO;

/**
 * Controller的异常共通处理
 * @author Bob
 */
@FunctionalInterface
public interface ControllerExceptionHandler<O> {

    ReturnCommonDTO<O> handle(String a);

}
