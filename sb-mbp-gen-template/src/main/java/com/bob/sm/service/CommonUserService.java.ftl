package ${packageName}.service;

import ${packageName}.dto.SystemUserDTO;

public interface CommonUserService {

    Long getCurrentUserId();
    SystemUserDTO getCurrentUser();
    Long findUserIdByLogin(String login);
    SystemUserDTO findUserByLogin(String login, boolean withRole);

}
