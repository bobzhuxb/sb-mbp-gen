package ${packageName}.service;

import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.SystemPermissionDTO;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;

public interface AccountService {

    EnhanceUserDTO getFullUserInfoByLogin(String login);

    ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword);

    ReturnCommonDTO resetPassword(long userId);

}
