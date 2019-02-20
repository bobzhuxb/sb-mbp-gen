package com.ts.fs.service.aopdeal;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface BaseDataProcess {

    Object requestParamToCriteria(HttpServletRequest request, Object criteria);

    ResponseEntity processRetData(ResponseEntity retVal);

}
