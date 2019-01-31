package ${packageName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.MbpPage;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.*;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.*;
import ${packageName}.util.MbpUtil;
import ${packageName}.util.MyBeanUtil;
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
public class SmMeetingServiceImpl extends ServiceImpl<SmMeetingMapper, SmMeeting> implements SmMeetingService {

    private final Logger log = LoggerFactory.getLogger(SmMeetingServiceImpl.class);

    @Autowired
    private SmMeetingDetailMapper smMeetingDetailMapper;

    @Autowired
    private SmMeetingScheduleMapper smMeetingScheduleMapper;

    @Autowired
    private SmMeetingGuestMapper smMeetingGuestMapper;

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
			if (smMeetingDTO.getSmMeetingDetailList() != null) {
				// 清空会议详情列表
				smMeetingDetailMapper.deleteByMap(new HashMap<String, Object>() {{
					put("{fromTo.fromColumnName}", smMeetingId);
				}});
			}
			if (smMeetingDTO.getSmMeetingScheduleList() != null) {
				// 清空会议日程列表
				smMeetingScheduleMapper.deleteByMap(new HashMap<String, Object>() {{
					put("{fromTo.fromColumnName}", smMeetingId);
				}});
			}
			if (smMeetingDTO.getSmMeetingGuestList() != null) {
				// 清空会议嘉宾列表
				smMeetingGuestMapper.deleteByMap(new HashMap<String, Object>() {{
					put("{fromTo.fromColumnName}", smMeetingId);
				}});
			}
		}
        SmMeeting smMeeting = new SmMeeting();
        MyBeanUtil.copyNonNullProperties(smMeetingDTO, smMeeting);
        boolean result = saveOrUpdate(smMeeting);
        long smMeetingId = smMeeting.getId();
        // 需要级联保存的属性
        if (smMeetingDTO.getSmMeetingDetailList() != null) {
            // 新增会议详情
            for (SmMeetingDetail smMeetingDetail : smMeetingDTO.getSmMeetingDetailList()) {
                smMeetingDetail.setSmMeetingId(smMeetingId);
                smMeetingDetail.setInsertTime(smMeetingDTO.getInsertTime());
                smMeetingDetail.setUpdateTime(smMeetingDTO.getUpdateTime());
                smMeetingDetailMapper.insert(smMeetingDetail);
            }
        }
        if (smMeetingDTO.getSmMeetingScheduleList() != null) {
            // 新增会议日程
            for (SmMeetingSchedule smMeetingSchedule : smMeetingDTO.getSmMeetingScheduleList()) {
                smMeetingSchedule.setSmMeetingId(smMeetingId);
                smMeetingSchedule.setInsertTime(smMeetingDTO.getInsertTime());
                smMeetingSchedule.setUpdateTime(smMeetingDTO.getUpdateTime());
                smMeetingScheduleMapper.insert(smMeetingSchedule);
            }
        }
        if (smMeetingDTO.getSmMeetingGuestList() != null) {
            // 新增会议嘉宾
            for (SmMeetingGuest smMeetingGuest : smMeetingDTO.getSmMeetingGuestList()) {
                smMeetingGuest.setSmMeetingId(smMeetingId);
                smMeetingGuest.setInsertTime(smMeetingDTO.getInsertTime());
                smMeetingGuest.setUpdateTime(smMeetingDTO.getUpdateTime());
                smMeetingGuestMapper.insert(smMeetingGuest);
            }
        }
        smMeetingDTO.setId(smMeeting.getId());
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 删除SmMeetingDTO {}", id);
        // 删除会议详情
        smMeetingDetailMapper.deleteByMap(new HashMap<String, Object>() {{
            put("{fromTo.fromColumnName}", id);
        }});
        // 删除会议日程
        smMeetingScheduleMapper.deleteByMap(new HashMap<String, Object>() {{
            put("{fromTo.fromColumnName}", id);
        }});
        // 删除会议嘉宾
        smMeetingGuestMapper.deleteByMap(new HashMap<String, Object>() {{
            put("{fromTo.fromColumnName}", id);
        }});
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
    public Optional<SmMeetingDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SmMeetingDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SmMeeting> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(smMeeting -> {
            SmMeetingDTO smMeetingDTO = new SmMeetingDTO();
            // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
            MyBeanUtil.copyNonNullProperties(smMeeting, smMeetingDTO);
            getAssociations(smMeetingDTO, criteria);
            return smMeetingDTO;
        });
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SmMeetingDTO> findAll(SmMeetingCriteria criteria) {
        log.debug("Service ==> 查询所有SmMeetingDTO {}", criteria);
        Wrapper<SmMeeting> wrapper = new MbpUtil().getWrapper(criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        List<SmMeetingDTO> smMeetingDTOList = list(wrapper)
            .stream().map(smMeeting -> {
                SmMeetingDTO smMeetingDTO = new SmMeetingDTO();
                // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
                MyBeanUtil.copyNonNullProperties(smMeeting, smMeetingDTO);
                getAssociations(smMeetingDTO, criteria);
                return smMeetingDTO;
        }).collect(Collectors.toList());
        return smMeetingDTOList;
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
        MbpPage<SmMeeting> pageQuery = new MbpPage<>(pageable.getCurrent(), pageable.getSize());
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SmMeetingDTO> pageResult = ((MbpPage)MbpUtil.selectPage(baseMapper, pageQuery, wrapper))
                .map(smMeeting -> {
                    SmMeetingDTO smMeetingDTO = new SmMeetingDTO();
                    // TODO:在此处对每条数据做些处理，如果不需要处理，不用map即可
                    MyBeanUtil.copyNonNullProperties(smMeeting, smMeetingDTO);
                    getAssociations(smMeetingDTO, criteria);
                    return smMeetingDTO;
                });
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
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(criteria);
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
    private Wrapper<SmMeeting> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SmMeetingCriteria smMeetingCriteria = new SmMeetingCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, smMeetingCriteria);
        Wrapper<SmMeeting> wrapper = MbpUtil.getWrapper(smMeetingCriteria);
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
            if (associationNameList.contains("smMeetingDetailList")) {
                // 获取会议详情列表
                List<SmMeetingDetail> smMeetingDetailList = smMeetingDetailMapper.selectList(
                        new QueryWrapper<SmMeetingDetail>().eq("sm_meeting", smMeetingDTO.getId())
                );
                smMeetingDTO.setSmMeetingDetailList(smMeetingDetailList);
            }
            if (associationNameList.contains("smMeetingScheduleList")) {
                // 获取会议日程列表
                List<SmMeetingSchedule> smMeetingScheduleList = smMeetingScheduleMapper.selectList(
                        new QueryWrapper<SmMeetingSchedule>().eq("sm_meeting", smMeetingDTO.getId())
                );
                smMeetingDTO.setSmMeetingScheduleList(smMeetingScheduleList);
            }
            if (associationNameList.contains("smMeetingGuestList")) {
                // 获取会议嘉宾列表
                List<SmMeetingGuest> smMeetingGuestList = smMeetingGuestMapper.selectList(
                        new QueryWrapper<SmMeetingGuest>().eq("sm_meeting", smMeetingDTO.getId())
                );
                smMeetingDTO.setSmMeetingGuestList(smMeetingGuestList);
            }
        }
        return smMeetingDTO;
    }

}
