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
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements SystemUserRoleService,
        BaseService<SystemUserRole> {

    private final Logger log = LoggerFactory.getLogger(SystemUserRoleServiceImpl.class);

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 新增或更新
     * @param systemUserRoleDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemUserRoleDTO systemUserRoleDTO) {
        log.debug("Service ==> 新增或修改SystemUserRole {}", systemUserRoleDTO);
		if (systemUserRoleDTO.getId() != null) {
			// 修改
			long systemUserRoleId = systemUserRoleDTO.getId();
		}
        // 新增或更新用户角色关系（当前实体）
        SystemUserRole systemUserRole = new SystemUserRole();
        MyBeanUtil.copyNonNullProperties(systemUserRoleDTO, systemUserRole);
        if (systemUserRole.getId() == null) {
            // 新增
            systemUserRole.setInsertUserId(systemUserRoleDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemUserRole);
        long systemUserRoleId = systemUserRole.getId();
        systemUserRoleDTO.setId(systemUserRoleId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemUserRoleDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemUserRoleDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemUserRoleDTO {}", columnMap);
        // 根据指定条件删除用户角色关系数据
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
    public Optional<SystemUserRoleDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemUserRoleDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemUserRole> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemUserRole -> doConvert(systemUserRole, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemUserRoleDTO> findAll(SystemUserRoleCriteria criteria) {
        log.debug("Service ==> 查询所有SystemUserRoleDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserRole.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemUserRole -> doConvert(systemUserRole, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemUserRoleDTO> findPage(SystemUserRoleCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemUserRoleDTO {}, {}", criteria, pageable);
        Page<SystemUserRole> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserRole.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemUserRoleDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemUserRole -> doConvert(systemUserRole, criteria));
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
    public int findCount(SystemUserRoleCriteria criteria) {
        log.debug("Service ==> 查询个数SystemUserRoleDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemUserRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemUserRole.class, null, tableIndexMap, this);
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
    public Wrapper<SystemUserRole> wrapperEnhance(QueryWrapper<SystemUserRole> wrapper, String tableAliasName,
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
    private Wrapper<SystemUserRole> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemUserRoleCriteria systemUserRoleCriteria = new SystemUserRoleCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemUserRoleCriteria);
        Wrapper<SystemUserRole> wrapper = MbpUtil.getWrapper(null, systemUserRoleCriteria, SystemUserRole.class, null, null, this);
        ((QueryWrapper<SystemUserRole>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemUserRole> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemUserRoleCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemUserRole.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemUserRoleCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemUserRoleCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemUserRole.getTableName() + " AS " + SystemUserRole.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemUserRoleCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
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
                    + SystemUser.getTableName() + "_" + tableCount + ".id = " + SystemUserRole.getTableName() + "_" + fromTableCount
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
        if (criteria.getSystemRole() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SystemRole.getTableName() + " AS " + SystemRole.getTableName() + "_" + tableCount + " ON "
                    + SystemRole.getTableName() + "_" + tableCount + ".id = " + SystemUserRole.getTableName() + "_" + fromTableCount
                    + ".system_role_id";
            String tableKey = "systemRole";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += systemRoleService.getJoinSql(criteria.getSystemRole(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemUserRole 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemUserRoleDTO doConvert(SystemUserRole systemUserRole, BaseCriteria criteria) {
        SystemUserRoleDTO systemUserRoleDTO = new SystemUserRoleDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemUserRole, systemUserRoleDTO);
        getAssociations(systemUserRoleDTO, criteria);
        return systemUserRoleDTO;
    }

    /**
     * 获取关联属性
     * @param systemUserRoleDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemUserRoleDTO getAssociations(SystemUserRoleDTO systemUserRoleDTO, BaseCriteria criteria) {
        if (systemUserRoleDTO.getId() == null) {
            return systemUserRoleDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("systemUser")) {
			    // 获取用户
                Long systemUserId = systemUserRoleDTO.getSystemUserId();
                if (systemUserId == null) {
                    return systemUserRoleDTO;
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
                systemUserRoleDTO.setSystemUser(systemUserOptional.isPresent() ? systemUserOptional.get() : null);
            }
            if (associationNameList.contains("systemRole")) {
			    // 获取角色
                Long systemRoleId = systemUserRoleDTO.getSystemRoleId();
                if (systemRoleId == null) {
                    return systemUserRoleDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("systemRole.")) {
                        String associationName2 = associationName.substring("systemRole.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria systemRoleCriteria = new BaseCriteria();
                systemRoleCriteria.setAssociationNameList(associationName2List);
                Optional<SystemRoleDTO> systemRoleOptional = systemRoleService.findOne(systemRoleId, systemRoleCriteria);
                systemUserRoleDTO.setSystemRole(systemRoleOptional.isPresent() ? systemRoleOptional.get() : null);
            }
        }
        return systemUserRoleDTO;
    }

}
