package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.domain.SystemUser;
import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.SystemRoleDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.criteria.SystemUserCriteria;
import ${packageName}.dto.criteria.filter.StringFilter;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.service.AccountService;
import ${packageName}.service.CommonUserService;
import ${packageName}.service.SystemUserService;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.web.rest.errors.CommonAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账户信息相关
 * @author Bob
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CommonUserService commonUserService;

    @Autowired
    private SystemUserMapper systemUserMapper;

    /**
     * 根据登录账号获取用户全信息
     * @param login
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public EnhanceUserDTO getFullUserInfoByLogin(String login) {
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
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword) {
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
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO changeSelfInfo(SystemUserDTO userDTO) {
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<Integer> validatePassword(String currentClearTextPassword) {
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO resetPassword(String userId) {
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
        SystemUser user = systemUserMapper.selectById(userId);
        String encryptedPassword = new BCryptPasswordEncoder().encode("123456");
        SystemUser userUpdate = new SystemUser();
        userUpdate.setId(user.getId());
        userUpdate.setPassword(encryptedPassword);
        systemUserMapper.updateById(userUpdate);
        return new ReturnCommonDTO();
    }

}
