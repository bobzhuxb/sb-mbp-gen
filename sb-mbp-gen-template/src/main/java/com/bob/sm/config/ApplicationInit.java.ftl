package ${packageName}.config;

import ${packageName}.service.ApiAdapterService;
import ${packageName}.service.BaseEntityConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Spring Boot启动后的操作
 * @author Bob
 */
@Component
@Order(value = 1)
public class ApplicationInit implements ApplicationRunner {

    @Autowired
    private ApiAdapterService apiAdapterService;

    @Autowired
    private BaseEntityConfigService baseEntityConfigService;

    @Override
    public void run(ApplicationArguments args) {
        initApiAdapter();
        initEntityConfig();
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

}
