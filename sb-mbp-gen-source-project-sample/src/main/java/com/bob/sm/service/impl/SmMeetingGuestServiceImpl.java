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
public class SmMeetingGuestServiceImpl extends ServiceImpl<SmMeetingGuestMapper, SmMeetingGuest> implements SmMeetingGuestService {

    private final Logger log = LoggerFactory.getLogger(SmMeetingGuestServiceImpl.class);

    @Autowired
    private SmMeetingGuestPhotoMapper smMeetingGuestPhotoMapper;

    @Autowired
    private SmMeetingService smMeetingService;

    @Autowired
    private SmMeetingGuestPhotoService smMeetingGuestPhotoService;

    /**
     * 新增或更新
     * @param smMeetingGuestDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(SmMeetingGuestDTO smMeetingGuestDTO) {
        log.debug("Service ==> 新增或修改SmMeetingGuest {}", smMeetingGuestDTO);
		if (smMeetingGuestDTO.getId() != null) {
			// 修改
			long smMeetingGuestId = smMeetingGuestDTO.getId();
			if (smMeetingGuestDTO.getSmMeetingGuestPhotoList() != null) {
				// 清空嘉宾照片列表
				smMeetingGuestPhotoService.deleteByMapCascade(new HashMap<String, Object>() {{
					put("sm_meeting_guest_id", smMeetingGuestId);
				}});
			}
		}
        // 新增或更新会议嘉宾（当前实体）
        SmMeetingGuest smMeetingGuest = new SmMeetingGuest();
        MyBeanUtil.copyNonNullProperties(smMeetingGuestDTO, smMeetingGuest);
        if (smMeetingGuest.getId() == null) {
            // 新增
            smMeetingGuest.setInsertUserId(smMeetingGuestDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(smMeetingGuest);
        long smMeetingGuestId = smMeetingGuest.getId();
        // 需要级联保存的属性
        if (smMeetingGuestDTO.getSmMeetingGuestPhotoList() != null) {
            // 新增嘉宾照片
            for (SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO : smMeetingGuestDTO.getSmMeetingGuestPhotoList()) {
                smMeetingGuestPhotoDTO.setId(null);
                smMeetingGuestPhotoDTO.setSmMeetingGuestId(smMeetingGuestId);
                smMeetingGuestPhotoDTO.setInsertUserId(smMeetingGuestDTO.getOperateUserId());
                smMeetingGuestPhotoDTO.setOperateUserId(smMeetingGuestDTO.getOperateUserId());
                smMeetingGuestPhotoDTO.setInsertTime(smMeetingGuestDTO.getInsertTime());
                smMeetingGuestPhotoDTO.setUpdateTime(smMeetingGuestDTO.getUpdateTime());
                smMeetingGuestPhotoService.save(smMeetingGuestPhotoDTO);
			}
        }
        smMeetingGuestDTO.setId(smMeetingGuestId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SmMeetingGuestDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SmMeetingGuestDTO {}", columnMap);
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(smMeetingGuest -> {
            // 删除级联的嘉宾照片
            smMeetingGuestPhotoService.deleteByMapCascade(new HashMap<String, Object>() {{put("sm_meeting_guest_id", smMeetingGuest.getId());}});
        });
        // 根据指定条件删除会议嘉宾数据
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
    public Optional<SmMeetingGuestDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmMeetingGuestDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmMeetingGuest> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smMeetingGuest -> doConvert(smMeetingGuest, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmMeetingGuestDTO> findAll(SmMeetingGuestCriteria criteria) {
        log.debug("Service ==> 查询所有SmMeetingGuestDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuest> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuest.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(smMeetingGuest -> doConvert(smMeetingGuest, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SmMeetingGuestDTO> findPage(SmMeetingGuestCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SmMeetingGuestDTO {}, {}", criteria, pageable);
        Page<SmMeetingGuest> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuest> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuest.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmMeetingGuestDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(smMeetingGuest -> doConvert(smMeetingGuest, criteria));
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
    public int findCount(SmMeetingGuestCriteria criteria) {
        log.debug("Service ==> 查询个数SmMeetingGuestDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuest> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuest.class, null, tableIndexMap);
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
    private Wrapper<SmMeetingGuest> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmMeetingGuestCriteria smMeetingGuestCriteria = new SmMeetingGuestCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smMeetingGuestCriteria);
        Wrapper<SmMeetingGuest> wrapper = MbpUtil.getWrapper(null, smMeetingGuestCriteria, SmMeetingGuest.class, null, null);
        ((QueryWrapper<SmMeetingGuest>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SmMeetingGuest> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SmMeetingGuestCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SmMeetingGuest.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SmMeetingGuestCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SmMeetingGuestCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SmMeetingGuest.getTableName() + " AS " + SmMeetingGuest.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SmMeetingGuestCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getSmMeeting() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SmMeeting.getTableName() + " AS " + SmMeeting.getTableName() + "_" + tableCount + " ON "
                    + SmMeeting.getTableName() + "_" + tableCount + ".id = " + SmMeetingGuest.getTableName() + "_" + fromTableCount
                    + ".sm_meeting_id";
            String tableKey = "smMeeting";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += smMeetingService.getJoinSql(criteria.getSmMeeting(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param smMeetingGuest 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SmMeetingGuestDTO doConvert(SmMeetingGuest smMeetingGuest, BaseCriteria criteria) {
        SmMeetingGuestDTO smMeetingGuestDTO = new SmMeetingGuestDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(smMeetingGuest, smMeetingGuestDTO);
        getAssociations(smMeetingGuestDTO, criteria);
        return smMeetingGuestDTO;
    }

    /**
     * 获取关联属性
     * @param smMeetingGuestDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SmMeetingGuestDTO getAssociations(SmMeetingGuestDTO smMeetingGuestDTO, BaseCriteria criteria) {
        if (smMeetingGuestDTO.getId() == null) {
            return smMeetingGuestDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("smMeetingGuestPhotoList")) {
                // 获取嘉宾照片列表
                List<SmMeetingGuestPhotoDTO> smMeetingGuestPhotoList = smMeetingGuestPhotoMapper.selectList(
                        new QueryWrapper<SmMeetingGuestPhoto>().eq("sm_meeting_guest_id", smMeetingGuestDTO.getId())
                ).stream().map(smMeetingGuestPhoto -> {
                    SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO = new SmMeetingGuestPhotoDTO();
                    MyBeanUtil.copyNonNullProperties(smMeetingGuestPhoto, smMeetingGuestPhotoDTO);
                    return smMeetingGuestPhotoDTO;
                }).collect(Collectors.toList());
                smMeetingGuestDTO.setSmMeetingGuestPhotoList(smMeetingGuestPhotoList);
                // 继续追查
                if (smMeetingGuestPhotoList != null && smMeetingGuestPhotoList.size() > 0) {
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
                        if (associationName.startsWith("smMeetingGuestPhotoList.")) {
                            String associationName2 = associationName.substring("smMeetingGuestPhotoList.".length());
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria smMeetingGuestPhotoCriteria = new BaseCriteria();
                    smMeetingGuestPhotoCriteria.setAssociationNameList(associationName2List);
                    for (SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO : smMeetingGuestPhotoList) {
                        smMeetingGuestPhotoService.getAssociations(smMeetingGuestPhotoDTO, smMeetingGuestPhotoCriteria);
                    }
                }
            }
            if (associationNameList.contains("smMeeting")) {
			    // 获取会议
                Long smMeetingId = smMeetingGuestDTO.getSmMeetingId();
                if (smMeetingId == null) {
                    return smMeetingGuestDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("smMeeting.")) {
                        String associationName2 = associationName.substring("smMeeting.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria smMeetingCriteria = new BaseCriteria();
                smMeetingCriteria.setAssociationNameList(associationName2List);
                Optional<SmMeetingDTO> smMeetingOptional = smMeetingService.findOne(smMeetingId, smMeetingCriteria);
                smMeetingGuestDTO.setSmMeeting(smMeetingOptional.isPresent() ? smMeetingOptional.get() : null);
            }
        }
        return smMeetingGuestDTO;
    }

}
