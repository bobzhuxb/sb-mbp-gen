package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.domain.${eentityName};
import ${packageName}.mapper.${eentityName}Mapper;
import ${packageName}.service.${eentityName}Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
@Transactional
public class ${eentityName}ServiceImpl extends ServiceImpl<${eentityName}Mapper, ${eentityName}>
        implements ${eentityName}Service {

    private final Logger log = LoggerFactory.getLogger(${eentityName}ServiceImpl.class);

}
