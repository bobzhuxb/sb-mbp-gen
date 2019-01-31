package com.bob.sm.security.dynamic;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

/**
 * 获取被拦截url所需的全部权限，在调用授权管理器AccessDecisionManager，
 * 这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，
 * 还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略（有：一票决定，一票否定，少数服从多数等），
 * 如果权限足够，则返回，权限不够则报错并调用权限不足页面。
 */
@Service
public class DynamicInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {

    private HashMap<String, Collection<ConfigAttribute>> map = null;

//    /**
//     * 加载资源，初始化资源变量
//     */
//    public void loadResourceDefine() {
//        map = new HashMap<>();
//        Collection<ConfigAttribute> array;
//        // 只获取第二层级的（第一层级时类的URL）
//        BasePermissionCriteria basePermissionCriteria = new BasePermissionCriteria();
//        IntegerFilter currentLevelFilter = new IntegerFilter();
//        currentLevelFilter.setEquals(2);
//        basePermissionCriteria.setCurrentLevel(currentLevelFilter);
//        final Specification<BasePermission> specification = basePermissionQueryService.createSpecification(basePermissionCriteria);
//        List<BasePermission> permissionList = basePermissionRepository.findAll(specification);
//        for (BasePermission permission : permissionList) {
//            array = new ArrayList<>();
//            // 此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。
//            // 此处添加的信息将会作为DynamicAuthAccessDecisionManager类的decide的第三个参数。
//            array.add(new SecurityConfig(String.valueOf(permission.getId())));
//            // 用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value
//            map.put(permission.getHttpType() + "_" + permission.getHttpUrl(), array);
//        }
//    }

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，
     * 用来判定用户是否有此权限。如果不在权限表中则放行。
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        if (map == null) {
//            loadResourceDefine();
//        }
//        // object 中包含用户请求的request 信息
//        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
//        for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
//            String resMethodUrl = iter.next();
//            int firstUnderlineIndex = resMethodUrl.indexOf("_");
//            String resMethod = resMethodUrl.substring(0, firstUnderlineIndex);
//            String resUrl = resMethodUrl.substring(firstUnderlineIndex + 1);
//            AntPathRequestMatcher matcher = new AntPathRequestMatcher(resUrl, resMethod);
//            if (matcher.matches(request)) {
//                return map.get(resMethodUrl);
//            }
//        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
