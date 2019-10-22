package ${packageName}.config;

import ${packageName}.security.permission.CustomMethodSecurityExpressionHandler;
import ${packageName}.security.permission.CustomPermissionEvaluator;
import ${packageName}.service.SystemPermissionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 方法安全配置
 * @author Bob
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private SystemPermissionService ps;

    public MethodSecurityConfig(SystemPermissionService ps) {
        this.ps = ps;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler expressionHandler =
            new CustomMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(ps));
        return expressionHandler;
    }


}
