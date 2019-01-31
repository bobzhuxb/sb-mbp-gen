package ${packageName}.config;

import ${packageName}.dto.help.BasePermissionDTO;
import ${packageName}.service.micro.BasePermissionService;
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
    private BasePermissionService basePermissionService;

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
    }

    /**
     * 初始化权限表
     */
    private void initPermissionData() {
        List<BasePermissionDTO> permissionList = PermissionUtil.getAllPermissions(
            Arrays.asList("${packageName}.web.rest"));
        basePermissionService.savePermissionsWithChildren(permissionList);
    }

}
