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

public interface SmPayLogService extends IService<SmPayLog> {

    ReturnCommonDTO save(SmPayLogDTO smPayLogDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SmPayLogDTO> findOne(Long id, BaseCriteria criteria);
    List<SmPayLogDTO> findAll(SmPayLogCriteria criteria);
    IPage<SmPayLogDTO> findPage(SmPayLogCriteria criteria, MbpPage pageable);
    int findCount(SmPayLogCriteria criteria);

    String getJoinSql(SmPayLogCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SmPayLogDTO getAssociations(SmPayLogDTO smPayLogDTO, BaseCriteria criteria);

}
