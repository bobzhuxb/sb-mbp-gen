package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.dto.SystemRoleDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.criteria.BaseCriteria;
import ${packageName}.dto.help.InnerAuthFilterOperateDTO;
import ${packageName}.dto.help.MbpPage;
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

import java.util.ArrayList;
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
     * 共通的权限过滤
     * @param baseCriteria 过滤条件
     * @param appendParamMap 附加条件
     * @param interceptReturnInfo 拦截返回
     * @param dragStoreIdFilterStr 药店ID的过滤字符串
     * @param operateList 特例操作
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean commonDataAuthorityFilter(BaseCriteria baseCriteria, Map<String, Object> appendParamMap,
                                             ReturnCommonDTO interceptReturnInfo, String dragStoreIdFilterStr,
                                             List<InnerAuthFilterOperateDTO> operateList) {
		// TODO: 以下根据实际情况调整 
        // 操作结果
        boolean result = false;
        if (!Constants.yesNo.YES.getValue().equals(baseCriteria.getAuthorityPass())) {
            // 之前未验证过权限的，才需要初次验证权限
            SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
            if (systemUserDTO == null) {
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
            appendParamMap.put("systemUserDTO", systemUserDTO);
            // 角色分析
            List<SystemRoleDTO> roleList = systemUserDTO.getSystemRoleList();
            // 是否监管用户
            boolean isSuperviseRole = false;
            // 是否药店用户
            boolean isDragStoreRole = false;
            for (SystemRoleDTO role : roleList) {
                if (role.getName() != null && role.getName().startsWith(Constants.ROLE_SUPERVISE_START)) {
                    isSuperviseRole = true;
                }
                if (role.getName() != null && role.getName().startsWith(Constants.ROLE_DRAG_STORE_START)) {
                    isDragStoreRole = true;
                }
            }
            // 追加前、中、后的特殊操作，如果不覆盖，则中间步骤走正常操作
            if (isSuperviseRole) {
                // xx角色
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_SUPERVISE_START, OPERATE_BEFORE))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_SUPERVISE_START, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            // 监管角色能看到指定权限下的数据
                            String organizationId = systemUserDTO.getSystemOrganizationId();
                            if (organizationId == null) {
                                throw new CommonAlertException("当前用户没有配置所属部门");
                            }
                            // 获取对应的药店ID列表
                            List<String> dragStoreIdList = systemOrganizationService
                                    .getCachedDragStoreIdByOrganization(organizationId);
                            if (dragStoreIdList != null && dragStoreIdList.size() > 0) {
                                // 指定药店范围内的数据
                                MyBeanUtil.setObjectProperty(baseCriteria, dragStoreIdFilterStr + ".in",
                                        dragStoreIdList);
                            } else {
                                // 没有对应的药店，返回空数据（此处拦截返回）
                                interceptReturnInfo.setResultCode(Constants.commonReturnStatus.SUCCESS.getValue());
                                if (Constants.returnDataType.OBJECT.getValue().equals(interceptReturnInfo.getDataType())) {
                                    interceptReturnInfo.setData(new Object());
                                } else if (Constants.returnDataType.LIST.getValue().equals(interceptReturnInfo.getDataType())) {
                                    interceptReturnInfo.setData(new ArrayList());
                                } else if (Constants.returnDataType.PAGE.getValue().equals(interceptReturnInfo.getDataType())) {
                                    interceptReturnInfo.setData(new MbpPage() {});
                                } else if (Constants.returnDataType.INTEGER.getValue().equals(interceptReturnInfo.getDataType())) {
                                    interceptReturnInfo.setData(0);
                                } else {
                                    interceptReturnInfo.setData(null);
                                }
                            }
                            appendParamMap.put(Constants.AUTH_FILTER_IS_SUPERVISE, true);
                            return true;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_SUPERVISE_START, OPERATE_AFTER))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
            } else if (isDragStoreRole) {
                // 药店角色
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_DRAG_STORE_START, OPERATE_BEFORE))
                        .orElse(map -> true).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_DRAG_STORE_START, OPERATE_OVERWRITE))
                        .orElse(map -> {
                            // 正常操作
                            // 用户所属药店ID
                            String dragStoreId = systemUserDTO.getRdtsDragStoreId();
                            if (dragStoreId == null) {
                                throw new CommonAlertException("当前用户没有配置所属的药店");
                            }
                            appendParamMap.put(Constants.AUTH_FILTER_IS_DRAG_STORE, true);
                            appendParamMap.put(Constants.AUTH_FILTER_DRAG_STORE_ID, dragStoreId);
                            // 药店可看到自己的数据
                            MyBeanUtil.setObjectProperty(baseCriteria, dragStoreIdFilterStr + ".equals", dragStoreId);
                            return true;
                        }).test(appendParamMap);
                if (!result) {
                    return result;
                }
                result = Optional.ofNullable(getOperate(operateList, Constants.ROLE_DRAG_STORE_START, OPERATE_AFTER))
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
                    if (Constants.ROLE_SUPERVISE_START.equals(roleNameStart)
                            || Constants.ROLE_DRAG_STORE_START.equals(roleNameStart)) {
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
