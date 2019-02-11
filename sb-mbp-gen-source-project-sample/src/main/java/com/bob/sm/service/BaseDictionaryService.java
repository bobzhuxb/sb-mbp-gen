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

public interface BaseDictionaryService extends IService<BaseDictionary> {

    ReturnCommonDTO save(BaseDictionaryDTO baseDictionaryDTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    Optional<BaseDictionaryDTO> findOne(Long id, BaseCriteria criteria);
    List<BaseDictionaryDTO> findAll(BaseDictionaryCriteria criteria);
    IPage<BaseDictionaryDTO> findPage(BaseDictionaryCriteria criteria, MbpPage pageable);
    int findCount(BaseDictionaryCriteria criteria);

    String getJoinSql(BaseDictionaryCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    BaseDictionaryDTO getAssociations(BaseDictionaryDTO baseDictionaryDTO, BaseCriteria criteria);

}
