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
public class SmPersonServiceImpl extends ServiceImpl<SmPersonMapper, SmPerson> implements SmPersonService {

    private final Logger log = LoggerFactory.getLogger(SmPersonServiceImpl.class);

    @Autowired
    private SmMeetingMapper asmMeetingMapper;

    @Autowired
    private SmMeetingMapper bsmMeetingMapper;

    @Autowired
    private SmMeetingService asmMeetingService;

    @Autowired
    private SmMeetingService bsmMeetingService;

    /**
     * 新增或更新
     * @param smPersonDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(SmPersonDTO smPersonDTO) {
        log.debug("Service ==> 新增或修改SmPerson {}", smPersonDTO);
		if (smPersonDTO.getId() != null) {
			// 修改
			long smPersonId = smPersonDTO.getId();
			if (smPersonDTO.getAsmMeetingList() != null) {
				// 清空会议列表
				asmMeetingService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("organizer_id", smPersonId);
				}});
			}
			if (smPersonDTO.getBsmMeetingList() != null) {
				// 清空会议列表
				bsmMeetingService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("contractor_id", smPersonId);
				}});
			}
		}
        // 新增或更新工作人员（当前实体）
        SmPerson smPerson = new SmPerson();
        MyBeanUtil.copyNonNullProperties(smPersonDTO, smPerson);
        if (smPerson.getId() == null) {
            // 新增
            smPerson.setInsertUserId(smPersonDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(smPerson);
        long smPersonId = smPerson.getId();
        // 需要级联保存的属性
        if (smPersonDTO.getAsmMeetingList() != null) {
            // 新增会议
            for (SmMeetingDTO asmMeetingDTO : smPersonDTO.getAsmMeetingList()) {
                asmMeetingDTO.setId(null);
                asmMeetingDTO.setOrganizerId(smPersonId);
                asmMeetingDTO.setInsertUserId(smPersonDTO.getOperateUserId());
                asmMeetingDTO.setOperateUserId(smPersonDTO.getOperateUserId());
                asmMeetingDTO.setInsertTime(smPersonDTO.getInsertTime());
                asmMeetingDTO.setUpdateTime(smPersonDTO.getUpdateTime());
                asmMeetingService.save(asmMeetingDTO);
			}
        }
        if (smPersonDTO.getBsmMeetingList() != null) {
            // 新增会议
            for (SmMeetingDTO bsmMeetingDTO : smPersonDTO.getBsmMeetingList()) {
                bsmMeetingDTO.setId(null);
                bsmMeetingDTO.setContractorId(smPersonId);
                bsmMeetingDTO.setInsertUserId(smPersonDTO.getOperateUserId());
                bsmMeetingDTO.setOperateUserId(smPersonDTO.getOperateUserId());
                bsmMeetingDTO.setInsertTime(smPersonDTO.getInsertTime());
                bsmMeetingDTO.setUpdateTime(smPersonDTO.getUpdateTime());
                bsmMeetingService.save(bsmMeetingDTO);
			}
        }
        smPersonDTO.setId(smPersonId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SmPersonDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SmPersonDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(smPerson -> {
            // 会议的organizer_id列级联置空
            asmMeetingMapper.organizerIdCascadeToNull(smPerson.getId());
            // 会议的contractor_id列级联置空
            bsmMeetingMapper.contractorIdCascadeToNull(smPerson.getId());
        });
        // 根据指定条件删除工作人员数据
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
    public Optional<SmPersonDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmPersonDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmPerson> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smPerson -> doConvert(smPerson, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmPersonDTO> findAll(SmPersonCriteria criteria) {
        log.debug("Service ==> 查询所有SmPersonDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SmPerson> wrapper = MbpUtil.getWrapper(null, criteria, SmPerson.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(smPerson -> doConvert(smPerson, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SmPersonDTO> findPage(SmPersonCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SmPersonDTO {}, {}", criteria, pageable);
        Page<SmPerson> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmPerson> wrapper = MbpUtil.getWrapper(null, criteria, SmPerson.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmPersonDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(smPerson -> doConvert(smPerson, criteria));
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
    public int findCount(SmPersonCriteria criteria) {
        log.debug("Service ==> 查询个数SmPersonDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmPerson> wrapper = MbpUtil.getWrapper(null, criteria, SmPerson.class, null, tableIndexMap);
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
    private Wrapper<SmPerson> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmPersonCriteria smPersonCriteria = new SmPersonCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smPersonCriteria);
        Wrapper<SmPerson> wrapper = MbpUtil.getWrapper(null, smPersonCriteria, SmPerson.class, null, null);
        ((QueryWrapper<SmPerson>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SmPerson> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SmPersonCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SmPerson.getTableName() + "_" + tableCount + ".*";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
            if (dictionaryNameList.contains("nationValue")) {
                joinDataSql += " ,base_dictionary_nation_code_" + tableCount + ".dic_value AS nation_value";
            }
            if (dictionaryNameList.contains("countryValue")) {
                joinDataSql += " ,base_dictionary_country_code_" + tableCount + ".dic_value AS country_value";
            }
        }
        joinDataSql += getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @return
     */
    private String getCountQuerySql(SmPersonCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SmPersonCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SmPerson.getTableName() + " AS " + SmPerson.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SmPersonCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
            if (dictionaryNameList.contains("nationValue")) {
                joinSubSql += " LEFT JOIN base_dictionary AS base_dictionary_nation_code_" + tableCount + " ON "
                        + "base_dictionary_nation_code_" + tableCount + ".dic_type = 'NATION' AND "
                        + "base_dictionary_nation_code_" + tableCount + ".dic_code = " + SmPerson.getTableName() + "_" + fromTableCount
                        + ".nation_code";
            }
            if (dictionaryNameList.contains("countryValue")) {
                joinSubSql += " LEFT JOIN base_dictionary AS base_dictionary_country_code_" + tableCount + " ON "
                        + "base_dictionary_country_code_" + tableCount + ".dic_type = 'COUNTRY' AND "
                        + "base_dictionary_country_code_" + tableCount + ".dic_code = " + SmPerson.getTableName() + "_" + fromTableCount
                        + ".country_code";
            }
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param smPerson 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SmPersonDTO doConvert(SmPerson smPerson, BaseCriteria criteria) {
        SmPersonDTO smPersonDTO = new SmPersonDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(smPerson, smPersonDTO);
        getAssociations(smPersonDTO, criteria);
        return smPersonDTO;
    }

    /**
     * 获取关联属性
     * @param smPersonDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SmPersonDTO getAssociations(SmPersonDTO smPersonDTO, BaseCriteria criteria) {
        if (smPersonDTO.getId() == null) {
            return smPersonDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("asmMeetingList")) {
                // 获取会议列表
                List<SmMeetingDTO> asmMeetingList = asmMeetingMapper.selectList(
                        new QueryWrapper<SmMeeting>().eq("organizer_id", smPersonDTO.getId())
                ).stream().map(asmMeeting -> {
                    SmMeetingDTO asmMeetingDTO = new SmMeetingDTO();
                    MyBeanUtil.copyNonNullProperties(asmMeeting, asmMeetingDTO);
                    return asmMeetingDTO;
                }).collect(Collectors.toList());
                smPersonDTO.setAsmMeetingList(asmMeetingList);
                // 继续追查
                if (asmMeetingList != null && asmMeetingList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("asmMeetingList.")) {
                            String associationName2 = associationName.substring("asmMeetingList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria asmMeetingCriteria = new BaseCriteria();
                    asmMeetingCriteria.setAssociationNameList(associationName2List);
                    for (SmMeetingDTO asmMeetingDTO : asmMeetingList) {
                        asmMeetingService.getAssociations(asmMeetingDTO, asmMeetingCriteria);
                    }
                }
            }
            if (associationNameList.contains("bsmMeetingList")) {
                // 获取会议列表
                List<SmMeetingDTO> bsmMeetingList = bsmMeetingMapper.selectList(
                        new QueryWrapper<SmMeeting>().eq("contractor_id", smPersonDTO.getId())
                ).stream().map(bsmMeeting -> {
                    SmMeetingDTO bsmMeetingDTO = new SmMeetingDTO();
                    MyBeanUtil.copyNonNullProperties(bsmMeeting, bsmMeetingDTO);
                    return bsmMeetingDTO;
                }).collect(Collectors.toList());
                smPersonDTO.setBsmMeetingList(bsmMeetingList);
                // 继续追查
                if (bsmMeetingList != null && bsmMeetingList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("bsmMeetingList.")) {
                            String associationName2 = associationName.substring("bsmMeetingList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria bsmMeetingCriteria = new BaseCriteria();
                    bsmMeetingCriteria.setAssociationNameList(associationName2List);
                    for (SmMeetingDTO bsmMeetingDTO : bsmMeetingList) {
                        bsmMeetingService.getAssociations(bsmMeetingDTO, bsmMeetingCriteria);
                    }
                }
            }
        }
        return smPersonDTO;
    }

}
