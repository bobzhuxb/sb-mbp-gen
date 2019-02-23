package ${packageName}.service.aopdeal;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class ${eentityName}DataProcess implements BaseDataProcess {

    /**
     * 将特殊条件处理成内部条件，简化前端调用方式
     * @param request Http请求
     * @param criteria 待合并的条件
     * @return 合并后的条件
	 */
    public Object requestParamToCriteria(HttpServletRequest request, Object criteria) {
        return criteria;
    }

    /**
     * 处理最终返回的数据
     * @param request Http请求
     * @param retVal 原返回数据
     * @return 处理后的返回数据
	 */
    public ResponseEntity processRetData(HttpServletRequest request, ResponseEntity retVal) {
        return retVal;
    }

}
