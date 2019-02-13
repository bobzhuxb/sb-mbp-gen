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
public class SystemUserResourceServiceImpl extends ServiceImpl<SystemUserResourceMapper, SystemUserResource> implements SystemUserResourceService,
        BaseService<SystemUserResource> {

    private final Logger log = LoggerFactory.getLogger(SystemUserResourceServiceImpl.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemResourceService systemResourceService;

    /**
     * 新增或更新
     * @param systemUserResourceDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemUserResourceDTO systemUserResourceDTO) {
        log.debug("Service ==> 新增或修改SystemUserResource {}", systemUserResourceDTO);
		if (systemUserResourceDTO.getId() != null) {
			// 修改
			long systemUserResourceId = systemUserResourceDTO.getId();
		}
        // 新增或更新用户资源关系（当前实体）
        SystemUserResource systemUserResource = new SystemUserResource();
        MyBeanUtil.copyNonNullProperties(systemUserResourceDTO, systemUserResource);
        if (systemUserResource.getId() == null) {
            // 新增
            systemUserResource.setInsertUserId(systemUserResourceDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemUserResource);
        long systemUserResourceId = systemUserResource.getId();
        systemUserResourceDTO.setId(systemUserResourceId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemUserResourceDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemUserResourceDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemUserResourceDTO {}", columnMap);
        // 根据指定条件删除用户资源关系数据
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
    public Optional<SystemUserResourceDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemUserResourceDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemUserResource> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemUserResource -> doConvert(systemUserResource, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemUserResourceDTO> findAll(SystemUserResourceCriteria criteria) {
        log.debug("Service ==> 查询所有SystemUserResourceDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserResource.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemUserResource -> doConvert(systemUserResource, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemUserResourceDTO> findPage(SystemUserResourceCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemUserResourceDTO {}, {}", criteria, pageable);
        Page<SystemUserResource> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserResource.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemUserResourceDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemUserResource -> doConvert(systemUserResource, criteria));
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
    public int findCount(SystemUserResourceCriteria criteria) {
        log.debug("Service ==> 查询个数SystemUserResourceDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserResource> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserResource.class, null, tableIndexMap, this);
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
    public Wrapper<SystemUserResource> wrapperEnhance(QueryWrapper<SystemUserResource> wrapper, String tableAliasName,
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
    private Wrapper<SystemUserResource> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemUserResourceCriteria systemUserResourceCriteria = new SystemUserResourceCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemUserResourceCriteria);
        Wrapper<SystemUserResource> wrapper = MbpUtil.getWrapper(null, systemUserResourceCriteria, SystemUserResource.class, null, null, this);
        ((QueryWrapper<SystemUserResource>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemUserResource> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemUserResourceCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemUserResource.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemUserResourceCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemUserResourceCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemUserResource.getTableName() + " AS " + SystemUserResource.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemUserResourceCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getSystemUser() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemUser.getTableName() + " AS " + SystemUser.getTableName() + "_" + tableCount + " ON "
                    + SystemUser.getTableName() + "_" + tableCount + ".id = " + SystemUserResource.getTableName() + "_" + fromTableCount
                    + ".system_user_id";
            String tableKey = "systemUser";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += systemUserService.getJoinSql(criteria.getSystemUser(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        if (criteria.getSystemResource() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemResource.getTableName() + " AS " + SystemResource.getTableName() + "_" + tableCount + " ON "
                    + SystemResource.getTableName() + "_" + tableCount + ".id = " + SystemUserResource.getTableName() + "_" + fromTableCount
                    + ".system_resource_id";
            String tableKey = "systemResource";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += systemResourceService.getJoinSql(criteria.getSystemResource(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemUserResource 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemUserResourceDTO doConvert(SystemUserResource systemUserResource, BaseCriteria criteria) {
        SystemUserResourceDTO systemUserResourceDTO = new SystemUserResourceDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemUserResource, systemUserResourceDTO);
        getAssociations(systemUserResourceDTO, criteria);
        return systemUserResourceDTO;
    }

    /**
     * 获取关联属性
     * @param systemUserResourceDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemUserResourceDTO getAssociations(SystemUserResourceDTO systemUserResourceDTO, BaseCriteria criteria) {
        if (systemUserResourceDTO.getId() == null) {
            return systemUserResourceDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("systemUser")) {
			    // 获取用户
                Long systemUserId = systemUserResourceDTO.getSystemUserId();
                if (systemUserId == null) {
                    return systemUserResourceDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("systemUser.")) {
                        String associationName2 = associationName.substring("systemUser.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria systemUserCriteria = new BaseCriteria();
                systemUserCriteria.setAssociationNameList(associationName2List);
                Optional<SystemUserDTO> systemUserOptional = systemUserService.findOne(systemUserId, systemUserCriteria);
                systemUserResourceDTO.setSystemUser(systemUserOptional.isPresent() ? systemUserOptional.get() : null);
            }
            if (associationNameList.contains("systemResource")) {
			    // 获取资源
                Long systemResourceId = systemUserResourceDTO.getSystemResourceId();
                if (systemResourceId == null) {
                    return systemUserResourceDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("systemResource.")) {
                        String associationName2 = associationName.substring("systemResource.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria systemResourceCriteria = new BaseCriteria();
                systemResourceCriteria.setAssociationNameList(associationName2List);
                Optional<SystemResourceDTO> systemResourceOptional = systemResourceService.findOne(systemResourceId, systemResourceCriteria);
                systemUserResourceDTO.setSystemResource(systemResourceOptional.isPresent() ? systemResourceOptional.get() : null);
            }
        }
        return systemUserResourceDTO;
    }

}
