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

public interface SystemPermissionService extends IService<SystemPermission> {

    ReturnCommonDTO save(SystemPermissionDTO systemPermissionDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemPermissionDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemPermissionDTO> findAll(SystemPermissionCriteria criteria);
    IPage<SystemPermissionDTO> findPage(SystemPermissionCriteria criteria, MbpPage pageable);
    int findCount(SystemPermissionCriteria criteria);

    String getJoinSql(SystemPermissionCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemPermissionDTO getAssociations(SystemPermissionDTO systemPermissionDTO, BaseCriteria criteria);

}
