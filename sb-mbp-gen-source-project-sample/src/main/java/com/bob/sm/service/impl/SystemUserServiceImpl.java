package com.bob.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.sm.config.Constants;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.domain.*;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.*;
import com.bob.sm.dto.criteria.filter.*;
import com.bob.sm.dto.help.MbpPage;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.mapper.*;
import com.bob.sm.security.SecurityUtils;
import com.bob.sm.service.*;
import com.bob.sm.util.MbpUtil;
import com.bob.sm.util.MyBeanUtil;
import com.bob.sm.web.rest.errors.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy
@Transactional
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService,
        BaseService<SystemUser> {

    private final Logger log = LoggerFactory.getLogger(SystemUserServiceImpl.class);

    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private SystemUserResourceMapper systemUserResourceMapper;

    @Autowired
    private SystemOrganizationService systemOrganizationService;

    @Autowired
    private SystemUserRoleService systemUserRoleService;

    @Autowired
    private SystemUserResourceService systemUserResourceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新增或更新
     * @param systemUserDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemUserDTO systemUserDTO) {
        log.debug("Service ==> 新增或修改SystemUser {}", systemUserDTO);
		if (systemUserDTO.getId() != null) {
			// 修改
			long systemUserId = systemUserDTO.getId();
			if (systemUserDTO.getSystemUserRoleList() != null) {
				// 清空角色列表
				systemUserRoleService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_user_id", systemUserId);
				}});
			}
			if (systemUserDTO.getSystemUserResourceList() != null) {
				// 清空资源列表
				systemUserResourceService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_user_id", systemUserId);
				}});
			}
		}
        // 新增或更新用户（当前实体）
        SystemUser systemUser = new SystemUser();
        MyBeanUtil.copyNonNullProperties(systemUserDTO, systemUser);
        if (systemUser.getId() == null) {
            // 新增
            systemUser.setInsertUserId(systemUserDTO.getOperateUserId());
            if (systemUser.getPassword() == null) {
                systemUser.setPassword("123456");
            }
            systemUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        }
        boolean result = saveOrUpdate(systemUser);
        long systemUserId = systemUser.getId();
        // 需要级联保存的属性
        if (systemUserDTO.getSystemUserRoleList() != null) {
            // 新增角色
            for (SystemUserRoleDTO systemUserRoleDTO : systemUserDTO.getSystemUserRoleList()) {
                systemUserRoleDTO.setId(null);
                systemUserRoleDTO.setSystemUserId(systemUserId);
                systemUserRoleDTO.setInsertUserId(systemUserDTO.getOperateUserId());
                systemUserRoleDTO.setOperateUserId(systemUserDTO.getOperateUserId());
                systemUserRoleDTO.setInsertTime(systemUserDTO.getInsertTime());
                systemUserRoleDTO.setUpdateTime(systemUserDTO.getUpdateTime());
                systemUserRoleService.save(systemUserRoleDTO);
			}
        }
        if (systemUserDTO.getSystemUserResourceList() != null) {
            // 新增资源
            for (SystemUserResourceDTO systemUserResourceDTO : systemUserDTO.getSystemUserResourceList()) {
                systemUserResourceDTO.setId(null);
                systemUserResourceDTO.setSystemUserId(systemUserId);
                systemUserResourceDTO.setInsertUserId(systemUserDTO.getOperateUserId());
                systemUserResourceDTO.setOperateUserId(systemUserDTO.getOperateUserId());
                systemUserResourceDTO.setInsertTime(systemUserDTO.getInsertTime());
                systemUserResourceDTO.setUpdateTime(systemUserDTO.getUpdateTime());
                systemUserResourceService.save(systemUserResourceDTO);
			}
        }
        systemUserDTO.setId(systemUserId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemUserDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemUserDTO {}", idList);
        for (long id : idList) {
		    ReturnCommonDTO returnCommonDTO = deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonException(returnCommonDTO.getErrMsg());
            }
        }
        return new ReturnCommonDTO();
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SystemUserDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(systemUser -> {
            // 删除级联的角色
            systemUserRoleService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_user_id", systemUser.getId());}});
            // 删除级联的资源
            systemUserResourceService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_user_id", systemUser.getId());}});
        });
        // 根据指定条件删除用户数据
        removeByMap(columnMap);
        return new ReturnCommonDTO();
    }

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public Optional<SystemUserDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemUserDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemUser> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemUser -> doConvert(systemUser, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemUserDTO> findAll(SystemUserCriteria criteria) {
        log.debug("Service ==> 查询所有SystemUserDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUser> wrapper = MbpUtil.getWrapper(null, criteria, SystemUser.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemUser -> doConvert(systemUser, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemUserDTO> findPage(SystemUserCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemUserDTO {}, {}", criteria, pageable);
        Page<SystemUser> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUser> wrapper = MbpUtil.getWrapper(null, criteria, SystemUser.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemUserDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemUser -> doConvert(systemUser, criteria));
        int totalCount = baseMapper.joinSelectCount(countQuerySql, wrapper);
        pageResult.setTotal((long)totalCount);
        return pageResult;
    }

    /**
     * 查询个数
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(readOnly = true)
    public int findCount(SystemUserCriteria criteria) {
        log.debug("Service ==> 查询个数SystemUserDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUser> wrapper = MbpUtil.getWrapper(null, criteria, SystemUser.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return 0;
        }
        return baseMapper.joinSelectCount(countQuerySql, wrapper);
    }

    /**
     * 附加的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param tableAliasName 表名的别名
     * @param paramName 参数名
     * @param paramValue 参数值
     * @return 增强后的Wrapper条件
     */
    public Wrapper<SystemUser> wrapperEnhance(QueryWrapper<SystemUser> wrapper, String tableAliasName,
                                             String paramName, Object paramValue) {
        // TODO: 增强的条件查询写在这里
        return wrapper;
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param criteria 附加条件
     * @return 查询通用Wrapper
     */
    private Wrapper<SystemUser> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemUserCriteria systemUserCriteria = new SystemUserCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemUserCriteria);
        Wrapper<SystemUser> wrapper = MbpUtil.getWrapper(null, systemUserCriteria, SystemUser.class, null, null, this);
        ((QueryWrapper<SystemUser>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemUser> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemUserCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemUser.getTableName() + "_" + tableCount + ".*";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        joinDataSql += getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @return
     */
    private String getCountQuerySql(SystemUserCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemUserCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemUser.getTableName() + " AS " + SystemUser.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemUserCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getSystemOrganization() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemOrganization.getTableName() + " AS " + SystemOrganization.getTableName() + "_" + tableCount + " ON "
                    + SystemOrganization.getTableName() + "_" + tableCount + ".id = " + SystemUser.getTableName() + "_" + fromTableCount
                    + ".system_organization_id";
            String tableKey = "systemOrganization";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += systemOrganizationService.getJoinSql(criteria.getSystemOrganization(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemUser 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemUserDTO doConvert(SystemUser systemUser, BaseCriteria criteria) {
        SystemUserDTO systemUserDTO = new SystemUserDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemUser, systemUserDTO);
        getAssociations(systemUserDTO, criteria);
        return systemUserDTO;
    }

    /**
     * 获取关联属性
     * @param systemUserDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemUserDTO getAssociations(SystemUserDTO systemUserDTO, BaseCriteria criteria) {
        if (systemUserDTO.getId() == null) {
            return systemUserDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("systemUserRoleList")) {
                // 获取角色列表
                List<SystemUserRoleDTO> systemUserRoleList = systemUserRoleMapper.selectList(
                        new QueryWrapper<SystemUserRole>().eq("system_user_id", systemUserDTO.getId())
                ).stream().map(systemUserRole -> {
                    SystemUserRoleDTO systemUserRoleDTO = new SystemUserRoleDTO();
                    MyBeanUtil.copyNonNullProperties(systemUserRole, systemUserRoleDTO);
                    return systemUserRoleDTO;
                }).collect(Collectors.toList());
                systemUserDTO.setSystemUserRoleList(systemUserRoleList);
                // 继续追查
                if (systemUserRoleList != null && systemUserRoleList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("systemUserRoleList.")) {
                            String associationName2 = associationName.substring("systemUserRoleList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria systemUserRoleCriteria = new BaseCriteria();
                    systemUserRoleCriteria.setAssociationNameList(associationName2List);
                    for (SystemUserRoleDTO systemUserRoleDTO : systemUserRoleList) {
                        systemUserRoleService.getAssociations(systemUserRoleDTO, systemUserRoleCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemUserResourceList")) {
                // 获取资源列表
                List<SystemUserResourceDTO> systemUserResourceList = systemUserResourceMapper.selectList(
                        new QueryWrapper<SystemUserResource>().eq("system_user_id", systemUserDTO.getId())
                ).stream().map(systemUserResource -> {
                    SystemUserResourceDTO systemUserResourceDTO = new SystemUserResourceDTO();
                    MyBeanUtil.copyNonNullProperties(systemUserResource, systemUserResourceDTO);
                    return systemUserResourceDTO;
                }).collect(Collectors.toList());
                systemUserDTO.setSystemUserResourceList(systemUserResourceList);
                // 继续追查
                if (systemUserResourceList != null && systemUserResourceList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("systemUserResourceList.")) {
                            String associationName2 = associationName.substring("systemUserResourceList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria systemUserResourceCriteria = new BaseCriteria();
                    systemUserResourceCriteria.setAssociationNameList(associationName2List);
                    for (SystemUserResourceDTO systemUserResourceDTO : systemUserResourceList) {
                        systemUserResourceService.getAssociations(systemUserResourceDTO, systemUserResourceCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemOrganization")) {
			    // 获取组织机构
                Long systemOrganizationId = systemUserDTO.getSystemOrganizationId();
                if (systemOrganizationId == null) {
                    return systemUserDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("systemOrganization.")) {
                        String associationName2 = associationName.substring("systemOrganization.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria systemOrganizationCriteria = new BaseCriteria();
                systemOrganizationCriteria.setAssociationNameList(associationName2List);
                Optional<SystemOrganizationDTO> systemOrganizationOptional = systemOrganizationService.findOne(systemOrganizationId, systemOrganizationCriteria);
                systemUserDTO.setSystemOrganization(systemOrganizationOptional.isPresent() ? systemOrganizationOptional.get() : null);
            }
        }
        return systemUserDTO;
    }

}
