package ${packageName}.service;

import javax.servlet.http.HttpServletRequest;

/**
 * API适配器，动态修改相关参数和返回
 * @author Bob
 */
public interface ApiAdapterService {

    void initApiAdapter();

    void initApiDocBase();

    void processQueryParam(HttpServletRequest request, Object[] parameters);

    void processReturn(HttpServletRequest request, Object[] parameters, Object retVal);

}
