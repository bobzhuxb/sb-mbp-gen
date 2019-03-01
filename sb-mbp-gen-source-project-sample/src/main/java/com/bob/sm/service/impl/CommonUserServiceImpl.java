package com.bob.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.sm.config.Constants;
import com.bob.sm.config.MybatisPlusConfig;
import com.bob.sm.domain.SystemPermission;
import com.bob.sm.domain.SystemUser;
import com.bob.sm.domain.SystemUserRole;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.SystemRoleResourceCriteria;
import com.bob.sm.dto.criteria.SystemUserCriteria;
import com.bob.sm.dto.criteria.SystemUserRoleCriteria;
import com.bob.sm.dto.criteria.filter.LongFilter;
import com.bob.sm.dto.criteria.filter.StringFilter;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.mapper.SystemPermissionMapper;
import com.bob.sm.mapper.SystemRoleMapper;
import com.bob.sm.mapper.SystemUserMapper;
import com.bob.sm.mapper.SystemUserRoleMapper;
import com.bob.sm.security.SecurityUtils;
import com.bob.sm.service.AccountService;
import com.bob.sm.service.CommonUserService;
import com.bob.sm.service.SystemRoleResourceService;
import com.bob.sm.service.SystemUserService;
import com.bob.sm.util.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy
@Transactional
public class CommonUserServiceImpl implements CommonUserService {

    private final Logger log = LoggerFactory.getLogger(CommonUserServiceImpl.class);

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private SystemRoleMapper systemRoleMapper;

    /**
     * 获取当前用户的用户ID
     * @return
     */
    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserLogin().map(login -> findUserIdByLogin(login)).get();
    }

    /**
     * 根据login获取用户ID
     * @param login
     * @return
     */
    public Long findUserIdByLogin(String login) {
        return Optional.ofNullable(findUserByLogin(login, false)).map(user -> user.getId()).get();
    }

    /**
     * 获取当前用户
     * @return
     */
    public SystemUserDTO getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().map(login -> findUserByLogin(login, true)).get();
    }

    /**
     * 根据login获取用户
     * @param login
     * @return
     */
    public SystemUserDTO findUserByLogin(String login, boolean withRole) {
        List<SystemUser> userList = systemUserMapper.selectList(new QueryWrapper<SystemUser>().eq("login", login));
        if (userList != null && userList.size() > 0) {
            SystemUserDTO systemUserDTO = Optional.of(userList.get(0)).map(systemUser -> {
                SystemUserDTO systemUserDTOTmp = new SystemUserDTO();
                MyBeanUtil.copyNonNullProperties(systemUser, systemUserDTOTmp);
                return systemUserDTOTmp;
            }).get();
            if (withRole) {
                systemUserDTO.setSystemRoleList(systemUserRoleMapper.selectList(
                        new QueryWrapper<SystemUserRole>().eq("system_user_id", systemUserDTO.getId()))
                        .stream().map(systemUserRole -> systemRoleMapper.selectById(systemUserRole.getSystemRoleId()))
                        .map(systemRole -> {
                            SystemRoleDTO systemRoleDTO = new SystemRoleDTO();
                            MyBeanUtil.copyNonNullProperties(systemRole, systemRoleDTO);
                            return systemRoleDTO;
                        }).collect(Collectors.toList()));
            }
            return systemUserDTO;
        }
        return null;
    }

}
