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

public interface SystemUserRoleService extends IService<SystemUserRole> {

    ReturnCommonDTO save(SystemUserRoleDTO systemUserRoleDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemUserRoleDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemUserRoleDTO> findAll(SystemUserRoleCriteria criteria);
    IPage<SystemUserRoleDTO> findPage(SystemUserRoleCriteria criteria, MbpPage pageable);
    int findCount(SystemUserRoleCriteria criteria);

    String getJoinSql(SystemUserRoleCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemUserRoleDTO getAssociations(SystemUserRoleDTO systemUserRoleDTO, BaseCriteria criteria);

}
