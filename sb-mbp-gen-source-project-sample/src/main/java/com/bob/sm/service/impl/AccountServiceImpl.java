package com.bob.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.sm.config.Constants;
import com.bob.sm.domain.SystemPermission;
import com.bob.sm.domain.SystemUser;
import com.bob.sm.dto.EnhanceUserDTO;
import com.bob.sm.dto.SystemPermissionDTO;
import com.bob.sm.dto.SystemResourceDTO;
import com.bob.sm.dto.SystemUserResourceDTO;
import com.bob.sm.dto.criteria.SystemRoleResourceCriteria;
import com.bob.sm.dto.criteria.SystemUserCriteria;
import com.bob.sm.dto.criteria.filter.LongFilter;
import com.bob.sm.dto.criteria.filter.StringFilter;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.mapper.SystemPermissionMapper;
import com.bob.sm.mapper.SystemUserMapper;
import com.bob.sm.security.SecurityUtils;
import com.bob.sm.service.AccountService;
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
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemRoleResourceService systemRoleResourceService;

    @Autowired
    private SystemPermissionMapper systemPermissionMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据登录账号获取用户全信息
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    public EnhanceUserDTO getFullUserInfoByLogin(String login) {
        log.debug("Service ==> 获取用户全信息 {}", login);
        SystemUserCriteria systemUserCriteria = new SystemUserCriteria();
        StringFilter idFilter = new StringFilter();
        idFilter.setEquals(login);
        systemUserCriteria.setLogin(idFilter);
        systemUserCriteria.setAssociationNameList(Arrays.asList("systemOrganization",
                "systemUserRoleList",
                "systemUserResourceList", "systemUserResourceList.systemResource"));
        List<EnhanceUserDTO> userList = systemUserService.findAll(systemUserCriteria, null).getData().stream().map(systemUserDTO -> {
            EnhanceUserDTO enhanceUserDTO = new EnhanceUserDTO();
            MyBeanUtil.copyNonNullProperties(systemUserDTO, enhanceUserDTO);
            List<SystemResourceDTO> userResourceList = new ArrayList<>();
            List<SystemUserResourceDTO> userResourceDTOList = systemUserDTO.getSystemUserResourceList();
            if (userResourceDTOList != null) {
                userResourceList = systemUserDTO.getSystemUserResourceList().stream()
                        .map(systemUserResourceDTO -> systemUserResourceDTO.getSystemResource()).collect(Collectors.toList());
            }
            List<SystemResourceDTO> roleResourceListDup = systemUserDTO.getSystemUserRoleList().stream()
                    .flatMap(systemUserRoleDTO -> {
                        SystemRoleResourceCriteria systemRoleResourceCriteria = new SystemRoleResourceCriteria();
                        LongFilter roleIdFilter = new LongFilter();
                        roleIdFilter.setEquals(systemUserRoleDTO.getSystemRoleId());
                        systemRoleResourceCriteria.setSystemRoleId(roleIdFilter);
                        systemRoleResourceCriteria.setAssociationNameList(Arrays.asList("systemResource"));
                        return systemRoleResourceService.findAll(systemRoleResourceCriteria, null).getData().stream()
                                .map(systemRoleResourceDTO -> systemRoleResourceDTO.getSystemResource());
                    }).collect(Collectors.toList());
            // 角色的资源去重
            List<SystemResourceDTO> roleResourceList = new ArrayList<>();
            resourceUnDuplicate(roleResourceListDup, roleResourceList);
            // 合并资源并去重
            List<SystemResourceDTO> resourceList = new ArrayList<>();
            resourceList.addAll(userResourceList);
            resourceUnDuplicate(roleResourceList, resourceList);
            // 设置值
            enhanceUserDTO.setResourcesFromUser(userResourceList);
            enhanceUserDTO.setResourcesFromRole(roleResourceList);
            enhanceUserDTO.setResources(resourceList);
            return enhanceUserDTO;
        }).collect(Collectors.toList());
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    /**
     * 当前用户修改自己的密码
     * @param currentClearTextPassword
     * @param newPassword
     * @return
     */
    public ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword) {
        log.debug("Service ==> 用户自己修改密码");
        Optional<SystemUser> userOptional = SecurityUtils.getCurrentUserLogin()
                .map(login -> systemUserMapper.selectById(1L));
        if (!userOptional.isPresent()) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "用户不存在");
        }
        SystemUser user = userOptional.get();
        String currentEncryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "原密码错误");
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        SystemUser userUpdate = new SystemUser();
        userUpdate.setId(user.getId());
        userUpdate.setPassword(encryptedPassword);
        user.setPassword(encryptedPassword);
        return new ReturnCommonDTO();
    }

    /**
     * 管理员重置别人的密码
     * @param userId
     * @return
     */
    public ReturnCommonDTO resetPassword(long userId) {
        log.debug("Service ==> 管理员重置别人的密码");
        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "用户不存在");
        }
        String encryptedPassword = passwordEncoder.encode("123456");
        SystemUser userUpdate = new SystemUser();
        userUpdate.setId(user.getId());
        userUpdate.setPassword(encryptedPassword);
        user.setPassword(encryptedPassword);
        return new ReturnCommonDTO();
    }

    /**
     * 保存许可（包括子数据）
     * @param permissionList
     */
    public void savePermissionsWithChildren(List<SystemPermissionDTO> permissionList) {
        log.debug("Service ==> 保存许可 {}", permissionList);
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for (SystemPermissionDTO permissionParentDTO : permissionList) {
            // 1、先存父级资源
            String httpUrl = permissionParentDTO.getHttpUrl();
            String functionCategroy = permissionParentDTO.getFunctionCategroy();
            int currentLevel = permissionParentDTO.getCurrentLevel();
            SystemPermission permissionExist = systemPermissionMapper.selectOne(new QueryWrapper<SystemPermission>()
                    .eq("http_url", httpUrl).eq("function_categroy", functionCategroy)
                    .eq("current_level", currentLevel));
            SystemPermission permissionParent = new SystemPermission();
            MyBeanUtil.copyNonNullProperties(permissionParentDTO, permissionParent);
            saveOrUpdatePermission(permissionExist, permissionParent, nowTime);
            long parentId = permissionParent.getId();
            // 2、再存子资源
            List<SystemPermissionDTO> childPermissionList = permissionParentDTO.getChildList();
            for (SystemPermissionDTO permissionChildDTO : childPermissionList) {
                String childHttpType = permissionChildDTO.getHttpType();
                String childHttpUrl = permissionChildDTO.getHttpUrl();
                // 合并类URL和方法URL
                String childFullHttpUrl = httpUrl + childHttpUrl;
                permissionChildDTO.setHttpUrl(childFullHttpUrl);
                int childCurrentLevel = permissionChildDTO.getCurrentLevel();
                SystemPermission permissionChildExist = systemPermissionMapper.selectOne(
                        new QueryWrapper<SystemPermission>()
                                .eq("http_type", childHttpType).eq("http_url", childFullHttpUrl)
                                .eq("current_level", childCurrentLevel));
                SystemPermission permissionChild = new SystemPermission();
                MyBeanUtil.copyNonNullProperties(permissionChildDTO, permissionChild);
                permissionChild.setParentId(parentId);
                saveOrUpdatePermission(permissionChildExist, permissionChild, nowTime);
            }
        }
    }

    private void resourceUnDuplicate(List<SystemResourceDTO> newResourceList, List<SystemResourceDTO> toResourceList) {
        for (SystemResourceDTO roleResourceDTO : newResourceList) {
            boolean resourceExist = false;
            for (SystemResourceDTO systemResourceDTOExist : toResourceList) {
                if (systemResourceDTOExist.getId().equals(roleResourceDTO)) {
                    resourceExist = true;
                    break;
                }
            }
            if (!resourceExist) {
                toResourceList.add(roleResourceDTO);
            }
        }
    }

    /**
     * 处理许可数据（中间过程）
     * @param permissionExist
     * @param systemPermission
     * @param nowTime
     */
    private void saveOrUpdatePermission(SystemPermission permissionExist, SystemPermission systemPermission,
                                        String nowTime) {
        if (systemPermission.getParentId() == null) {
            systemPermission.setParentId(0L);
        }
        if (permissionExist != null) {
            // 已经存在该数据
            systemPermission.setId(permissionExist.getId());
            systemPermission.setUpdateTime(nowTime);
            systemPermissionMapper.updateById(systemPermission);
        } else {
            // 不存在该数据
            systemPermission.setInsertTime(nowTime);
            systemPermission.setUpdateTime(nowTime);
            systemPermissionMapper.insert(systemPermission);
        }
    }

}
