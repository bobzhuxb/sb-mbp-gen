package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.dto.SystemRoleDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.help.InnerAuthFilterOperateDTO;
import ${packageName}.dto.help.InnerUserInfoDetailDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.service.AuthService;
import ${packageName}.service.CommonUserService;
import ${packageName}.util.MyBeanUtil;
import ${packageName}.web.rest.errors.CommonAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 数据权限相关
 * @author Bob
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * 前置操作
     */
    private static final String OPERATE_BEFORE = "before";

    /**
     * 覆盖操作
     */
    private static final String OPERATE_OVERWRITE = "overwrite";

    /**
     * 后续操作
     */
    private static final String OPERATE_AFTER = "after";

    @Autowired
    private CommonUserService commonUserService;

    /**
     * 获取当前用户明细及角色分析信息
     * @return 用户信息
     */
    @Override
    public InnerUserInfoDetailDTO commonGetCurrentUser() {
        // 用户信息明细返回
        InnerUserInfoDetailDTO userInfoDetailDTO = new InnerUserInfoDetailDTO();
        // 获取当前用户信息
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            return null;
        }
        // 角色分析
        List<SystemRoleDTO> roleList = systemUserDTO.getSystemRoleList();
        // 是否局端人员
        boolean isBureauRole = false;
        // 是否街道人员
        boolean isStreetRole = false;
        for (SystemRoleDTO role : roleList) {
            if (role.getName() != null && role.getName().startsWith(Constants.ROLE_BUREAU_START)) {
                isBureauRole = true;
            }
            if (role.getName() != null && role.getName().startsWith(Constants.ROLE_STREET_START)) {
                isStreetRole = true;
            }
        }
        userInfoDetailDTO.setSystemUserDTO(systemUserDTO);
        userInfoDetailDTO.setBureauRole(isBureauRole);
        userInfoDetailDTO.setStreetRole(isStreetRole);
        return userInfoDetailDTO;
    }

    /**
     * 共通的权限过滤
     * @param baseCriteria 过滤条件
     * @param appendParamMap 附加条件
     * @param interceptReturnInfo 拦截返回
     * @param organizationIdFilterStr 组织架构ID的过滤字符串
     * @param operateList 特例操作
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean commonDataAuthorityFilter(BaseCriteria baseCriteria, Map<String, Object> appendParamMap,
                                             ReturnCommonDTO interceptReturnInfo, String organizationIdFilterStr,
                                             List<InnerAuthFilterOperateDTO> operateList) {
        // 操作结果
        boolean result = false;
        if (!Constants.yesNo.YES.getValue().equals(baseCriteria.getAuthorityPass())) {
            // 之前未验证过权限的，才需要初次验证权限
            InnerUserInfoDetailDTO userInfoDetailDTO = commonGetCurrentUser();
            if (userInfoDetailDTO == null) {
                // 没有登录，没有权限查看数据
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_NONE, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            return false;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
            }
            // 追加结果systemUserDTO
            SystemUserDTO systemUserDTO = userInfoDetailDTO.getSystemUserDTO();
            appendParamMap.put(Constants.appendParamMapKey.USER_INFO_DETAIL.getValue(), userInfoDetailDTO);
            // 所属分局ID（分局ID为1表示区局，可以查看所有数据，其他只能查看自己分局的数据）
            String organId = systemUserDTO.getSystemOrganizationId();
            if (organId == null) {
                throw new CommonAlertException("当前用户没有配置所属分局");
            }
            // 追加前、中、后的特殊操作，如果不覆盖，则中间步骤走正常操作
            if (userInfoDetailDTO.getBureauRole()) {
                // 局端角色
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_BUREAU_START, OPERATE_BEFORE))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_BUREAU_START, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            if (!"1".equals(organId)) {
                                // 除了区局（organId=1）可查看全部数据外，其他只能查看自己分局的数据
                                MyBeanUtil.setObjectProperty(baseCriteria, organizationIdFilterStr + ".equals", organId);
                            }
                            // 获取对应的组织架构ID列表（请根据实际需求修改）
                            appendParamMap.put(Constants.appendParamMapKey.ROLE_NAME.getValue(), Constants.role.ROLE_BUREAU.getValue());
                            return true;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_BUREAU_START, OPERATE_AFTER))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
            } else if (userInfoDetailDTO.getStreetRole()) {
                // 街道人员角色
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_STREET_START, OPERATE_BEFORE))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_STREET_START, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            if (!"1".equals(organId)) {
                                // 除了区局（organId=1）可查看全部数据外，其他只能查看自己分局的数据
                                MyBeanUtil.setObjectProperty(baseCriteria, organizationIdFilterStr + ".equals", organId);
                            }
                            return true;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_STREET_START, OPERATE_AFTER))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
            } else {
                // 其他角色
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_OTHER, OPERATE_BEFORE))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_OTHER, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            // 其他角色无查看权限
                            return false;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_OTHER, OPERATE_AFTER))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
            }
        }
        return true;
    }

    /**
     * 获取具体的操作
     * @param operateList 操作列表
     * @param roleNameStart 角色类别
     * @param aopWhere 操作类型（方法前、方法覆盖、方法后）
     * @return 具体的操作
     */
    private Predicate<Map<String, Object>> getOperate(List<InnerAuthFilterOperateDTO> operateList,
                                                      String roleNameStart, String aopWhere) {
        if (operateList != null && operateList.size() > 0) {
            // 获取操作
            for (InnerAuthFilterOperateDTO operate : operateList) {
                if (operate.getRoleName() != null) {
                    // 找到指定角色的操作
                    boolean rightRole = false;
                    if (Constants.ROLE_BUREAU_START.equals(roleNameStart)
                            || Constants.ROLE_STREET_START.equals(roleNameStart)) {
                        rightRole = operate.getRoleName().startsWith(roleNameStart);
                    } else {
                        if (Constants.ROLE_OTHER.equals(roleNameStart)) {
                            rightRole = true;
                        }
                    }
                    // 返回具体的操作
                    if (rightRole) {
                        if (OPERATE_BEFORE.equals(aopWhere) && operate.getOperateBefore() != null) {
                            return operate.getOperateBefore();
                        } else if (OPERATE_OVERWRITE.equals(aopWhere) && operate.getOperateOverwrite() != null) {
                            return operate.getOperateOverwrite();
                        } else if (OPERATE_AFTER.equals(aopWhere) && operate.getOperateAfter() != null) {
                            return operate.getOperateAfter();
                        }
                    }
                }
            }
        }
        return null;
    }

}
