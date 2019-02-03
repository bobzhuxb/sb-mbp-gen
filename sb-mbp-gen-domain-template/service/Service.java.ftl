package ${packageName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.MbpPage;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ${eentityName}Service extends IService<${eentityName}> {

    ReturnCommonDTO save(${eentityName}DTO ${entityName}DTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<${eentityName}DTO> findOne(Long id, BaseCriteria criteria);
    List<${eentityName}DTO> findAll(${eentityName}Criteria criteria);
    IPage<${eentityName}DTO> findPage(${eentityName}Criteria criteria, MbpPage pageable);
    int findCount(${eentityName}Criteria criteria);

    String getJoinSql(${eentityName}Criteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                      Map<String, Integer> tableIndexMap);
    ${eentityName}DTO getAssociations(${eentityName}DTO ${entityName}DTO, BaseCriteria criteria);

}
