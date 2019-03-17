package ${packageName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.MbpPage;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Map;

public interface ${eentityName}Service extends BaseService<${eentityName}, ${eentityName}Criteria> {

    ReturnCommonDTO save(${eentityName}DTO ${entityName}DTO);
    ReturnCommonDTO deleteById(Long id);
    ReturnCommonDTO deleteByIdList(List<Long> idList);
    ReturnCommonDTO deleteByIdListNot(List<Long> idList);
    ReturnCommonDTO deleteByMapCascade(Map<String, Object> deleteMap);
    ReturnCommonDTO<${eentityName}DTO> findOne(Long id, BaseCriteria criteria,
            Map<String, Object> appendParamMap);
    ReturnCommonDTO<List<${eentityName}DTO>> findAll(${eentityName}Criteria criteria,
            Map<String, Object> appendParamMap);
    ReturnCommonDTO<IPage<${eentityName}DTO>> findPage(${eentityName}Criteria criteria,
            MbpPage pageable, Map<String, Object> appendParamMap);
    ReturnCommonDTO<Integer> findCount(${eentityName}Criteria criteria);

    ${eentityName}DTO getAssociations(${eentityName}DTO ${entityName}DTO, BaseCriteria criteria,
                        Map<String, Object> appendParamMap);

}
