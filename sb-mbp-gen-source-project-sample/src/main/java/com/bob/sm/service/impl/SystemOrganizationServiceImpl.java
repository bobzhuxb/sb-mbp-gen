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
public class SystemOrganizationServiceImpl extends ServiceImpl<SystemOrganizationMapper, SystemOrganization> implements SystemOrganizationService,
        BaseService<SystemOrganization> {

    private final Logger log = LoggerFactory.getLogger(SystemOrganizationServiceImpl.class);

    @Autowired
    private SystemOrganizationMapper childMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private SystemOrganizationService parentService;

    @Autowired
    private SystemOrganizationService childService;

    @Autowired
    private SystemUserService systemUserService;

    /**
     * 新增或更新
     * @param systemOrganizationDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemOrganizationDTO systemOrganizationDTO) {
        log.debug("Service ==> 新增或修改SystemOrganization {}", systemOrganizationDTO);
		if (systemOrganizationDTO.getId() != null) {
			// 修改
			long systemOrganizationId = systemOrganizationDTO.getId();
			if (systemOrganizationDTO.getChildList() != null) {
				// 清空子组织机构列表
				childService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("parent_id", systemOrganizationId);
				}});
			}
			if (systemOrganizationDTO.getSystemUserList() != null) {
				// 清空用户列表
				systemUserService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_organization_id", systemOrganizationId);
				}});
			}
		}
        // 新增或更新组织机构（当前实体）
        SystemOrganization systemOrganization = new SystemOrganization();
        MyBeanUtil.copyNonNullProperties(systemOrganizationDTO, systemOrganization);
        if (systemOrganization.getId() == null) {
            // 新增
            systemOrganization.setInsertUserId(systemOrganizationDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemOrganization);
        long systemOrganizationId = systemOrganization.getId();
        // 需要级联保存的属性
        if (systemOrganizationDTO.getChildList() != null) {
            // 新增子组织机构
            for (SystemOrganizationDTO childDTO : systemOrganizationDTO.getChildList()) {
                childDTO.setId(null);
                childDTO.setParentId(systemOrganizationId);
                childDTO.setInsertUserId(systemOrganizationDTO.getOperateUserId());
                childDTO.setOperateUserId(systemOrganizationDTO.getOperateUserId());
                childDTO.setInsertTime(systemOrganizationDTO.getInsertTime());
                childDTO.setUpdateTime(systemOrganizationDTO.getUpdateTime());
                childService.save(childDTO);
			}
        }
        if (systemOrganizationDTO.getSystemUserList() != null) {
            // 新增用户
            for (SystemUserDTO systemUserDTO : systemOrganizationDTO.getSystemUserList()) {
                systemUserDTO.setId(null);
                systemUserDTO.setSystemOrganizationId(systemOrganizationId);
                systemUserDTO.setInsertUserId(systemOrganizationDTO.getOperateUserId());
                systemUserDTO.setOperateUserId(systemOrganizationDTO.getOperateUserId());
                systemUserDTO.setInsertTime(systemOrganizationDTO.getInsertTime());
                systemUserDTO.setUpdateTime(systemOrganizationDTO.getUpdateTime());
                systemUserService.save(systemUserDTO);
			}
        }
        systemOrganizationDTO.setId(systemOrganizationId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemOrganizationDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemOrganizationDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemOrganizationDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(systemOrganization -> {
            // 删除级联的子组织机构
            childService.deleteByMapCascade(new HashMap<String, Object>() {{put("parent_id", systemOrganization.getId());}});
            // 删除级联的用户
            systemUserService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_organization_id", systemOrganization.getId());}});
        });
        // 根据指定条件删除组织机构数据
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
    public Optional<SystemOrganizationDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemOrganizationDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemOrganization> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemOrganization -> doConvert(systemOrganization, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemOrganizationDTO> findAll(SystemOrganizationCriteria criteria) {
        log.debug("Service ==> 查询所有SystemOrganizationDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemOrganization> wrapper = MbpUtil.getWrapper(null, criteria, SystemOrganization.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemOrganization -> doConvert(systemOrganization, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemOrganizationDTO> findPage(SystemOrganizationCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemOrganizationDTO {}, {}", criteria, pageable);
        Page<SystemOrganization> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemOrganization> wrapper = MbpUtil.getWrapper(null, criteria, SystemOrganization.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemOrganizationDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemOrganization -> doConvert(systemOrganization, criteria));
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
    public int findCount(SystemOrganizationCriteria criteria) {
        log.debug("Service ==> 查询个数SystemOrganizationDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemOrganization> wrapper = MbpUtil.getWrapper(null, criteria, SystemOrganization.class, null, tableIndexMap, this);
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
    public Wrapper<SystemOrganization> wrapperEnhance(QueryWrapper<SystemOrganization> wrapper, String tableAliasName,
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
    private Wrapper<SystemOrganization> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemOrganizationCriteria systemOrganizationCriteria = new SystemOrganizationCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemOrganizationCriteria);
        Wrapper<SystemOrganization> wrapper = MbpUtil.getWrapper(null, systemOrganizationCriteria, SystemOrganization.class, null, null, this);
        ((QueryWrapper<SystemOrganization>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemOrganization> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemOrganizationCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemOrganization.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemOrganizationCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemOrganizationCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemOrganization.getTableName() + " AS " + SystemOrganization.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemOrganizationCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getParent() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemOrganization.getTableName() + " AS " + SystemOrganization.getTableName() + "_" + tableCount + " ON "
                    + SystemOrganization.getTableName() + "_" + tableCount + ".id = " + SystemOrganization.getTableName() + "_" + fromTableCount
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
     * @param systemOrganization 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemOrganizationDTO doConvert(SystemOrganization systemOrganization, BaseCriteria criteria) {
        SystemOrganizationDTO systemOrganizationDTO = new SystemOrganizationDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemOrganization, systemOrganizationDTO);
        getAssociations(systemOrganizationDTO, criteria);
        return systemOrganizationDTO;
    }

    /**
     * 获取关联属性
     * @param systemOrganizationDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemOrganizationDTO getAssociations(SystemOrganizationDTO systemOrganizationDTO, BaseCriteria criteria) {
        if (systemOrganizationDTO.getId() == null) {
            return systemOrganizationDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("childList")) {
                // 获取子组织机构列表
                List<SystemOrganizationDTO> childList = childMapper.selectList(
                        new QueryWrapper<SystemOrganization>().eq("parent_id", systemOrganizationDTO.getId())
                ).stream().map(child -> {
                    SystemOrganizationDTO childDTO = new SystemOrganizationDTO();
                    MyBeanUtil.copyNonNullProperties(child, childDTO);
                    return childDTO;
                }).collect(Collectors.toList());
                systemOrganizationDTO.setChildList(childList);
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
                    for (SystemOrganizationDTO childDTO : childList) {
                        childService.getAssociations(childDTO, childCriteria);
                    }
                }
            }
            if (associationNameList.contains("systemUserList")) {
                // 获取用户列表
                List<SystemUserDTO> systemUserList = systemUserMapper.selectList(
                        new QueryWrapper<SystemUser>().eq("system_organization_id", systemOrganizationDTO.getId())
                ).stream().map(systemUser -> {
                    SystemUserDTO systemUserDTO = new SystemUserDTO();
                    MyBeanUtil.copyNonNullProperties(systemUser, systemUserDTO);
                    return systemUserDTO;
                }).collect(Collectors.toList());
                systemOrganizationDTO.setSystemUserList(systemUserList);
                // 继续追查
                if (systemUserList != null && systemUserList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("systemUserList.")) {
                            String associationName2 = associationName.substring("systemUserList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria systemUserCriteria = new BaseCriteria();
                    systemUserCriteria.setAssociationNameList(associationName2List);
                    for (SystemUserDTO systemUserDTO : systemUserList) {
                        systemUserService.getAssociations(systemUserDTO, systemUserCriteria);
                    }
                }
            }
            if (associationNameList.contains("parent")) {
			    // 获取父组织机构
                Long parentId = systemOrganizationDTO.getParentId();
                if (parentId == null) {
                    return systemOrganizationDTO;
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
                Optional<SystemOrganizationDTO> parentOptional = parentService.findOne(parentId, parentCriteria);
                systemOrganizationDTO.setParent(parentOptional.isPresent() ? parentOptional.get() : null);
            }
        }
        return systemOrganizationDTO;
    }

}
