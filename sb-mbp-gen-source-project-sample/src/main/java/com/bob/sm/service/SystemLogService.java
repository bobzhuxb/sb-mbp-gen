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

public interface SystemLogService extends IService<SystemLog> {

    ReturnCommonDTO save(SystemLogDTO systemLogDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SystemLogDTO> findOne(Long id, BaseCriteria criteria);
    List<SystemLogDTO> findAll(SystemLogCriteria criteria);
    IPage<SystemLogDTO> findPage(SystemLogCriteria criteria, MbpPage pageable);
    int findCount(SystemLogCriteria criteria);

    String getJoinSql(SystemLogCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SystemLogDTO getAssociations(SystemLogDTO systemLogDTO, BaseCriteria criteria);

}
