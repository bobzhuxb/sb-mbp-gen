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

public interface SystemResourcePermissionService extends IService<SystemResourcePermission> {

    ReturnCommonDTO save(SystemResourcePermissionDTO systemResourcePermissionDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemResourcePermissionDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemResourcePermissionDTO> findAll(SystemResourcePermissionCriteria criteria);
    IPage<SystemResourcePermissionDTO> findPage(SystemResourcePermissionCriteria criteria, MbpPage pageable);
    int findCount(SystemResourcePermissionCriteria criteria);

    String getJoinSql(SystemResourcePermissionCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemResourcePermissionDTO getAssociations(SystemResourcePermissionDTO systemResourcePermissionDTO, BaseCriteria criteria);

}
