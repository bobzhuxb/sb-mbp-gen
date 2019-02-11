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
public class SmMeetingServiceImpl extends ServiceImpl<SmMeetingMapper, SmMeeting> implements SmMeetingService {

    private final Logger log = LoggerFactory.getLogger(SmMeetingServiceImpl.class);

    @Autowired
    private SmMeetingDetailMapper smMeetingDetailMapper;

    @Autowired
    private SmMeetingScheduleMapper smMeetingScheduleMapper;

    @Autowired
    private SmMeetingGuestMapper smMeetingGuestMapper;

    @Autowired
    private SmPersonService organizerService;

    @Autowired
    private SmPersonService contractorService;

    @Autowired
    private SmMeetingDetailService smMeetingDetailService;

    @Autowired
    private SmMeetingScheduleService smMeetingScheduleService;

    @Autowired
    private SmMeetingGuestService smMeetingGuestService;

    /**
     * 新增或更新
     * @param smMeetingDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(SmMeetingDTO smMeetingDTO) {
        log.debug("Service ==> 新增或修改SmMeeting {}", smMeetingDTO);
		if (smMeetingDTO.getId() != null) {
			// 修改
			long smMeetingId = smMeetingDTO.getId();
			if (smMeetingDTO.getSmMeetingDetail() != null) {
			    if (smMeetingDTO.getSmMeetingDetail().getId() == null) {
				    // 删除会议详情
				    smMeetingDetailService.deleteByMapCascade(new HashMap<String, Object>() {{
					    put("sm_meeting_id", smMeetingId);
				    }});
                }
			}
			if (smMeetingDTO.getSmMeetingScheduleList() != null) {
				// 清空会议日程列表
				smMeetingScheduleService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("sm_meeting_id", smMeetingId);
				}});
			}
			if (smMeetingDTO.getSmMeetingGuestList() != null) {
				// 清空会议嘉宾列表
				smMeetingGuestService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("sm_meeting_id", smMeetingId);
				}});
			}
		}
        // 新增或更新会议（当前实体）
        SmMeeting smMeeting = new SmMeeting();
        MyBeanUtil.copyNonNullProperties(smMeetingDTO, smMeeting);
        if (smMeeting.getId() == null) {
            // 新增
            smMeeting.setInsertUserId(smMeetingDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(smMeeting);
        long smMeetingId = smMeeting.getId();
        // 需要级联保存的属性
        if (smMeetingDTO.getSmMeetingDetail() != null) {
            // 新增或更新会议详情
            SmMeetingDetailDTO smMeetingDetailDTO = smMeetingDTO.getSmMeetingDetail();
            smMeetingDetailDTO.setSmMeetingId(smMeetingId);
            if (smMeetingDetailDTO.getId() == null) {
                // 新增
                smMeetingDetailDTO.setInsertUserId(smMeetingDTO.getOperateUserId());
            }
            smMeetingDetailDTO.setOperateUserId(smMeetingDTO.getOperateUserId());
            smMeetingDetailDTO.setInsertTime(smMeetingDTO.getInsertTime());
            smMeetingDetailDTO.setUpdateTime(smMeetingDTO.getUpdateTime());
			smMeetingDetailService.save(smMeetingDetailDTO);
        }
        if (smMeetingDTO.getSmMeetingScheduleList() != null) {
            // 新增会议日程
            for (SmMeetingScheduleDTO smMeetingScheduleDTO : smMeetingDTO.getSmMeetingScheduleList()) {
                smMeetingScheduleDTO.setId(null);
                smMeetingScheduleDTO.setSmMeetingId(smMeetingId);
                smMeetingScheduleDTO.setInsertUserId(smMeetingDTO.getOperateUserId());
                smMeetingScheduleDTO.setOperateUserId(smMeetingDTO.getOperateUserId());
                smMeetingScheduleDTO.setInsertTime(smMeetingDTO.getInsertTime());
                smMeetingScheduleDTO.setUpdateTime(smMeetingDTO.getUpdateTime());
                smMeetingScheduleService.save(smMeetingScheduleDTO);
			}
        }
        if (smMeetingDTO.getSmMeetingGuestList() != null) {
            // 新增会议嘉宾
            for (SmMeetingGuestDTO smMeetingGuestDTO : smMeetingDTO.getSmMeetingGuestList()) {
                smMeetingGuestDTO.setId(null);
                smMeetingGuestDTO.setSmMeetingId(smMeetingId);
                smMeetingGuestDTO.setInsertUserId(smMeetingDTO.getOperateUserId());
                smMeetingGuestDTO.setOperateUserId(smMeetingDTO.getOperateUserId());
                smMeetingGuestDTO.setInsertTime(smMeetingDTO.getInsertTime());
                smMeetingGuestDTO.setUpdateTime(smMeetingDTO.getUpdateTime());
                smMeetingGuestService.save(smMeetingGuestDTO);
			}
        }
        smMeetingDTO.setId(smMeetingId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SmMeetingDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SmMeetingDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(smMeeting -> {
            // 删除级联的会议详情
            smMeetingDetailService.deleteByMapCascade(new HashMap<String, Object>() {{put("sm_meeting_id", smMeeting.getId());}});
            // 有级联的会议日程则不允许删除
            int smMeetingScheduleCount = smMeetingScheduleMapper.selectCount(
                    new QueryWrapper<SmMeetingSchedule>().eq("sm_meeting_id", smMeeting.getId()));
            if (smMeetingScheduleCount > 0) {
                throw new CommonException("有存在的会议日程，无法删除。");
            }
            // 删除级联的会议嘉宾
            smMeetingGuestService.deleteByMapCascade(new HashMap<String, Object>() {{put("sm_meeting_id", smMeeting.getId());}});
        });
        // 根据指定条件删除会议数据
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
    public Optional<SmMeetingDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmMeetingDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmMeeting> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smMeeting -> doConvert(smMeeting, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmMeetingDTO> findAll(SmMeetingCriteria criteria) {
        log.debug("Service ==> 查询所有SmMeetingDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(null, criteria, SmMeeting.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(smMeeting -> doConvert(smMeeting, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SmMeetingDTO> findPage(SmMeetingCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SmMeetingDTO {}, {}", criteria, pageable);
        Page<SmMeeting> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(null, criteria, SmMeeting.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmMeetingDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(smMeeting -> doConvert(smMeeting, criteria));
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
    public int findCount(SmMeetingCriteria criteria) {
        log.debug("Service ==> 查询个数SmMeetingDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(null, criteria, SmMeeting.class, null, tableIndexMap);
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
    private Wrapper<SmMeeting> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmMeetingCriteria smMeetingCriteria = new SmMeetingCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smMeetingCriteria);
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(null, smMeetingCriteria, SmMeeting.class, null, null);
        ((QueryWrapper<SmMeeting>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SmMeeting> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SmMeetingCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SmMeeting.getTableName() + "_" + tableCount + ".*";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
            if (dictionaryNameList.contains("ksValue")) {
                joinDataSql += " ,base_dictionary_ks_code_" + tableCount + ".dic_value AS ks_value";
            }
        }
        joinDataSql += getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @return
     */
    private String getCountQuerySql(SmMeetingCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SmMeetingCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SmMeeting.getTableName() + " AS " + SmMeeting.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SmMeetingCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
            if (dictionaryNameList.contains("ksValue")) {
                joinSubSql += " LEFT JOIN base_dictionary AS base_dictionary_ks_code_" + tableCount + " ON "
                        + "base_dictionary_ks_code_" + tableCount + ".dic_type = 'KESHI' AND "
                        + "base_dictionary_ks_code_" + tableCount + ".dic_code = " + SmMeeting.getTableName() + "_" + fromTableCount
                        + ".ks_code";
            }
        }
        if (criteria.getOrganizer() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SmPerson.getTableName() + " AS " + SmPerson.getTableName() + "_" + tableCount + " ON "
                    + SmPerson.getTableName() + "_" + tableCount + ".id = " + SmMeeting.getTableName() + "_" + fromTableCount
                    + ".organizer_id";
            String tableKey = "organizer";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += organizerService.getJoinSql(criteria.getOrganizer(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        if (criteria.getContractor() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SmPerson.getTableName() + " AS " + SmPerson.getTableName() + "_" + tableCount + " ON "
                    + SmPerson.getTableName() + "_" + tableCount + ".id = " + SmMeeting.getTableName() + "_" + fromTableCount
                    + ".contractor_id";
            String tableKey = "contractor";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += contractorService.getJoinSql(criteria.getContractor(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param smMeeting 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SmMeetingDTO doConvert(SmMeeting smMeeting, BaseCriteria criteria) {
        SmMeetingDTO smMeetingDTO = new SmMeetingDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(smMeeting, smMeetingDTO);
        getAssociations(smMeetingDTO, criteria);
        return smMeetingDTO;
    }

    /**
     * 获取关联属性
     * @param smMeetingDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SmMeetingDTO getAssociations(SmMeetingDTO smMeetingDTO, BaseCriteria criteria) {
        if (smMeetingDTO.getId() == null) {
            return smMeetingDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("smMeetingDetail")) {
                // 获取会议详情
				SmMeetingDetailDTO smMeetingDetailDTO = null;
                SmMeetingDetail smMeetingDetail = smMeetingDetailMapper.selectOne(
                        new QueryWrapper<SmMeetingDetail>().eq("sm_meeting_id", smMeetingDTO.getId())
                );
                if (smMeetingDetail != null) {
                    smMeetingDetailDTO = new SmMeetingDetailDTO();
                    MyBeanUtil.copyNonNullProperties(smMeetingDetail, smMeetingDetailDTO);
                }
                smMeetingDTO.setSmMeetingDetail(smMeetingDetailDTO);
                // 继续追查
                if (smMeetingDetailDTO != null) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("smMeetingDetail.")) {
                            String associationName2 = associationName.substring("smMeetingDetail.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria smMeetingDetailCriteria = new BaseCriteria();
                    smMeetingDetailCriteria.setAssociationNameList(associationName2List);
                    smMeetingDetailService.getAssociations(smMeetingDetailDTO, smMeetingDetailCriteria);
                }
            }
            if (associationNameList.contains("smMeetingScheduleList")) {
                // 获取会议日程列表
                List<SmMeetingScheduleDTO> smMeetingScheduleList = smMeetingScheduleMapper.selectList(
                        new QueryWrapper<SmMeetingSchedule>().eq("sm_meeting_id", smMeetingDTO.getId())
                ).stream().map(smMeetingSchedule -> {
                    SmMeetingScheduleDTO smMeetingScheduleDTO = new SmMeetingScheduleDTO();
                    MyBeanUtil.copyNonNullProperties(smMeetingSchedule, smMeetingScheduleDTO);
                    return smMeetingScheduleDTO;
                }).collect(Collectors.toList());
                smMeetingDTO.setSmMeetingScheduleList(smMeetingScheduleList);
                // 继续追查
                if (smMeetingScheduleList != null && smMeetingScheduleList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("smMeetingScheduleList.")) {
                            String associationName2 = associationName.substring("smMeetingScheduleList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria smMeetingScheduleCriteria = new BaseCriteria();
                    smMeetingScheduleCriteria.setAssociationNameList(associationName2List);
                    for (SmMeetingScheduleDTO smMeetingScheduleDTO : smMeetingScheduleList) {
                        smMeetingScheduleService.getAssociations(smMeetingScheduleDTO, smMeetingScheduleCriteria);
                    }
                }
            }
            if (associationNameList.contains("smMeetingGuestList")) {
                // 获取会议嘉宾列表
                List<SmMeetingGuestDTO> smMeetingGuestList = smMeetingGuestMapper.selectList(
                        new QueryWrapper<SmMeetingGuest>().eq("sm_meeting_id", smMeetingDTO.getId())
                ).stream().map(smMeetingGuest -> {
                    SmMeetingGuestDTO smMeetingGuestDTO = new SmMeetingGuestDTO();
                    MyBeanUtil.copyNonNullProperties(smMeetingGuest, smMeetingGuestDTO);
                    return smMeetingGuestDTO;
                }).collect(Collectors.toList());
                smMeetingDTO.setSmMeetingGuestList(smMeetingGuestList);
                // 继续追查
                if (smMeetingGuestList != null && smMeetingGuestList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("smMeetingGuestList.")) {
                            String associationName2 = associationName.substring("smMeetingGuestList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria smMeetingGuestCriteria = new BaseCriteria();
                    smMeetingGuestCriteria.setAssociationNameList(associationName2List);
                    for (SmMeetingGuestDTO smMeetingGuestDTO : smMeetingGuestList) {
                        smMeetingGuestService.getAssociations(smMeetingGuestDTO, smMeetingGuestCriteria);
                    }
                }
            }
            if (associationNameList.contains("organizer")) {
			    // 获取组织者
                Long organizerId = smMeetingDTO.getOrganizerId();
                if (organizerId == null) {
                    return smMeetingDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("organizer.")) {
                        String associationName2 = associationName.substring("organizer.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria organizerCriteria = new BaseCriteria();
                organizerCriteria.setAssociationNameList(associationName2List);
                Optional<SmPersonDTO> organizerOptional = organizerService.findOne(organizerId, organizerCriteria);
                smMeetingDTO.setOrganizer(organizerOptional.isPresent() ? organizerOptional.get() : null);
            }
            if (associationNameList.contains("contractor")) {
			    // 获取联系人
                Long contractorId = smMeetingDTO.getContractorId();
                if (contractorId == null) {
                    return smMeetingDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("contractor.")) {
                        String associationName2 = associationName.substring("contractor.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria contractorCriteria = new BaseCriteria();
                contractorCriteria.setAssociationNameList(associationName2List);
                Optional<SmPersonDTO> contractorOptional = contractorService.findOne(contractorId, contractorCriteria);
                smMeetingDTO.setContractor(contractorOptional.isPresent() ? contractorOptional.get() : null);
            }
        }
        return smMeetingDTO;
    }

}
