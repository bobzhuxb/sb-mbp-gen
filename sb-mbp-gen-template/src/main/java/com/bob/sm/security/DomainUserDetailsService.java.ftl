package ${packageName}.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.domain.SystemResource;
import ${packageName}.domain.SystemResourcePermission;
import ${packageName}.domain.SystemUser;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.SystemPermissionCriteria;
import ${packageName}.dto.criteria.SystemResourcePermissionCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.LongFilter;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.service.AccountService;
import ${packageName}.service.SystemResourcePermissionService;
import ${packageName}.service.SystemUserService;
import ${packageName}.util.MyBeanUtil;
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
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemResourcePermissionService systemResourcePermissionService;

    public DomainUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        SystemUserCriteria systemUserCriteria = new SystemUserCriteria();
        StringFilter loginFilter = new StringFilter();
        loginFilter.setEquals(login);
        systemUserCriteria.setLogin(loginFilter);
        systemUserCriteria.setAssociationNameList(Arrays.asList("systemUserRoleList", "systemUserRoleList.systemRole"));
        List<SystemUserDTO> userList = systemUserService.findAll(systemUserCriteria);
        if (userList == null || userList.size() == 0) {
            throw new UsernameNotFoundException("用户" + login + "找不到");
        }
        return createSpringSecurityUser(login, userList.get(0));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login, SystemUserDTO userDTO) {
        List<SystemPermissionDTO> permissionList = getUserPermissions(login);
        List<SystemUserRoleDTO> userRoleList = userDTO.getSystemUserRoleList();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (userRoleList != null && userRoleList.size() > 0) {
            List<GrantedAuthority> grantedRoleNames = userRoleList.stream().map(
                    systemUserRoleDTO ->
                            new SimpleGrantedAuthority(systemUserRoleDTO.getSystemRole().getName()))
                    .collect(Collectors.toList());
            grantedAuthorities.addAll(grantedRoleNames);
        }
        List<GrantedAuthority> grantedPermissionIds = permissionList.stream()
                .map(permission -> new SimpleGrantedAuthority(String.valueOf(permission.getId())))
                .collect(Collectors.toList());
        grantedAuthorities.addAll(grantedPermissionIds);

        return new org.springframework.security.core.userdetails.User(userDTO.getLogin(),
                userDTO.getPassword(),
                grantedAuthorities);
    }

    @Transactional(readOnly = true)
    public List<SystemPermissionDTO> getUserPermissions(String login) {
        List<Long> resourceIdList = accountService.getFullUserInfoByLogin(login).getResources().stream()
                .map(systemResourceDTO -> systemResourceDTO.getId()).collect(Collectors.toList());
        SystemResourcePermissionCriteria resourcePermissionCriteria = new SystemResourcePermissionCriteria();
        LongFilter resourceIdFilter = new LongFilter();
        resourceIdFilter.setIn(resourceIdList);
        resourcePermissionCriteria.setSystemResourceId(resourceIdFilter);
        resourcePermissionCriteria.setAssociationNameList(Arrays.asList("systemPermission"));
        List<SystemPermissionDTO> permissionList = systemResourcePermissionService.findAll(resourcePermissionCriteria)
                .stream().map(systemResourcePermissionDTO -> systemResourcePermissionDTO.getSystemPermission())
                .collect(Collectors.toList());
        return permissionList;
    }

}
