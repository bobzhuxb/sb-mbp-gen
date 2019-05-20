package com.bob.sm.service;

import com.bob.sm.dto.EnhanceUserDTO;
import com.bob.sm.dto.SystemPermissionDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;

import java.util.List;

public interface AccountService {

    EnhanceUserDTO getFullUserInfoByLogin(String login);

    ReturnCommonDTO changePassword(String currentClearTextPassword, String newPassword);

    ReturnCommonDTO resetPassword(long userId);

}
