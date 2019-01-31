package com.bob.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy
@Transactional
public class SmMeetingDetailServiceImpl extends ServiceImpl<SmMeetingDetailMapper, SmMeetingDetail> implements SmMeetingDetailService {

    private final Logger log = LoggerFactory.getLogger(SmMeetingDetailServiceImpl.class);

    @Autowired
    private SmMeetingService smMeetingService;

    /**
     * 新增或更新
     * @param smMeetingDetailDTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(SmMeetingDetailDTO smMeetingDetailDTO) {
        log.debug("Service ==> 新增或修改SmMeetingDetail {}", smMeetingDetailDTO);
		if (smMeetingDetailDTO.getId() != null) {
			// 修改
			long smMeetingDetailId = smMeetingDetailDTO.getId();
		}
        SmMeetingDetail smMeetingDetail = new SmMeetingDetail();
        MyBeanUtil.copyNonNullProperties(smMeetingDetailDTO, smMeetingDetail);
        boolean result = saveOrUpdate(smMeetingDetail);
        long smMeetingDetailId = smMeetingDetail.getId();
        smMeetingDetailDTO.setId(smMeetingDetail.getId());
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 删除SmMeetingDetailDTO {}", id);
        boolean result = removeById(id);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "删除失败");
    }

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public Optional<SmMeetingDetailDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmMeetingDetailDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmMeetingDetail> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smMeetingDetail -> {
            SmMeetingDetailDTO smMeetingDetailDTO = new SmMeetingDetailDTO();
            // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
            MyBeanUtil.copyNonNullProperties(smMeetingDetail, smMeetingDetailDTO);
            getAssociations(smMeetingDetailDTO, criteria);
            return smMeetingDetailDTO;
        });
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmMeetingDetailDTO> findAll(SmMeetingDetailCriteria criteria) {
        log.debug("Service ==> 查询所有SmMeetingDetailDTO {}", criteria);
        Wrapper<SmMeetingDetail> wrapper = new MbpUtil().getWrapper(criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        List<SmMeetingDetailDTO> smMeetingDetailDTOList = list(wrapper)
            .stream().map(smMeetingDetail -> {
                SmMeetingDetailDTO smMeetingDetailDTO = new SmMeetingDetailDTO();
                // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
                MyBeanUtil.copyNonNullProperties(smMeetingDetail, smMeetingDetailDTO);
                getAssociations(smMeetingDetailDTO, criteria);
                return smMeetingDetailDTO;
        }).collect(Collectors.toList());
        return smMeetingDetailDTOList;
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SmMeetingDetailDTO> findPage(SmMeetingDetailCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SmMeetingDetailDTO {}, {}", criteria, pageable);
        MbpPage<SmMeetingDetail> pageQuery = new MbpPage<>(pageable.getCurrent(), pageable.getSize());
        Wrapper<SmMeetingDetail> wrapper = MbpUtil.getWrapper(criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmMeetingDetailDTO> pageResult = ((MbpPage)MbpUtil.selectPage(baseMapper, pageQuery, wrapper))
                .map(smMeetingDetail -> {
                    SmMeetingDetailDTO smMeetingDetailDTO = new SmMeetingDetailDTO();
                    // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
                    MyBeanUtil.copyNonNullProperties(smMeetingDetail, smMeetingDetailDTO);
                    getAssociations(smMeetingDetailDTO, criteria);
                    return smMeetingDetailDTO;
                });
        return pageResult;
    }

    /**
     * 查询个数
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(readOnly = true)
    public int findCount(SmMeetingDetailCriteria criteria) {
        log.debug("Service ==> 查询个数SmMeetingDetailDTO {}", criteria);
        Wrapper<SmMeetingDetail> wrapper = MbpUtil.getWrapper(criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return 0;
        }
        int count = count(wrapper);
        return count;
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param criteria 附加条件
     * @return 查询通用Wrapper
     */
    private Wrapper<SmMeetingDetail> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmMeetingDetailCriteria smMeetingDetailCriteria = new SmMeetingDetailCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smMeetingDetailCriteria);
        Wrapper<SmMeetingDetail> wrapper = MbpUtil.getWrapper(smMeetingDetailCriteria);
        ((QueryWrapper<SmMeetingDetail>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SmMeetingDetail> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }

    /**
     * 获取关联属性
     * @param smMeetingDetailDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SmMeetingDetailDTO getAssociations(SmMeetingDetailDTO smMeetingDetailDTO, BaseCriteria criteria) {
        if (smMeetingDetailDTO.getId() == null) {
            return smMeetingDetailDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
            if (associationNameList.contains("smMeeting")) {
			    // 获取会议
                Long smMeetingId = smMeetingDetailDTO.getSmMeetingId();
                if (smMeetingId == null) {
                    return smMeetingDetailDTO;
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
                smMeetingDetailDTO.setSmMeeting(smMeetingOptional.isPresent() ? smMeetingOptional.get() : null);
            }
        }
        return smMeetingDetailDTO;
    }

}
