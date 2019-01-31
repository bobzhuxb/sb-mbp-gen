package com.bob.sm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.sm.domain.*;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.*;
import com.bob.sm.dto.help.MbpPage;
import com.bob.sm.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Optional;

public interface SmMeetingScheduleService extends IService<SmMeetingSchedule> {

    ReturnCommonDTO save(SmMeetingScheduleDTO smMeetingScheduleDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingScheduleDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingScheduleDTO> findAll(SmMeetingScheduleCriteria criteria);
    IPage<SmMeetingScheduleDTO> findPage(SmMeetingScheduleCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingScheduleCriteria criteria);

    SmMeetingScheduleDTO getAssociations(SmMeetingScheduleDTO smMeetingScheduleDTO, BaseCriteria criteria);

}
