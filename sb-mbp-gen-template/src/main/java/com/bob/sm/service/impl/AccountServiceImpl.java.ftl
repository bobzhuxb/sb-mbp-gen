package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.domain.SystemUser;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.SystemRoleResourceCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.service.AccountService;
import ${packageName}.service.CommonUserService;
import ${packageName}.service.SystemRoleResourceService;
import ${packageName}.service.SystemUserService;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.web.rest.errors.CommonAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private CommonUserService commonUserService;

    @Autowired
    private SystemUserMapper systemUserMapper;

    /**
     * 根据登录账号获取用户全信息
     * @param login
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public EnhanceUserDTO getFullUserInfoByLogin(String login) {
        log.debug("Service ==> 获取用户全信息 {}", login);
        SystemUserCriteria systemUserCriteria = new SystemUserCriteria();
        StringFilter idFilter = new StringFilter();
        idFilter.setEquals(login);
        systemUserCriteria.setLogin(idFilter);
        systemUserCriteria.setAssociationNameList(Arrays.asList("systemOrganization",
                "systemUserRoleList", "systemUserRoleList.systemRole",
                "systemUserResourceList", "systemUserResourceList.systemResource"));
        List<EnhanceUserDTO> userList = systemUserService.baseFindAll("SystemUser", systemUserCriteria, null)
                .getData().stream().map(systemUserDTO -> {
            EnhanceUserDTO enhanceUserDTO = new EnhanceUserDTO();
            MyBeanUtil.copyNonNullProperties(systemUserDTO, enhanceUserDTO);
            // 设置角色
            enhanceUserDTO.setSystemRoleList(systemUserDTO.getSystemUserRoleList().stream().map(
                    systemUserRoleDTO -> systemUserRoleDTO.getSystemRole()).collect(Collectors.toList()));
            enhanceUserDTO.setSystemUserRoleList(null);
            // 设置资源
            List<SystemResourceDTO> userResourceList = new ArrayList<>();
            List<SystemUserResourceDTO> userResourceDTOList = systemUserDTO.getSystemUserResourceList();
            if (userResourceDTOList != null) {
                userResourceList = systemUserDTO.getSystemUserResourceList().stream()
                        .map(systemUserResourceDTO -> systemUserResourceDTO.getSystemResource()).collect(Collectors.toList());
            }
            List<SystemResourceDTO> roleResourceListDup = systemUserDTO.getSystemUserRoleList().stream()
                    .flatMap(systemUserRoleDTO -> {
                        SystemRoleResourceCriteria systemRoleResourceCriteria = new SystemRoleResourceCriteria();
                        StringFilter roleIdFilter = new StringFilter();
                        roleIdFilter.setEquals(systemUserRoleDTO.getSystemRoleId());
                        systemRoleResourceCriteria.setSystemRoleId(roleIdFilter);
                        systemRoleResourceCriteria.setAssociationNameList(Arrays.asList("systemResource"));
                        return systemRoleResourceService.baseFindAll("SystemRoleResource", systemRoleResourceCriteria, null)
                                .getData().stream().map(systemRoleResourceDTO -> systemRoleResourceDTO.getSystemResource());
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
    @Override
    public ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword) {
        log.debug("Service ==> 用户自己修改密码");
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "当前用户不存在");
        }
        String currentEncryptedPassword = systemUserDTO.getPassword();
        if (!new BCryptPasswordEncoder().matches(currentClearTextPassword, currentEncryptedPassword)) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "原密码错误");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        SystemUser userUpdate = new SystemUser();
        userUpdate.setId(systemUserDTO.getId());
        userUpdate.setPassword(encryptedPassword);
        systemUserMapper.updateById(userUpdate);
        return new ReturnCommonDTO();
    }

    /**
     * 当前用户修改自己的信息（基本信息）
     * @param userDTO
     * @return
     */
    @Override
    public ReturnCommonDTO changeSelfInfo(SystemUserDTO userDTO) {
        log.debug("Service ==> 用户自己修改信息，密码除外");
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "当前用户不存在");
        }
        // 不能修改账号、密码、账号状态、所属系统
        userDTO.setLogin(null);
        userDTO.setPassword(null);
        userDTO.setAccountStatus(null);
        userDTO.setSystemCode(null);
        // 更新用户信息
        SystemUser systemUserUpdate = new SystemUser();
        MyBeanUtil.copyNonNullProperties(userDTO, systemUserUpdate);
        systemUserUpdate.setId(systemUserDTO.getId());
        systemUserMapper.updateById(systemUserUpdate);
        return new ReturnCommonDTO();
    }

    /**
     * 当前用户验证密码
     * @param currentClearTextPassword
     * @return
     */
    public ReturnCommonDTO<Integer> validatePassword(String currentClearTextPassword) {
        log.debug("Service ==> 用户自己修改密码");
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "当前用户不存在");
        }
        String currentEncryptedPassword = systemUserDTO.getPassword();
        if (new BCryptPasswordEncoder().matches(currentClearTextPassword, currentEncryptedPassword)) {
            return new ReturnCommonDTO(Constants.yesNo.YES.getValue());
        } else {
            ReturnCommonDTO rtn = new ReturnCommonDTO(Constants.yesNo.NO.getValue());
            rtn.setErrMsg("原密码错误");
            return rtn;
        }
    }

    /**
     * 管理员重置别人的密码
     * @param userId
     * @return
     */
    public ReturnCommonDTO resetPassword(String userId) {
        log.debug("Service ==> 管理员重置别人的密码");
        // 权限验证
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            // 没有登录
            throw new CommonAlertException("您尚未登录");
        }
        List<SystemRoleDTO> roleList = systemUserDTO.getSystemRoleList();
        // 是否管理员
        boolean isAdminRole = false;
        for (SystemRoleDTO role : roleList) {
            if (role.getName() != null && role.getName().equals(Constants.role.ROLE_ADMIN.getValue())) {
                isAdminRole = true;
            }
        }
        if (!isAdminRole) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "您没有权限重置密码");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode("123456");
        SystemUser userUpdate = new SystemUser();
        userUpdate.setId(user.getId());
        userUpdate.setPassword(encryptedPassword);
        systemUserMapper.updateById(userUpdate);
        return new ReturnCommonDTO();
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

}
