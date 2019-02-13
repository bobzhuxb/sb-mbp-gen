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
public class SystemPermissionServiceImpl extends ServiceImpl<SystemPermissionMapper, SystemPermission> implements SystemPermissionService,
        BaseService<SystemPermission> {

    private final Logger log = LoggerFactory.getLogger(SystemPermissionServiceImpl.class);

    @Autowired
    private SystemPermissionMapper childMapper;

    @Autowired
    private SystemResourcePermissionMapper systemResourcePermissionMapper;

    @Autowired
    private SystemPermissionService parentService;

    @Autowired
    private SystemPermissionService childService;

    @Autowired
    private SystemResourcePermissionService systemResourcePermissionService;

    /**
     * 新增或更新
     * @param systemPermissionDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemPermissionDTO systemPermissionDTO) {
        log.debug("Service ==> 新增或修改SystemPermission {}", systemPermissionDTO);
		if (systemPermissionDTO.getId() != null) {
			// 修改
			long systemPermissionId = systemPermissionDTO.getId();
			if (systemPermissionDTO.getChildList() != null) {
				// 清空子操作许可列表
				childService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("parent_id", systemPermissionId);
				}});
			}
			if (systemPermissionDTO.getSystemResourcePermissionList() != null) {
				// 清空资源列表
				systemResourcePermissionService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_permission_id", systemPermissionId);
				}});
			}
		}
        // 新增或更新操作许可（当前实体）
        SystemPermission systemPermission = new SystemPermission();
        MyBeanUtil.copyNonNullProperties(systemPermissionDTO, systemPermission);
        if (systemPermission.getId() == null) {
            // 新增
            systemPermission.setInsertUserId(systemPermissionDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemPermission);
        long systemPermissionId = systemPermission.getId();
        // 需要级联保存的属性
        if (systemPermissionDTO.getChildList() != null) {
            // 新增子操作许可
            for (SystemPermissionDTO childDTO : systemPermissionDTO.getChildList()) {
                childDTO.setId(null);
                childDTO.setParentId(systemPermissionId);
                childDTO.setInsertUserId(systemPermissionDTO.getOperateUserId());
                childDTO.setOperateUserId(systemPermissionDTO.getOperateUserId());
                childDTO.setInsertTime(systemPermissionDTO.getInsertTime());
                childDTO.setUpdateTime(systemPermissionDTO.getUpdateTime());
                childService.save(childDTO);
			}
        }
        if (systemPermissionDTO.getSystemResourcePermissionList() != null) {
            // 新增资源
            for (SystemResourcePermissionDTO systemResourcePermissionDTO : systemPermissionDTO.getSystemResourcePermissionList()) {
                systemResourcePermissionDTO.setId(null);
                systemResourcePermissionDTO.setSystemPermissionId(systemPermissionId);
                systemResourcePermissionDTO.setInsertUserId(systemPermissionDTO.getOperateUserId());
                systemResourcePermissionDTO.setOperateUserId(systemPermissionDTO.getOperateUserId());
                systemResourcePermissionDTO.setInsertTime(systemPermissionDTO.getInsertTime());
                systemResourcePermissionDTO.setUpdateTime(systemPermissionDTO.getUpdateTime());
                systemResourcePermissionService.save(systemResourcePermissionDTO);
			}
        }
        systemPermissionDTO.setId(systemPermissionId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemPermissionDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemPermissionDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemPermissionDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(systemPermission -> {
            // 删除级联的子操作许可
            childService.deleteByMapCascade(new HashMap<String, Object>() {{put("parent_id", systemPermission.getId());}});
            // 删除级联的资源
            systemResourcePermissionService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_permission_id", systemPermission.getId());}});
        });
        // 根据指定条件删除操作许可数据
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
    public Optional<SystemPermissionDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemPermissionDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemPermission> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemPermission -> doConvert(systemPermission, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemPermissionDTO> findAll(SystemPermissionCriteria criteria) {
        log.debug("Service ==> 查询所有SystemPermissionDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemPermission> wrapper = MbpUtil.getWrapper(null, criteria, SystemPermission.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemPermission -> doConvert(systemPermission, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemPermissionDTO> findPage(SystemPermissionCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemPermissionDTO {}, {}", criteria, pageable);
        Page<SystemPermission> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemPermission> wrapper = MbpUtil.getWrapper(null, criteria, SystemPermission.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemPermissionDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemPermission -> doConvert(systemPermission, criteria));
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
    public int findCount(SystemPermissionCriteria criteria) {
        log.debug("Service ==> 查询个数SystemPermissionDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemPermission> wrapper = MbpUtil.getWrapper(null, criteria, SystemPermission.class, null, tableIndexMap, this);
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
    public Wrapper<SystemPermission> wrapperEnhance(QueryWrapper<SystemPermission> wrapper, String tableAliasName,
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
    private Wrapper<SystemPermission> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemPermissionCriteria systemPermissionCriteria = new SystemPermissionCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemPermissionCriteria);
        Wrapper<SystemPermission> wrapper = MbpUtil.getWrapper(null, systemPermissionCriteria, SystemPermission.class, null, null, this);
        ((QueryWrapper<SystemPermission>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemPermission> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemPermissionCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemPermission.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemPermissionCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemPermissionCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemPermission.getTableName() + " AS " + SystemPermission.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemPermissionCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getParent() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemPermission.getTableName() + " AS " + SystemPermission.getTableName() + "_" + tableCount + " ON "
                    + SystemPermission.getTableName() + "_" + tableCount + ".id = " + SystemPermission.getTableName() + "_" + fromTableCount
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
     * @param systemPermission 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemPermissionDTO doConvert(SystemPermission systemPermission, BaseCriteria criteria) {
        SystemPermissionDTO systemPermissionDTO = new SystemPermissionDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemPermission, systemPermissionDTO);
        getAssociations(systemPermissionDTO, criteria);
        return systemPermissionDTO;
    }

    /**
     * 获取关联属性
     * @param systemPermissionDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemPermissionDTO getAssociations(SystemPermissionDTO systemPermissionDTO, BaseCriteria criteria) {
        if (systemPermissionDTO.getId() == null) {
            return systemPermissionDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("childList")) {
                // 获取子操作许可列表
                List<SystemPermissionDTO> childList = childMapper.selectList(
                        new QueryWrapper<SystemPermission>().eq("parent_id", systemPermissionDTO.getId())
                ).stream().map(child -> {
                    SystemPermissionDTO childDTO = new SystemPermissionDTO();
                    MyBeanUtil.copyNonNullProperties(child, childDTO);
                    return childDTO;
                }).collect(Collectors.toList());
                systemPermissionDTO.setChildList(childList);
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
                    for (SystemPermissionDTO childDTO : childList) {
                        childService.getAssociations(childDTO, childCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemResourcePermissionList")) {
                // 获取资源列表
                List<SystemResourcePermissionDTO> systemResourcePermissionList = systemResourcePermissionMapper.selectList(
                        new QueryWrapper<SystemResourcePermission>().eq("system_permission_id", systemPermissionDTO.getId())
                ).stream().map(systemResourcePermission -> {
                    SystemResourcePermissionDTO systemResourcePermissionDTO = new SystemResourcePermissionDTO();
                    MyBeanUtil.copyNonNullProperties(systemResourcePermission, systemResourcePermissionDTO);
                    return systemResourcePermissionDTO;
                }).collect(Collectors.toList());
                systemPermissionDTO.setSystemResourcePermissionList(systemResourcePermissionList);
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
            if (associationNameList.contains("parent")) {
			    // 获取父操作许可
                Long parentId = systemPermissionDTO.getParentId();
                if (parentId == null) {
                    return systemPermissionDTO;
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
                Optional<SystemPermissionDTO> parentOptional = parentService.findOne(parentId, parentCriteria);
                systemPermissionDTO.setParent(parentOptional.isPresent() ? parentOptional.get() : null);
            }
        }
        return systemPermissionDTO;
    }

}
