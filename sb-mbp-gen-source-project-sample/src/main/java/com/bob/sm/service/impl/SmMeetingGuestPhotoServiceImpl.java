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
public class SmMeetingGuestPhotoServiceImpl extends ServiceImpl<SmMeetingGuestPhotoMapper, SmMeetingGuestPhoto> implements SmMeetingGuestPhotoService {

    private final Logger log = LoggerFactory.getLogger(SmMeetingGuestPhotoServiceImpl.class);

    @Autowired
    private SmMeetingGuestService smMeetingGuestService;

    /**
     * 新增或更新
     * @param smMeetingGuestPhotoDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO) {
        log.debug("Service ==> 新增或修改SmMeetingGuestPhoto {}", smMeetingGuestPhotoDTO);
		if (smMeetingGuestPhotoDTO.getId() != null) {
			// 修改
			long smMeetingGuestPhotoId = smMeetingGuestPhotoDTO.getId();
		}
        // 新增或更新会议嘉宾照片（当前实体）
        SmMeetingGuestPhoto smMeetingGuestPhoto = new SmMeetingGuestPhoto();
        MyBeanUtil.copyNonNullProperties(smMeetingGuestPhotoDTO, smMeetingGuestPhoto);
        if (smMeetingGuestPhoto.getId() == null) {
            // 新增
            smMeetingGuestPhoto.setInsertUserId(smMeetingGuestPhotoDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(smMeetingGuestPhoto);
        long smMeetingGuestPhotoId = smMeetingGuestPhoto.getId();
        smMeetingGuestPhotoDTO.setId(smMeetingGuestPhotoId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SmMeetingGuestPhotoDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SmMeetingGuestPhotoDTO {}", columnMap);
        // 根据指定条件删除会议嘉宾照片数据
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
    public Optional<SmMeetingGuestPhotoDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmMeetingGuestPhotoDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmMeetingGuestPhoto> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smMeetingGuestPhoto -> doConvert(smMeetingGuestPhoto, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmMeetingGuestPhotoDTO> findAll(SmMeetingGuestPhotoCriteria criteria) {
        log.debug("Service ==> 查询所有SmMeetingGuestPhotoDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuestPhoto> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuestPhoto.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(smMeetingGuestPhoto -> doConvert(smMeetingGuestPhoto, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SmMeetingGuestPhotoDTO> findPage(SmMeetingGuestPhotoCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SmMeetingGuestPhotoDTO {}, {}", criteria, pageable);
        Page<SmMeetingGuestPhoto> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuestPhoto> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuestPhoto.class, null, tableIndexMap);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmMeetingGuestPhotoDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(smMeetingGuestPhoto -> doConvert(smMeetingGuestPhoto, criteria));
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
    public int findCount(SmMeetingGuestPhotoCriteria criteria) {
        log.debug("Service ==> 查询个数SmMeetingGuestPhotoDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SmMeetingGuestPhoto> wrapper = MbpUtil.getWrapper(null, criteria, SmMeetingGuestPhoto.class, null, tableIndexMap);
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
    private Wrapper<SmMeetingGuestPhoto> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmMeetingGuestPhotoCriteria smMeetingGuestPhotoCriteria = new SmMeetingGuestPhotoCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smMeetingGuestPhotoCriteria);
        Wrapper<SmMeetingGuestPhoto> wrapper = MbpUtil.getWrapper(null, smMeetingGuestPhotoCriteria, SmMeetingGuestPhoto.class, null, null);
        ((QueryWrapper<SmMeetingGuestPhoto>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SmMeetingGuestPhoto> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SmMeetingGuestPhotoCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SmMeetingGuestPhoto.getTableName() + "_" + tableCount + ".*";
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
    private String getCountQuerySql(SmMeetingGuestPhotoCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SmMeetingGuestPhotoCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SmMeetingGuestPhoto.getTableName() + " AS " + SmMeetingGuestPhoto.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SmMeetingGuestPhotoCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        if (criteria.getSmMeetingGuest() != null) {
            tableCount++;
            joinSubSql += " LEFT JOIN " + SmMeetingGuest.getTableName() + " AS " + SmMeetingGuest.getTableName() + "_" + tableCount + " ON "
                    + SmMeetingGuest.getTableName() + "_" + tableCount + ".id = " + SmMeetingGuestPhoto.getTableName() + "_" + fromTableCount
                    + ".sm_meeting_guest_id";
            String tableKey = "smMeetingGuest";
            if (lastFieldName != null) {
                // 拼接key
                tableKey = lastFieldName + "." + tableKey;
            }
            tableIndexMap.put(tableKey, tableCount);
            joinSubSql += smMeetingGuestService.getJoinSql(criteria.getSmMeetingGuest(), tableCount, tableCount, tableKey,
			        tableIndexMap);
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param smMeetingGuestPhoto 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SmMeetingGuestPhotoDTO doConvert(SmMeetingGuestPhoto smMeetingGuestPhoto, BaseCriteria criteria) {
        SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO = new SmMeetingGuestPhotoDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(smMeetingGuestPhoto, smMeetingGuestPhotoDTO);
        getAssociations(smMeetingGuestPhotoDTO, criteria);
        return smMeetingGuestPhotoDTO;
    }

    /**
     * 获取关联属性
     * @param smMeetingGuestPhotoDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SmMeetingGuestPhotoDTO getAssociations(SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO, BaseCriteria criteria) {
        if (smMeetingGuestPhotoDTO.getId() == null) {
            return smMeetingGuestPhotoDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("smMeetingGuest")) {
			    // 获取会议嘉宾
                Long smMeetingGuestId = smMeetingGuestPhotoDTO.getSmMeetingGuestId();
                if (smMeetingGuestId == null) {
                    return smMeetingGuestPhotoDTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("smMeetingGuest.")) {
                        String associationName2 = associationName.substring("smMeetingGuest.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria smMeetingGuestCriteria = new BaseCriteria();
                smMeetingGuestCriteria.setAssociationNameList(associationName2List);
                Optional<SmMeetingGuestDTO> smMeetingGuestOptional = smMeetingGuestService.findOne(smMeetingGuestId, smMeetingGuestCriteria);
                smMeetingGuestPhotoDTO.setSmMeetingGuest(smMeetingGuestOptional.isPresent() ? smMeetingGuestOptional.get() : null);
            }
        }
        return smMeetingGuestPhotoDTO;
    }

}
