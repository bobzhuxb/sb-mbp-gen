package ${packageName}.config;

import ${packageName}.dto.SystemPermissionDTO;
import ${packageName}.service.AccountService;
import ${packageName}.service.ApiAdapterService;
import ${packageName}.service.BaseEntityConfigService;
import ${packageName}.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(value = 1)
public class ApplicationInit implements ApplicationRunner {

    @Value("${r'${permission.do-init}'}")
    private String permissionInit;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApiAdapterService apiAdapterService;

    @Autowired
    private BaseEntityConfigService baseEntityConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if ("true".equals(permissionInit)) {
            // 初始化权限表，此步骤仅作为开发环境使用
            try {
                initPermissionData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initApiAdapter();
        initEntityConfig();
    }

    /**
     * 初始化权限表
     */
    private void initPermissionData() {
        List<SystemPermissionDTO> permissionList = PermissionUtil.getAllPermissions(
            Arrays.asList("${packageName}.web.rest"));
        accountService.savePermissionsWithChildren(permissionList);
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
