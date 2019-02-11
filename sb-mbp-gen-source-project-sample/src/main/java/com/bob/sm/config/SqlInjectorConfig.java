package com.bob.sm.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.bob.sm.config.mphelp.QuerySupportMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SqlInjectorConfig extends DefaultSqlInjector {

    @Resource
    private QuerySupportMethod querySupportMethod;

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();
        methodList.add(querySupportMethod);
        return methodList;
    }
}
