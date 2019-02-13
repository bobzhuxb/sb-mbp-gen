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

public interface SystemRoleResourceService extends IService<SystemRoleResource> {

    ReturnCommonDTO save(SystemRoleResourceDTO systemRoleResourceDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemRoleResourceDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemRoleResourceDTO> findAll(SystemRoleResourceCriteria criteria);
    IPage<SystemRoleResourceDTO> findPage(SystemRoleResourceCriteria criteria, MbpPage pageable);
    int findCount(SystemRoleResourceCriteria criteria);

    String getJoinSql(SystemRoleResourceCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemRoleResourceDTO getAssociations(SystemRoleResourceDTO systemRoleResourceDTO, BaseCriteria criteria);

}
