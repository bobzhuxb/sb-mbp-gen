package ${packageName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.config.Constants;
import ${packageName}.config.MybatisPlusConfig;
import ${packageName}.domain.SystemPermission;
import ${packageName}.domain.SystemUser;
import ${packageName}.domain.SystemUserRole;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.SystemRoleResourceCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.SystemUserRoleCriteria;
import ${packageName}.dto.criteria.filter.LongFilter;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.SystemPermissionMapper;
import ${packageName}.mapper.SystemRoleMapper;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.mapper.SystemUserRoleMapper;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.AccountService;
import ${packageName}.service.CommonUserService;
import ${packageName}.service.SystemRoleResourceService;
import ${packageName}.service.SystemUserService;
import ${packageName}.util.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户共通处理类
 * @author Bob
 */
@Service
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getCurrentUserId() {
        return SecurityUtils.getCurrentUserLogin().map(login -> findUserIdByLogin(login)).orElse(null);
    }

    /**
     * 根据login获取用户ID
     * @param login
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String findUserIdByLogin(String login) {
        return Optional.ofNullable(findUserByLogin(login, false)).map(user -> user.getId()).get();
    }

    /**
     * 获取当前用户
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserDTO getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().map(login -> findUserByLogin(login, true)).orElse(null);
    }

    /**
     * 根据login获取用户
     * @param login
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
