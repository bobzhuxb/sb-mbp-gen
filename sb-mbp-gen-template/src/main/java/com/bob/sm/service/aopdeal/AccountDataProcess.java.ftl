package ${packageName}.service.aopdeal;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class AccountDataProcess implements BaseDataProcess {

    public Object requestParamToCriteria(HttpServletRequest request, Object criteria) {
        return criteria;
    }

    public ResponseEntity processRetData(HttpServletRequest request, ResponseEntity retVal) {
        return retVal;
    }

}
