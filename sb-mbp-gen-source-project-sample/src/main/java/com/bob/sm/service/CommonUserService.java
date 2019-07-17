package com.bob.sm.service;

import com.bob.sm.dto.SystemUserDTO;

public interface CommonUserService {

    String getCurrentUserId();
    SystemUserDTO getCurrentUser();
    String findUserIdByLogin(String login);
    SystemUserDTO findUserByLogin(String login, boolean withRole);

}
