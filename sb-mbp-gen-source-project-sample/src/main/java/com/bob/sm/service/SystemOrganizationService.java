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

public interface SystemOrganizationService extends IService<SystemOrganization> {

    ReturnCommonDTO save(SystemOrganizationDTO systemOrganizationDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemOrganizationDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemOrganizationDTO> findAll(SystemOrganizationCriteria criteria);
    IPage<SystemOrganizationDTO> findPage(SystemOrganizationCriteria criteria, MbpPage pageable);
    int findCount(SystemOrganizationCriteria criteria);

    String getJoinSql(SystemOrganizationCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemOrganizationDTO getAssociations(SystemOrganizationDTO systemOrganizationDTO, BaseCriteria criteria);

}
