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

public interface SystemResourceService extends IService<SystemResource> {

    ReturnCommonDTO save(SystemResourceDTO systemResourceDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemResourceDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemResourceDTO> findAll(SystemResourceCriteria criteria);
    IPage<SystemResourceDTO> findPage(SystemResourceCriteria criteria, MbpPage pageable);
    int findCount(SystemResourceCriteria criteria);

    String getJoinSql(SystemResourceCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemResourceDTO getAssociations(SystemResourceDTO systemResourceDTO, BaseCriteria criteria);

}
