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

public interface SmMeetingDetailService extends IService<SmMeetingDetail> {

    ReturnCommonDTO save(SmMeetingDetailDTO smMeetingDetailDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SmMeetingDetailDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingDetailDTO> findAll(SmMeetingDetailCriteria criteria);
    IPage<SmMeetingDetailDTO> findPage(SmMeetingDetailCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingDetailCriteria criteria);

    String getJoinSql(SmMeetingDetailCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SmMeetingDetailDTO getAssociations(SmMeetingDetailDTO smMeetingDetailDTO, BaseCriteria criteria);

}
