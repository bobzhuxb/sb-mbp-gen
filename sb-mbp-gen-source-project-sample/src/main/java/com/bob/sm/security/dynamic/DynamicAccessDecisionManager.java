package com.bob.sm.security.dynamic;

import com.bob.sm.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.dto.help.RemoteAuthorizationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 判定是否拥有权限
 */
@Service
public class DynamicAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private YmlConfig ymlConfig;

    // decide 方法是判定是否拥有权限的决策方法，
    // authentication 是CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
    // object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
    // configAttributes 为DynamicInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，
    // 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。
    // 如果不在权限表中则放行。
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // configAttributes：需要的权限许可，authentication：当前请求所拥有的权限
        String username = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof String) {
            username = principal.toString();
        }
        String remoteAuthorization = ymlConfig.getRemoteAuthorization();
        if ("false".equals(remoteAuthorization)) {
            // 关闭权限验证
            return;
        }
        if ("admin".equals(username)) {
            // admin账号默认拥有所有许可
            return;
        }
        String protocolPrefix = ymlConfig.getRemoteProtocolPrefix();
        String authorizationIp = ymlConfig.getRemoteAuthorizationIp();
        String authorizationPort = ymlConfig.getRemoteAuthorizationPort();
        String authorizationUrl = protocolPrefix + authorizationIp
                + (authorizationPort == null || "".equals(authorizationPort) || "80".equals(authorizationPort) ? "" : ":" + authorizationPort)
                + ymlConfig.getAuthorizationUrl();
        RemoteAuthorizationDTO remoteAuthorizationDTO = new RemoteAuthorizationDTO();
        remoteAuthorizationDTO.setLogin(username);
        remoteAuthorizationDTO.setResourceType("PERMISSION");
        String resultJson = HttpUtil.doPost(authorizationUrl, JSON.toJSONString(remoteAuthorizationDTO), null);
        if (resultJson != null) {
            RemoteAuthorizationDTO remoteAuthorizationResult = JSON.parseObject(resultJson, RemoteAuthorizationDTO.class);
            if ("success".equals(remoteAuthorizationResult.getAuthorizationResult())) {
                return;
            }
        }
        throw new AccessDeniedException("没有权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
