package com.bob.sm.service.aopdeal;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class SfstDishDataProcess implements BaseDataProcess {

    public Object requestParamToCriteria(HttpServletRequest request, Object criteria) {
        return criteria;
    }

    public ResponseEntity processRetData(ResponseEntity retVal) {
        return retVal;
    }

}
