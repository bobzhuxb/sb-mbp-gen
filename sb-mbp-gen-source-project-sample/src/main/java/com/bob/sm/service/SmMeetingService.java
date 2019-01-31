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

public interface SmMeetingService extends IService<SmMeeting> {

    ReturnCommonDTO save(SmMeetingDTO smMeetingDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingDTO> findAll(SmMeetingCriteria criteria);
    IPage<SmMeetingDTO> findPage(SmMeetingCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingCriteria criteria);

    SmMeetingDTO getAssociations(SmMeetingDTO smMeetingDTO, BaseCriteria criteria);

}
