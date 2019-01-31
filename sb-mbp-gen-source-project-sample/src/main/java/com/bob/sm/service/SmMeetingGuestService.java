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

public interface SmMeetingGuestService extends IService<SmMeetingGuest> {

    ReturnCommonDTO save(SmMeetingGuestDTO smMeetingGuestDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingGuestDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingGuestDTO> findAll(SmMeetingGuestCriteria criteria);
    IPage<SmMeetingGuestDTO> findPage(SmMeetingGuestCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingGuestCriteria criteria);

    SmMeetingGuestDTO getAssociations(SmMeetingGuestDTO smMeetingGuestDTO, BaseCriteria criteria);

}
