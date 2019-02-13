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

public interface SystemRoleService extends IService<SystemRole> {

    ReturnCommonDTO save(SystemRoleDTO systemRoleDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemRoleDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemRoleDTO> findAll(SystemRoleCriteria criteria);
    IPage<SystemRoleDTO> findPage(SystemRoleCriteria criteria, MbpPage pageable);
    int findCount(SystemRoleCriteria criteria);

    String getJoinSql(SystemRoleCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemRoleDTO getAssociations(SystemRoleDTO systemRoleDTO, BaseCriteria criteria);

}
