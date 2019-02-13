package com.bob.sm.security.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 动态权限安全拦截器
 */
@Service
public class DynamicSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private static final String[] interceptUrls = {"/api"};

    private static final String[][] exceptUrls = {{null, "/api/authenticate"}, {"GET", "/api/account"}};

    private static final String[][] onlyAdminUrls = {{null, "/api/important-urls"}};

    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager(DynamicAccessDecisionManager dynamicAccessDecisionManager) {
        super.setAccessDecisionManager(dynamicAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }


    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        // fi里面有一个被拦截的url
        // 里面调用DynamicInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        // 再调用DynamicAccessDecisionManager的decide方法来校验用户的权限是否足够
        InterceptorStatusToken token = null;
        HttpServletRequest request = fi.getHttpRequest();
        HttpServletResponse response = fi.getHttpResponse();
        String requestUrl = fi.getRequestUrl();
        String requestHttpMethod = request.getMethod();
        // 用户名获取及判断拦截
        String username = null;
        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            Object principalUser = ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            username = ((org.springframework.security.core.userdetails.User) principalUser).getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof String) {
            username = principal.toString();
        }
        if (!"admin".equals(username)) {
            // 非admin账号的拦截
            for (String[] onlyAdminUrl : onlyAdminUrls) {
                // 非admin账号不允许访问相关URL
                if (requestUrl.startsWith(onlyAdminUrl[1])
                        && (null == onlyAdminUrl[0] || onlyAdminUrl[0].equalsIgnoreCase(requestHttpMethod))) {
                    return;
                }
            }
        }
        // 其他的URL拦截
        for (String interceptUrl : interceptUrls) {
            // 需要拦截的URL
            if (requestUrl.startsWith(interceptUrl)) {
                boolean isExcept = false;
                for (String[] exceptUrl : exceptUrls) {
                    // 排除例外的URL
                    if (requestUrl.startsWith(exceptUrl[1])
                            && (null == exceptUrl[0] || exceptUrl[0].equalsIgnoreCase(requestHttpMethod))) {
                        isExcept = true;
                        break;
                    }
                }
                if (!isExcept) {
                    token = super.beforeInvocation(fi);
                }
            }
        }
        try {
            // 执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }


    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;

    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
