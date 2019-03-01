package com.bob.sm.service;

import com.bob.sm.dto.SystemUserDTO;

public interface CommonUserService {

    Long getCurrentUserId();
    SystemUserDTO getCurrentUser();
    Long findUserIdByLogin(String login);
    SystemUserDTO findUserByLogin(String login, boolean withRole);

}
