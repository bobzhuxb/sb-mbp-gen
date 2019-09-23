package ${packageName}.security;

import ${packageName}.dto.*;
import ${packageName}.dto.criteria.SystemResourcePermissionCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.LongFilter;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.mapper.SystemPermissionMapper;
import ${packageName}.service.AccountService;
import ${packageName}.service.SystemResourcePermissionService;
import ${packageName}.service.SystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 * @author Bob
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemPermissionMapper systemPermissionMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        SystemUserCriteria systemUserCriteria = new SystemUserCriteria();
        StringFilter loginFilter = new StringFilter();
        loginFilter.setEquals(login);
        systemUserCriteria.setLogin(loginFilter);
        systemUserCriteria.setAssociationNameList(Arrays.asList("systemUserRoleList", "systemUserRoleList.systemRole"));
        List<SystemUserDTO> userList = systemUserService.baseFindAll("SystemUser", systemUserCriteria, null).getData();
        if (userList == null || userList.size() == 0) {
            throw new UsernameNotFoundException("用户" + login + "找不到");
        }
        return createSpringSecurityUser(login, userList.get(0));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login, SystemUserDTO userDTO) {
        List<SystemUserRoleDTO> userRoleList = userDTO.getSystemUserRoleList();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (userRoleList != null && userRoleList.size() > 0) {
            List<GrantedAuthority> grantedRoleNames = userRoleList.stream().map(
                    systemUserRoleDTO ->
                            new SimpleGrantedAuthority(systemUserRoleDTO.getSystemRole().getName()))
                    .collect(Collectors.toList());
            grantedAuthorities.addAll(grantedRoleNames);
        }
        // 动态权限
        List<String> permissionIdentifyList = systemPermissionMapper.findPermissionIdentifyByLogin(login);
        if (permissionIdentifyList != null && permissionIdentifyList.size() > 0) {
            List<GrantedAuthority> grantedPermissions = permissionIdentifyList.stream()
                    .map(permissionIdentify -> new SimpleGrantedAuthority(permissionIdentify))
                    .collect(Collectors.toList());
            grantedAuthorities.addAll(grantedPermissions);
        }

        return new org.springframework.security.core.userdetails.User(userDTO.getLogin(),
                userDTO.getPassword(),
                grantedAuthorities);
    }

}
