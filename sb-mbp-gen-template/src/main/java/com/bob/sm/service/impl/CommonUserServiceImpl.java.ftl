package ${packageName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.config.Constants;
import ${packageName}.domain.SystemUser;
import ${packageName}.domain.SystemUserRole;
import ${packageName}.dto.SystemRoleDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.mapper.SystemRoleMapper;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.mapper.SystemUserRoleMapper;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.CommonUserService;
import ${packageName}.util.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户共通处理
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
    public String getCurrentUserId() {
        return SecurityUtils.getCurrentUserLogin().map(login -> findUserIdByLogin(login)).orElse(null);
    }

    /**
     * 根据login获取用户ID
     * @param login
     * @return
     */
    @Override
    public String findUserIdByLogin(String login) {
        return Optional.ofNullable(((CommonUserService)AopContext.currentProxy()).findCachedUserByLogin(login))
                .map(user -> user.getId()).get();
    }

    /**
     * 获取当前用户
     * @return
     */
    @Override
    public SystemUserDTO getCurrentUser() {
        return SecurityUtils.getCurrentUserLogin().map(login ->
                ((CommonUserService)AopContext.currentProxy()).findCachedUserByLogin(login)).orElse(null);
    }

    /**
     * 根据login获取用户（缓存存在时读缓存，缓存不存在时读库并将返回值设置到缓存中）
     * @param login
     * @return
     */
    @Override
    @Cacheable(cacheNames = Constants.CACHE_USER_INFO, key = "#login")
    public SystemUserDTO findCachedUserByLogin(String login) {
        return ((CommonUserService)AopContext.currentProxy()).findUserByLogin(login);
    }

    /**
     * 根据login获取用户（强制读库，并将返回值设置到缓存中）
     * @param login
     * @return
     */
    @Override
    @CachePut(cacheNames = Constants.CACHE_USER_INFO, key = "#login")
    public SystemUserDTO findForceCacheUserByLogin(String login) {
        return ((CommonUserService)AopContext.currentProxy()).findUserByLogin(login);
    }

    /**
     * 根据login获取用户
     * @param login
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserDTO findUserByLogin(String login) {
        List<SystemUser> userList = systemUserMapper.selectList(new QueryWrapper<SystemUser>().eq("login", login));
        if (userList != null && userList.size() > 0) {
            SystemUserDTO systemUserDTO = Optional.of(userList.get(0)).map(systemUser -> {
                SystemUserDTO systemUserDTOTmp = new SystemUserDTO();
                MyBeanUtil.copyNonNullProperties(systemUser, systemUserDTOTmp);
                return systemUserDTOTmp;
            }).get();
            systemUserDTO.setSystemRoleList(systemUserRoleMapper.selectList(
                    new QueryWrapper<SystemUserRole>().eq(SystemUserRole._systemUserId, systemUserDTO.getId()))
                    .stream().map(systemUserRole -> systemRoleMapper.selectById(systemUserRole.getSystemRoleId()))
                    .map(systemRole -> {
                        SystemRoleDTO systemRoleDTO = new SystemRoleDTO();
                        MyBeanUtil.copyNonNullProperties(systemRole, systemRoleDTO);
                        return systemRoleDTO;
                    }).collect(Collectors.toList()));
            return systemUserDTO;
        }
        return null;
    }

}
