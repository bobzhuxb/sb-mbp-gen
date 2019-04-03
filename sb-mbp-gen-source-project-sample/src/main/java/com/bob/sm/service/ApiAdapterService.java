package com.bob.sm.service;

import javax.servlet.http.HttpServletRequest;

public interface ApiAdapterService {

    void initApiAdapter();

    void initApiDocBase();

    void processQueryParam(HttpServletRequest request, Object[] parameters);

    void processReturn(HttpServletRequest request, Object[] parameters, Object retVal);

}
