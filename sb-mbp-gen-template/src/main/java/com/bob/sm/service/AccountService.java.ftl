package ${packageName}.service;

import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.SystemPermissionDTO;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;

/**
 * 账户
 * @author Bob
 */
public interface AccountService {

    EnhanceUserDTO getFullUserInfoByLogin(String login);

    ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword);

    ReturnCommonDTO resetPassword(String userId);

}
