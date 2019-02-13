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
public class SystemResourceServiceImpl extends ServiceImpl<SystemResourceMapper, SystemResource> implements SystemResourceService,
        BaseService<SystemResource> {

    private final Logger log = LoggerFactory.getLogger(SystemResourceServiceImpl.class);

    @Autowired
    private SystemResourceMapper childMapper;

    @Autowired
    private SystemResourcePermissionMapper systemResourcePermissionMapper;

    @Autowired
    private SystemRoleResourceMapper systemRoleResourceMapper;

    @Autowired
    private SystemUserResourceMapper systemUserResourceMapper;

    @Autowired
    private SystemResourceService parentService;

    @Autowired
    private SystemResourceService childService;

    @Autowired
    private SystemResourcePermissionService systemResourcePermissionService;

    @Autowired
    private SystemRoleResourceService systemRoleResourceService;

    @Autowired
    private SystemUserResourceService systemUserResourceService;

    /**
     * 新增或更新
     * @param systemResourceDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemResourceDTO systemResourceDTO) {
        log.debug("Service ==> 新增或修改SystemResource {}", systemResourceDTO);
		if (systemResourceDTO.getId() != null) {
			// 修改
			long systemResourceId = systemResourceDTO.getId();
			if (systemResourceDTO.getChildList() != null) {
				// 清空子资源列表
				childService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("parent_id", systemResourceId);
				}});
			}
			if (systemResourceDTO.getSystemResourcePermissionList() != null) {
				// 清空许可列表
				systemResourcePermissionService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_resource_id", systemResourceId);
				}});
			}
			if (systemResourceDTO.getSystemRoleResourceList() != null) {
				// 清空角色列表
				systemRoleResourceService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_resource_id", systemResourceId);
				}});
			}
			if (systemResourceDTO.getSystemUserResourceList() != null) {
				// 清空用户列表
				systemUserResourceService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_resource_id", systemResourceId);
				}});
			}
		}
        // 新增或更新资源（当前实体）
        SystemResource systemResource = new SystemResource();
        MyBeanUtil.copyNonNullProperties(systemResourceDTO, systemResource);
        if (systemResource.getId() == null) {
            // 新增
            systemResource.setInsertUserId(systemResourceDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemResource);
        long systemResourceId = systemResource.getId();
        // 需要级联保存的属性
        if (systemResourceDTO.getChildList() != null) {
            // 新增子资源
            for (SystemResourceDTO childDTO : systemResourceDTO.getChildList()) {
                childDTO.setId(null);
                childDTO.setParentId(systemResourceId);
                childDTO.setInsertUserId(systemResourceDTO.getOperateUserId());
                childDTO.setOperateUserId(systemResourceDTO.getOperateUserId());
                childDTO.setInsertTime(systemResourceDTO.getInsertTime());
                childDTO.setUpdateTime(systemResourceDTO.getUpdateTime());
                childService.save(childDTO);
			}
        }
        if (systemResourceDTO.getSystemResourcePermissionList() != null) {
            // 新增许可
            for (SystemResourcePermissionDTO systemResourcePermissionDTO : systemResourceDTO.getSystemResourcePermissionList()) {
                systemResourcePermissionDTO.setId(null);
                systemResourcePermissionDTO.setSystemResourceId(systemResourceId);
                systemResourcePermissionDTO.setInsertUserId(systemResourceDTO.getOperateUserId());
                systemResourcePermissionDTO.setOperateUserId(systemResourceDTO.getOperateUserId());
                systemResourcePermissionDTO.setInsertTime(systemResourceDTO.getInsertTime());
                systemResourcePermissionDTO.setUpdateTime(systemResourceDTO.getUpdateTime());
                systemResourcePermissionService.save(systemResourcePermissionDTO);
			}
        }
        if (systemResourceDTO.getSystemRoleResourceList() != null) {
            // 新增角色
            for (SystemRoleResourceDTO systemRoleResourceDTO : systemResourceDTO.getSystemRoleResourceList()) {
                systemRoleResourceDTO.setId(null);
                systemRoleResourceDTO.setSystemResourceId(systemResourceId);
                systemRoleResourceDTO.setInsertUserId(systemResourceDTO.getOperateUserId());
                systemRoleResourceDTO.setOperateUserId(systemResourceDTO.getOperateUserId());
                systemRoleResourceDTO.setInsertTime(systemResourceDTO.getInsertTime());
                systemRoleResourceDTO.setUpdateTime(systemResourceDTO.getUpdateTime());
                systemRoleResourceService.save(systemRoleResourceDTO);
			}
        }
        if (systemResourceDTO.getSystemUserResourceList() != null) {
            // 新增用户
            for (SystemUserResourceDTO systemUserResourceDTO : systemResourceDTO.getSystemUserResourceList()) {
                systemUserResourceDTO.setId(null);
                systemUserResourceDTO.setSystemResourceId(systemResourceId);
                systemUserResourceDTO.setInsertUserId(systemResourceDTO.getOperateUserId());
                systemUserResourceDTO.setOperateUserId(systemResourceDTO.getOperateUserId());
                systemUserResourceDTO.setInsertTime(systemResourceDTO.getInsertTime());
                systemUserResourceDTO.setUpdateTime(systemResourceDTO.getUpdateTime());
                systemUserResourceService.save(systemUserResourceDTO);
			}
        }
        systemResourceDTO.setId(systemResourceId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemResourceDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemResourceDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemResourceDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(systemResource -> {
            // 删除级联的子资源
            childService.deleteByMapCascade(new HashMap<String, Object>() {{put("parent_id", systemResource.getId());}});
            // 删除级联的许可
            systemResourcePermissionService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_resource_id", systemResource.getId());}});
            // 删除级联的角色
            systemRoleResourceService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_resource_id", systemResource.getId());}});
            // 删除级联的用户
            systemUserResourceService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_resource_id", systemResource.getId());}});
        });
        // 根据指定条件删除资源数据
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
    public Optional<SystemResourceDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemResourceDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemResource> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemResource -> doConvert(systemResource, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemResourceDTO> findAll(SystemResourceCriteria criteria) {
        log.debug("Service ==> 查询所有SystemResourceDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemResource.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemResource -> doConvert(systemResource, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemResourceDTO> findPage(SystemResourceCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemResourceDTO {}, {}", criteria, pageable);
        Page<SystemResource> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemResource.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemResourceDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemResource -> doConvert(systemResource, criteria));
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
    public int findCount(SystemResourceCriteria criteria) {
        log.debug("Service ==> 查询个数SystemResourceDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemResource.class, null, tableIndexMap, this);
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
    public Wrapper<SystemResource> wrapperEnhance(QueryWrapper<SystemResource> wrapper, String tableAliasName,
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
    private Wrapper<SystemResource> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemResourceCriteria systemResourceCriteria = new SystemResourceCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemResourceCriteria);
        Wrapper<SystemResource> wrapper = MbpUtil.getWrapper(null, systemResourceCriteria, SystemResource.class, null, null, this);
        ((QueryWrapper<SystemResource>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemResource> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemResourceCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemResource.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemResourceCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemResourceCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemResource.getTableName() + " AS " + SystemResource.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemResourceCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getParent() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemResource.getTableName() + " AS " + SystemResource.getTableName() + "_" + tableCount + " ON "
                    + SystemResource.getTableName() + "_" + tableCount + ".id = " + SystemResource.getTableName() + "_" + fromTableCount
                    + ".parent_id";
            String tableKey = "parent";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += parentService.getJoinSql(criteria.getParent(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemResource 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemResourceDTO doConvert(SystemResource systemResource, BaseCriteria criteria) {
        SystemResourceDTO systemResourceDTO = new SystemResourceDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemResource, systemResourceDTO);
        getAssociations(systemResourceDTO, criteria);
        return systemResourceDTO;
    }

    /**
     * 获取关联属性
     * @param systemResourceDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemResourceDTO getAssociations(SystemResourceDTO systemResourceDTO, BaseCriteria criteria) {
        if (systemResourceDTO.getId() == null) {
            return systemResourceDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("childList")) {
                // 获取子资源列表
                List<SystemResourceDTO> childList = childMapper.selectList(
                        new QueryWrapper<SystemResource>().eq("parent_id", systemResourceDTO.getId())
                ).stream().map(child -> {
                    SystemResourceDTO childDTO = new SystemResourceDTO();
                    MyBeanUtil.copyNonNullProperties(child, childDTO);
                    return childDTO;
                }).collect(Collectors.toList());
                systemResourceDTO.setChildList(childList);
                // 继续追查
                if (childList != null && childList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("childList.")) {
                            String associationName2 = associationName.substring("childList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria childCriteria = new BaseCriteria();
                    childCriteria.setAssociationNameList(associationName2List);
                    for (SystemResourceDTO childDTO : childList) {
                        childService.getAssociations(childDTO, childCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemResourcePermissionList")) {
                // 获取许可列表
                List<SystemResourcePermissionDTO> systemResourcePermissionList = systemResourcePermissionMapper.selectList(
                        new QueryWrapper<SystemResourcePermission>().eq("system_resource_id", systemResourceDTO.getId())
                ).stream().map(systemResourcePermission -> {
                    SystemResourcePermissionDTO systemResourcePermissionDTO = new SystemResourcePermissionDTO();
                    MyBeanUtil.copyNonNullProperties(systemResourcePermission, systemResourcePermissionDTO);
                    return systemResourcePermissionDTO;
                }).collect(Collectors.toList());
                systemResourceDTO.setSystemResourcePermissionList(systemResourcePermissionList);
                // 继续追查
                if (systemResourcePermissionList != null && systemResourcePermissionList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("systemResourcePermissionList.")) {
                            String associationName2 = associationName.substring("systemResourcePermissionList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria systemResourcePermissionCriteria = new BaseCriteria();
                    systemResourcePermissionCriteria.setAssociationNameList(associationName2List);
                    for (SystemResourcePermissionDTO systemResourcePermissionDTO : systemResourcePermissionList) {
                        systemResourcePermissionService.getAssociations(systemResourcePermissionDTO, systemResourcePermissionCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemRoleResourceList")) {
                // 获取角色列表
                List<SystemRoleResourceDTO> systemRoleResourceList = systemRoleResourceMapper.selectList(
                        new QueryWrapper<SystemRoleResource>().eq("system_resource_id", systemResourceDTO.getId())
                ).stream().map(systemRoleResource -> {
                    SystemRoleResourceDTO systemRoleResourceDTO = new SystemRoleResourceDTO();
                    MyBeanUtil.copyNonNullProperties(systemRoleResource, systemRoleResourceDTO);
                    return systemRoleResourceDTO;
                }).collect(Collectors.toList());
                systemResourceDTO.setSystemRoleResourceList(systemRoleResourceList);
                // 继续追查
                if (systemRoleResourceList != null && systemRoleResourceList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("systemRoleResourceList.")) {
                            String associationName2 = associationName.substring("systemRoleResourceList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria systemRoleResourceCriteria = new BaseCriteria();
                    systemRoleResourceCriteria.setAssociationNameList(associationName2List);
                    for (SystemRoleResourceDTO systemRoleResourceDTO : systemRoleResourceList) {
                        systemRoleResourceService.getAssociations(systemRoleResourceDTO, systemRoleResourceCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemUserResourceList")) {
                // 获取用户列表
                List<SystemUserResourceDTO> systemUserResourceList = systemUserResourceMapper.selectList(
                        new QueryWrapper<SystemUserResource>().eq("system_resource_id", systemResourceDTO.getId())
                ).stream().map(systemUserResource -> {
                    SystemUserResourceDTO systemUserResourceDTO = new SystemUserResourceDTO();
                    MyBeanUtil.copyNonNullProperties(systemUserResource, systemUserResourceDTO);
                    return systemUserResourceDTO;
                }).collect(Collectors.toList());
                systemResourceDTO.setSystemUserResourceList(systemUserResourceList);
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
            if (associationNameList.contains("parent")) {
			    // 获取父资源
                Long parentId = systemResourceDTO.getParentId();
                if (parentId == null) {
                    return systemResourceDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("parent.")) {
                        String associationName2 = associationName.substring("parent.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria parentCriteria = new BaseCriteria();
                parentCriteria.setAssociationNameList(associationName2List);
                Optional<SystemResourceDTO> parentOptional = parentService.findOne(parentId, parentCriteria);
                systemResourceDTO.setParent(parentOptional.isPresent() ? parentOptional.get() : null);
            }
        }
        return systemResourceDTO;
    }

}
