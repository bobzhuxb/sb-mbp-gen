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

public interface SmPersonService extends IService<SmPerson> {

    ReturnCommonDTO save(SmPersonDTO smPersonDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<SmPersonDTO> findOne(Long id, BaseCriteria criteria);
    List<SmPersonDTO> findAll(SmPersonCriteria criteria);
    IPage<SmPersonDTO> findPage(SmPersonCriteria criteria, MbpPage pageable);
    int findCount(SmPersonCriteria criteria);

    String getJoinSql(SmPersonCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    SmPersonDTO getAssociations(SmPersonDTO smPersonDTO, BaseCriteria criteria);

}
