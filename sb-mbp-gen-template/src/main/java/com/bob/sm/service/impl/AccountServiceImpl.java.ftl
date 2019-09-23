package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.domain.SystemPermission;
import ${packageName}.domain.SystemUser;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.SystemRoleResourceCriteria;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.LongFilter;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.SystemPermissionMapper;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.service.AccountService;
import ${packageName}.service.CommonUserService;
import ${packageName}.service.SystemRoleResourceService;
import ${packageName}.service.SystemUserService;
import ${packageName}.util.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 账户
 * @author Bob
 */
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
    private SystemPermissionMapper systemPermissionMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

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
        List<EnhanceUserDTO> userList = systemUserService.baseFindAll("SystemUser", systemUserCriteria, null)
                .getData().stream().map(systemUserDTO -> {
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
        SystemUser user = systemUserMapper.selectById(userId);
        if (user == null) {
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "用户不存在");
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
