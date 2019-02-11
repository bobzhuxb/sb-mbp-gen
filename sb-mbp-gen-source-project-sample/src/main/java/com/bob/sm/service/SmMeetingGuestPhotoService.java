package com.bob.sm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.sm.domain.*;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.*;
import com.bob.sm.dto.help.MbpPage;
import com.bob.sm.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SmMeetingGuestPhotoService extends IService<SmMeetingGuestPhoto> {

    ReturnCommonDTO save(SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SmMeetingGuestPhotoDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingGuestPhotoDTO> findAll(SmMeetingGuestPhotoCriteria criteria);
    IPage<SmMeetingGuestPhotoDTO> findPage(SmMeetingGuestPhotoCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingGuestPhotoCriteria criteria);

    String getJoinSql(SmMeetingGuestPhotoCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SmMeetingGuestPhotoDTO getAssociations(SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO, BaseCriteria criteria);

}
