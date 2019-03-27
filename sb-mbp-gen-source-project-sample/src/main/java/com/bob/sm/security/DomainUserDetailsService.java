package com.bob.sm.security;

import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.SystemResourcePermissionCriteria;
import com.bob.sm.dto.criteria.SystemUserCriteria;
import com.bob.sm.dto.criteria.filter.LongFilter;
import com.bob.sm.dto.criteria.filter.StringFilter;
import com.bob.sm.service.AccountService;
import com.bob.sm.service.SystemResourcePermissionService;
import com.bob.sm.service.SystemUserService;
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
        // TODO: 暂时删除动态权限
//        List<Long> permissionIdList = getPermissionIdsOfUser(login);
//        List<GrantedAuthority> grantedPermissionIds = permissionIdList.stream()
//                .map(permissionId -> new SimpleGrantedAuthority(String.valueOf(permissionId)))
//                .collect(Collectors.toList());
//        grantedAuthorities.addAll(grantedPermissionIds);

        return new org.springframework.security.core.userdetails.User(userDTO.getLogin(),
                userDTO.getPassword(),
                grantedAuthorities);
    }

    @Transactional(readOnly = true)
    public List<Long> getPermissionIdsOfUser(String login) {
        List<Long> resourceIdList = accountService.getFullUserInfoByLogin(login).getResources().stream()
                .map(systemResourceDTO -> systemResourceDTO.getId()).collect(Collectors.toList());
        SystemResourcePermissionCriteria resourcePermissionCriteria = new SystemResourcePermissionCriteria();
        LongFilter resourceIdFilter = new LongFilter();
        resourceIdFilter.setIn(resourceIdList);
        resourcePermissionCriteria.setSystemResourceId(resourceIdFilter);
        resourcePermissionCriteria.setAssociationNameList(Arrays.asList("systemPermission"));
        List<SystemResourcePermissionDTO> systemResourcePermissionDTOList
                = systemResourcePermissionService.baseFindAll(
                        "SystemResourcePermission", resourcePermissionCriteria, null).getData();
        // 去重
        List<Long> permissionIdList = new ArrayList<>();
        for (SystemResourcePermissionDTO systemResourcePermissionDTO : systemResourcePermissionDTOList) {
            boolean exist = false;
            for (long permissionId : permissionIdList) {
                if (systemResourcePermissionDTO.getSystemPermissionId().equals(permissionId)) {
                    exist = true;
                }
            }
            if (!exist) {
                permissionIdList.add(systemResourcePermissionDTO.getSystemPermissionId());
            }
        }
        return permissionIdList;
    }

}
