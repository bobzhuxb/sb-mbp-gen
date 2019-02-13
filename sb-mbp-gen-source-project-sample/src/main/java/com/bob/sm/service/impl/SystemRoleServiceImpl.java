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
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService,
        BaseService<SystemRole> {

    private final Logger log = LoggerFactory.getLogger(SystemRoleServiceImpl.class);

    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private SystemRoleResourceMapper systemRoleResourceMapper;

    @Autowired
    private SystemUserRoleService systemUserRoleService;

    @Autowired
    private SystemRoleResourceService systemRoleResourceService;

    /**
     * 新增或更新
     * @param systemRoleDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemRoleDTO systemRoleDTO) {
        log.debug("Service ==> 新增或修改SystemRole {}", systemRoleDTO);
		if (systemRoleDTO.getId() != null) {
			// 修改
			long systemRoleId = systemRoleDTO.getId();
			if (systemRoleDTO.getSystemUserRoleList() != null) {
				// 清空用户列表
				systemUserRoleService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_role_id", systemRoleId);
				}});
			}
			if (systemRoleDTO.getSystemRoleResourceList() != null) {
				// 清空资源列表
				systemRoleResourceService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("system_role_id", systemRoleId);
				}});
			}
		}
        // 新增或更新角色（当前实体）
        SystemRole systemRole = new SystemRole();
        MyBeanUtil.copyNonNullProperties(systemRoleDTO, systemRole);
        if (systemRole.getId() == null) {
            // 新增
            systemRole.setInsertUserId(systemRoleDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemRole);
        long systemRoleId = systemRole.getId();
        // 需要级联保存的属性
        if (systemRoleDTO.getSystemUserRoleList() != null) {
            // 新增用户
            for (SystemUserRoleDTO systemUserRoleDTO : systemRoleDTO.getSystemUserRoleList()) {
                systemUserRoleDTO.setId(null);
                systemUserRoleDTO.setSystemRoleId(systemRoleId);
                systemUserRoleDTO.setInsertUserId(systemRoleDTO.getOperateUserId());
                systemUserRoleDTO.setOperateUserId(systemRoleDTO.getOperateUserId());
                systemUserRoleDTO.setInsertTime(systemRoleDTO.getInsertTime());
                systemUserRoleDTO.setUpdateTime(systemRoleDTO.getUpdateTime());
                systemUserRoleService.save(systemUserRoleDTO);
			}
        }
        if (systemRoleDTO.getSystemRoleResourceList() != null) {
            // 新增资源
            for (SystemRoleResourceDTO systemRoleResourceDTO : systemRoleDTO.getSystemRoleResourceList()) {
                systemRoleResourceDTO.setId(null);
                systemRoleResourceDTO.setSystemRoleId(systemRoleId);
                systemRoleResourceDTO.setInsertUserId(systemRoleDTO.getOperateUserId());
                systemRoleResourceDTO.setOperateUserId(systemRoleDTO.getOperateUserId());
                systemRoleResourceDTO.setInsertTime(systemRoleDTO.getInsertTime());
                systemRoleResourceDTO.setUpdateTime(systemRoleDTO.getUpdateTime());
                systemRoleResourceService.save(systemRoleResourceDTO);
			}
        }
        systemRoleDTO.setId(systemRoleId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemRoleDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemRoleDTO {}", idList);
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
        log.debug("Service ==> 根据指定Map删除SystemRoleDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(systemRole -> {
            // 删除级联的用户
            systemUserRoleService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_role_id", systemRole.getId());}});
            // 删除级联的资源
            systemRoleResourceService.deleteByMapCascade(new HashMap<String, Object>() {{put("system_role_id", systemRole.getId());}});
        });
        // 根据指定条件删除角色数据
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
    public Optional<SystemRoleDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemRoleDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemRole> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemRole -> doConvert(systemRole, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemRoleDTO> findAll(SystemRoleCriteria criteria) {
        log.debug("Service ==> 查询所有SystemRoleDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemRole.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemRole -> doConvert(systemRole, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemRoleDTO> findPage(SystemRoleCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemRoleDTO {}, {}", criteria, pageable);
        Page<SystemRole> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemRole.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemRoleDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemRole -> doConvert(systemRole, criteria));
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
    public int findCount(SystemRoleCriteria criteria) {
        log.debug("Service ==> 查询个数SystemRoleDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemRole> wrapper = MbpUtil.getWrapper(null, criteria, SystemRole.class, null, tableIndexMap, this);
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
    public Wrapper<SystemRole> wrapperEnhance(QueryWrapper<SystemRole> wrapper, String tableAliasName,
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
    private Wrapper<SystemRole> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemRoleCriteria systemRoleCriteria = new SystemRoleCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemRoleCriteria);
        Wrapper<SystemRole> wrapper = MbpUtil.getWrapper(null, systemRoleCriteria, SystemRole.class, null, null, this);
        ((QueryWrapper<SystemRole>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemRole> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemRoleCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemRole.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SystemRoleCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemRoleCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemRole.getTableName() + " AS " + SystemRole.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemRoleCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemRole 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemRoleDTO doConvert(SystemRole systemRole, BaseCriteria criteria) {
        SystemRoleDTO systemRoleDTO = new SystemRoleDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemRole, systemRoleDTO);
        getAssociations(systemRoleDTO, criteria);
        return systemRoleDTO;
    }

    /**
     * 获取关联属性
     * @param systemRoleDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemRoleDTO getAssociations(SystemRoleDTO systemRoleDTO, BaseCriteria criteria) {
        if (systemRoleDTO.getId() == null) {
            return systemRoleDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("systemUserRoleList")) {
                // 获取用户列表
                List<SystemUserRoleDTO> systemUserRoleList = systemUserRoleMapper.selectList(
                        new QueryWrapper<SystemUserRole>().eq("system_role_id", systemRoleDTO.getId())
                ).stream().map(systemUserRole -> {
                    SystemUserRoleDTO systemUserRoleDTO = new SystemUserRoleDTO();
                    MyBeanUtil.copyNonNullProperties(systemUserRole, systemUserRoleDTO);
                    return systemUserRoleDTO;
                }).collect(Collectors.toList());
                systemRoleDTO.setSystemUserRoleList(systemUserRoleList);
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
            if (associationNameList.contains("systemRoleResourceList")) {
                // 获取资源列表
                List<SystemRoleResourceDTO> systemRoleResourceList = systemRoleResourceMapper.selectList(
                        new QueryWrapper<SystemRoleResource>().eq("system_role_id", systemRoleDTO.getId())
                ).stream().map(systemRoleResource -> {
                    SystemRoleResourceDTO systemRoleResourceDTO = new SystemRoleResourceDTO();
                    MyBeanUtil.copyNonNullProperties(systemRoleResource, systemRoleResourceDTO);
                    return systemRoleResourceDTO;
                }).collect(Collectors.toList());
                systemRoleDTO.setSystemRoleResourceList(systemRoleResourceList);
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
        }
        return systemRoleDTO;
    }

}
