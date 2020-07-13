package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.config.Constants;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.*;
import ${packageName}.mapper.*;
<#if eentityName == 'SystemPermission'>
import ${packageName}.security.SecurityUtils;
</#if>
import ${packageName}.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ${entityComment}
 * @author Bob
 */
@Service
public class ${eentityName}ServiceImpl extends ServiceImpl<${eentityName}Mapper, ${eentityName}>
        implements ${eentityName}Service {

    private final Logger log = LoggerFactory.getLogger(${eentityName}ServiceImpl.class);

    /**
     * 获取日志
     * @return
     */
    @Override
    public Logger getLog() {
        return log;
    }

}