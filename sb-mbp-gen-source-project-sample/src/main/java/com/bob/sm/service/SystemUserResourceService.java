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

public interface SystemUserResourceService extends IService<SystemUserResource> {

    ReturnCommonDTO save(SystemUserResourceDTO systemUserResourceDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemUserResourceDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemUserResourceDTO> findAll(SystemUserResourceCriteria criteria);
    IPage<SystemUserResourceDTO> findPage(SystemUserResourceCriteria criteria, MbpPage pageable);
    int findCount(SystemUserResourceCriteria criteria);

    String getJoinSql(SystemUserResourceCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemUserResourceDTO getAssociations(SystemUserResourceDTO systemUserResourceDTO, BaseCriteria criteria);

}
