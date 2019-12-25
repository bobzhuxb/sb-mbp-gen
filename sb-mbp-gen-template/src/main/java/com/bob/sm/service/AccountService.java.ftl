package ${packageName}.service;

import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.help.ReturnCommonDTO;

/**
 * 账户
 * @author Bob
 */
public interface AccountService {

    EnhanceUserDTO getFullUserInfoByLogin(String login);

    ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword);

    ReturnCommonDTO changeSelfInfo(SystemUserDTO userDTO);

    ReturnCommonDTO validatePassword(String currentClearTextPassword);

    ReturnCommonDTO resetPassword(String userId);

}
