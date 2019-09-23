package ${packageName}.service;

import ${packageName}.dto.SystemUserDTO;

/**
 * 系统用户共通处理类
 * @author Bob
 */
public interface CommonUserService {

    String getCurrentUserId();
    SystemUserDTO getCurrentUser();
    String findUserIdByLogin(String login);
    SystemUserDTO findUserByLogin(String login, boolean withRole);

}
