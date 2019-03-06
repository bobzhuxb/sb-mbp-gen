package com.bob.sm.service.aopdeal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bob.sm.config.Constants;
import com.bob.sm.dto.help.MbpPage;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.util.MyBeanUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface BaseDataProcess {

    /**
     * 将特殊条件处理成内部条件，简化前端调用方式
     * @param request Http请求
     * @param criteria 待合并的条件
     * @return 合并后的条件
	 */
    default Object requestParamToCriteria(HttpServletRequest request, Object criteria) {
        return criteria;
    }

    /**
     * 处理最终返回的数据
     * @param request Http请求
     * @param retVal 原返回数据
     * @return 处理后的返回数据
	 */
    default ResponseEntity processRetData(HttpServletRequest request, ResponseEntity retVal) {
        return retVal;
    }

    /**
     * 预处理返回数据
     * @param request Http请求
     * @param retVal 原返回数据
     * @return 处理后的返回数据
	 */
    default ResponseEntity preProcessRetData(HttpServletRequest request, ResponseEntity retVal) {
        if (retVal.getStatusCode() == HttpStatus.OK) {
            Object body = retVal.getBody();
            if (body instanceof ReturnCommonDTO
                    && Constants.commonReturnStatus.SUCCESS.getValue().equals(((ReturnCommonDTO) body).getResultCode())) {
                Object retData = ((ReturnCommonDTO) body).getData();
                if (retData instanceof IPage) {
                    MbpPage mbpPage = new MbpPage();
                    MyBeanUtil.copyNonNullProperties(retData, mbpPage);
                    ((ReturnCommonDTO) body).setData(mbpPage);
                }
            }
        }
        return retVal;
    }

}
