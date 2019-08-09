package com.bob.sm.config;

import com.bob.sm.service.ApiAdapterService;
import com.bob.sm.service.BaseEntityConfigService;
import com.bob.sm.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class ApplicationInit implements ApplicationRunner {

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private ApiAdapterService apiAdapterService;

    @Autowired
    private BaseEntityConfigService baseEntityConfigService;

    @Autowired
    private WxService wxService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initApiAdapter();
        initEntityConfig();
        if ("open".equals(ymlConfig.getWxAccessTokenSwitch())) {
            initWxAccessToken();
        }
    }

    /**
     * 初始化前端接口适配器和API文档
     */
    private void initApiAdapter() {
        apiAdapterService.initApiAdapter();
        apiAdapterService.initApiDocBase();
    }

    /**
     * 初始化实体相关配置（用于共用Service方法）
     */
    private void initEntityConfig() {
        baseEntityConfigService.initEntitiesConfig();
    }

    /**
     * 初始化微信的ACCESS_TOKEN
     */
    private void initWxAccessToken() {
        wxService.refreshAccessToken(10);
    }

}
