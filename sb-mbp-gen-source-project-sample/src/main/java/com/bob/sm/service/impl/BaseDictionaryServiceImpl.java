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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy
@Transactional
public class BaseDictionaryServiceImpl extends ServiceImpl<BaseDictionaryMapper, BaseDictionary> implements BaseDictionaryService {

    private final Logger log = LoggerFactory.getLogger(BaseDictionaryServiceImpl.class);

    @Autowired
    private BaseDictionaryMapper childMapper;

    @Autowired
    private BaseDictionaryService parentService;

    @Autowired
    private BaseDictionaryService childService;

    @Autowired
	private SmPersonMapper smPersonMapper;

    @Autowired
	private SmMeetingMapper smMeetingMapper;

    /**
     * 新增或更新
     * @param baseDictionaryDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(BaseDictionaryDTO baseDictionaryDTO) {
        log.debug("Service ==> 新增或修改BaseDictionary {}", baseDictionaryDTO);
		if (baseDictionaryDTO.getId() != null) {
			// 修改
			long baseDictionaryId = baseDictionaryDTO.getId();
			if (baseDictionaryDTO.getChildList() != null) {
				// 清空子数据字典列表
				childService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("parent_id", baseDictionaryId);
				}});
			}
		}
        // 新增或更新数据字典（当前实体）
        BaseDictionary baseDictionary = new BaseDictionary();
        MyBeanUtil.copyNonNullProperties(baseDictionaryDTO, baseDictionary);
        if (baseDictionary.getId() == null) {
            // 新增
            baseDictionary.setInsertUserId(baseDictionaryDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(baseDictionary);
        long baseDictionaryId = baseDictionary.getId();
        // 需要级联保存的属性
        if (baseDictionaryDTO.getChildList() != null) {
            // 新增子数据字典
            for (BaseDictionaryDTO childDTO : baseDictionaryDTO.getChildList()) {
                childDTO.setId(null);
                childDTO.setParentId(baseDictionaryId);
                childDTO.setInsertUserId(baseDictionaryDTO.getOperateUserId());
                childDTO.setOperateUserId(baseDictionaryDTO.getOperateUserId());
                childDTO.setInsertTime(baseDictionaryDTO.getInsertTime());
                childDTO.setUpdateTime(baseDictionaryDTO.getUpdateTime());
                childService.save(childDTO);
			}
        }
        baseDictionaryDTO.setId(baseDictionaryId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除BaseDictionaryDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除BaseDictionaryDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(baseDictionary -> {
            // 判断是否有使用该数据字典的数据
            if ("NATION".equals(baseDictionary.getDicType())) {
                int smPersonCount = smPersonMapper.selectCount(
                        new QueryWrapper<SmPerson>().eq("nation_code", baseDictionary.getDicCode()));
                if (smPersonCount > 0) {
                    throw new CommonException("有使用该数据字典的工作人员信息，无法删除。");
                }
            }
            if ("COUNTRY".equals(baseDictionary.getDicType())) {
                int smPersonCount = smPersonMapper.selectCount(
                        new QueryWrapper<SmPerson>().eq("country_code", baseDictionary.getDicCode()));
                if (smPersonCount > 0) {
                    throw new CommonException("有使用该数据字典的工作人员信息，无法删除。");
                }
            }
            if ("KESHI".equals(baseDictionary.getDicType())) {
                int smMeetingCount = smMeetingMapper.selectCount(
                        new QueryWrapper<SmMeeting>().eq("ks_code", baseDictionary.getDicCode()));
                if (smMeetingCount > 0) {
                    throw new CommonException("有使用该数据字典的会议信息，无法删除。");
                }
            }
            // 删除级联的子数据字典
            childService.deleteByMapCascade(new HashMap<String, Object>() {{put("parent_id", baseDictionary.getId());}});
        });
        // 根据指定条件删除数据字典数据
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
    public Optional<BaseDictionaryDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询BaseDictionaryDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<BaseDictionary> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(baseDictionary -> doConvert(baseDictionary, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<BaseDictionaryDTO> findAll(BaseDictionaryCriteria criteria) {
        log.debug("Service ==> 查询所有BaseDictionaryDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<BaseDictionary> wrapper = MbpUtil.getWrapper(null, criteria, BaseDictionary.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(baseDictionary -> doConvert(baseDictionary, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<BaseDictionaryDTO> findPage(BaseDictionaryCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询BaseDictionaryDTO {}, {}", criteria, pageable);
        Page<BaseDictionary> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<BaseDictionary> wrapper = MbpUtil.getWrapper(null, criteria, BaseDictionary.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<BaseDictionaryDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(baseDictionary -> doConvert(baseDictionary, criteria));
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
    public int findCount(BaseDictionaryCriteria criteria) {
        log.debug("Service ==> 查询个数BaseDictionaryDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<BaseDictionary> wrapper = MbpUtil.getWrapper(null, criteria, BaseDictionary.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return 0;
        }
        return baseMapper.joinSelectCount(countQuerySql, wrapper);
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param criteria 附加条件
     * @return 查询通用Wrapper
     */
    private Wrapper<BaseDictionary> idEqualsPrepare(Long id, BaseCriteria criteria) {
        BaseDictionaryCriteria baseDictionaryCriteria = new BaseDictionaryCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, baseDictionaryCriteria);
        Wrapper<BaseDictionary> wrapper = MbpUtil.getWrapper(null, baseDictionaryCriteria, BaseDictionary.class, null, null);
        ((QueryWrapper<BaseDictionary>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<BaseDictionary> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(BaseDictionaryCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + BaseDictionary.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(BaseDictionaryCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(BaseDictionaryCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + BaseDictionary.getTableName() + " AS " + BaseDictionary.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(BaseDictionaryCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getParent() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + BaseDictionary.getTableName() + " AS " + BaseDictionary.getTableName() + "_" + tableCount + " ON "
                    + BaseDictionary.getTableName() + "_" + tableCount + ".id = " + BaseDictionary.getTableName() + "_" + fromTableCount
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
     * @param baseDictionary 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private BaseDictionaryDTO doConvert(BaseDictionary baseDictionary, BaseCriteria criteria) {
        BaseDictionaryDTO baseDictionaryDTO = new BaseDictionaryDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(baseDictionary, baseDictionaryDTO);
        getAssociations(baseDictionaryDTO, criteria);
        return baseDictionaryDTO;
    }

    /**
     * 获取关联属性
     * @param baseDictionaryDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public BaseDictionaryDTO getAssociations(BaseDictionaryDTO baseDictionaryDTO, BaseCriteria criteria) {
        if (baseDictionaryDTO.getId() == null) {
            return baseDictionaryDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("childList")) {
                // 获取子数据字典列表
                List<BaseDictionaryDTO> childList = childMapper.selectList(
                        new QueryWrapper<BaseDictionary>().eq("parent_id", baseDictionaryDTO.getId())
                ).stream().map(child -> {
                    BaseDictionaryDTO childDTO = new BaseDictionaryDTO();
                    MyBeanUtil.copyNonNullProperties(child, childDTO);
                    return childDTO;
                }).collect(Collectors.toList());
                baseDictionaryDTO.setChildList(childList);
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
                    for (BaseDictionaryDTO childDTO : childList) {
                        childService.getAssociations(childDTO, childCriteria);
                    }
                }
            }
            if (associationNameList.contains("parent")) {
			    // 获取父数据字典
                Long parentId = baseDictionaryDTO.getParentId();
                if (parentId == null) {
                    return baseDictionaryDTO;
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
                Optional<BaseDictionaryDTO> parentOptional = parentService.findOne(parentId, parentCriteria);
                baseDictionaryDTO.setParent(parentOptional.isPresent() ? parentOptional.get() : null);
            }
        }
        return baseDictionaryDTO;
    }

}
