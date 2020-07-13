package ${packageName}.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.config.Constants;
import ${packageName}.domain.SystemUser;
import ${packageName}.domain.SystemUserRole;
import ${packageName}.dto.SystemRoleDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.SystemUserRoleDTO;
import ${packageName}.mapper.SystemRoleMapper;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.mapper.SystemUserRoleMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 * @author Bob
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private SystemRoleMapper systemRoleMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String loginWithPrefix) {
        log.debug("Authenticating {}", loginWithPrefix);
        String systemCode = null;
        String login = loginWithPrefix.substring(2);
        if (loginWithPrefix.startsWith(Constants.systemCode.WEB.getLoginAppendPrefix())) {
            // Web端
            systemCode = Constants.systemCode.WEB.getValue();
        } else if (loginWithPrefix.startsWith(Constants.systemCode.APP.getLoginAppendPrefix())) {
            // App端
            systemCode = Constants.systemCode.APP.getValue();
        }
        // 获取用户
        SystemUser userNow = systemUserMapper.selectOne(new QueryWrapper<SystemUser>()
                // 登录名条件
                .eq(SystemUser._login, login)
                // 登录入口条件
                .eq(SystemUser._systemCode, systemCode)
                // 用户启用条件
                .eq(SystemUser._accountStatus, Constants.enabled.ENABLED.getValue())
        );
        if (userNow == null) {
            throw new UsernameNotFoundException("用户" + login + "找不到");
        }
        SystemUserDTO userDTONow = new SystemUserDTO();
        MyBeanUtil.copyNonNullProperties(userNow, userDTONow);
        // 获取角色列表
        userDTONow.setSystemRoleList(Optional.ofNullable(systemUserRoleMapper.selectList(
                new QueryWrapper<SystemUserRole>().eq(SystemUserRole._systemUserId, userNow.getId())))
                .orElse(new ArrayList<>()).stream()
                .map(systemUserRole -> systemRoleMapper.selectById(systemUserRole.getSystemRoleId()))
                .map(systemRole -> {
                    SystemRoleDTO systemRoleDTO = new SystemRoleDTO();
                    MyBeanUtil.copyNonNullProperties(systemRole, systemRoleDTO);
                    return systemRoleDTO;
                }).collect(Collectors.toList()));
        return createSpringSecurityUser(login, userDTONow);
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String loginWithPrefix, SystemUserDTO userDTO) {
        List<SystemUserRoleDTO> userRoleList = userDTO.getSystemUserRoleList();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (userRoleList != null && userRoleList.size() > 0) {
            List<GrantedAuthority> grantedRoleNames = userRoleList.stream().map(
                    systemUserRoleDTO ->
                            new SimpleGrantedAuthority(systemUserRoleDTO.getSystemRole().getName()))
                    .collect(Collectors.toList());
            grantedAuthorities.addAll(grantedRoleNames);
        }

        return new org.springframework.security.core.userdetails.User(loginWithPrefix,
                userDTO.getPassword(),
                grantedAuthorities);
    }

}
