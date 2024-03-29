package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.annotation.IdNullValidate;
import ${packageName}.annotation.validation.group.*;
import ${packageName}.config.Constants;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.*;
import ${packageName}.service.*;
import ${packageName}.util.ParamValidatorUtil;
import ${packageName}.web.rest.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注意规范：新增的方法以create开头，修改的方法以update开头，删除的方法以delete开头，查询的方法以get开头
 * AOP处理时使用到了bindingResult，所有的bindingResult参数不得删除
 * ${entityComment}
 * @author Bob
 */
@RestController
public class ${eentityName}Controller extends BaseController {

    private final Logger log = LoggerFactory.getLogger(${eentityName}Controller.class);

    /**
     * 对应的实体名（注意：这个常量值不要修改）
     */
    private final static String DOMAIN_NAME = ${eentityName}.class.getSimpleName();

    @Autowired
    private CommonService commonService;

    @Autowired
    private ${eentityName}Service ${entityName}Service;

    /**
     * 新增
     * @param ${entityName}DTO 待新增的实体
	 * @param bindingResult 参数验证结果（注意：不分配groups分组时，默认每次都需要验证）
     * @return 结果返回码和消息
     */
    @PostMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> create${eentityName}(@IdNullValidate(mustNull = true)
        @RequestBody @Validated(value = {ValidateCreateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
		ReturnCommonDTO resultDTO = ${entityName}Service.baseSave(DOMAIN_NAME, ${entityName}DTO, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ${entityName}DTO 待修改的实体
	 * @param bindingResult 参数验证结果（注意：不分配groups分组时，默认每次都需要验证）
     * @return 结果返回码和消息
     */
    @PutMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> update${eentityName}(@IdNullValidate(mustNotNull = true)
        @RequestBody @Validated(value = {ValidateUpdateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
		ReturnCommonDTO resultDTO = ${entityName}Service.baseSave(DOMAIN_NAME, ${entityName}DTO, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/${entityUrl}/{id}")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}(@PathVariable String id) {
        ReturnCommonDTO resultDTO = ${entityName}Service.baseDeleteById(DOMAIN_NAME, id, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}s(@RequestBody List<String> idList) {
        ReturnCommonDTO resultDTO = ${entityName}Service.baseDeleteByIdList(DOMAIN_NAME, idList, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param primaryId 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条${entityComment}数据
     */
    @GetMapping("/${entityUrl}/{primaryId}")
    public ResponseEntity<ReturnCommonDTO<${eentityName}DTO>> get${eentityName}(
            @PathVariable String primaryId, ${eentityName}Criteria criteria) {
        ReturnCommonDTO<${eentityName}DTO> resultDTO = ${entityName}Service.baseFindOne(DOMAIN_NAME, primaryId, criteria, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条${entityComment}数据
     */
    @GetMapping("/${entityUrl}-all")
    public ResponseEntity<ReturnCommonDTO<List<${eentityName}DTO>>> getAll${eentityName}s(
            ${eentityName}Criteria criteria) {
        ReturnCommonDTO<List<${eentityName}DTO>> resultDTO = ${entityName}Service.baseFindAll(DOMAIN_NAME, criteria, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 条件查询单条
     * @param criteria 查询条件
     * @return 使用ResponseEntity封装的单条${entityComment}数据
     */
    @GetMapping("/${entityUrl}-one")
    public ResponseEntity<ReturnCommonDTO<${eentityName}DTO>> get${eentityName}One(
            ${eentityName}Criteria criteria) {
        criteria.setLimit(1);
		ReturnCommonDTO<List<${eentityName}DTO>> resultTmp = ${entityName}Service.baseFindAll(DOMAIN_NAME, criteria, null);
		ReturnCommonDTO<${eentityName}DTO> resultDTO = commonService.doGetSingleResult(resultTmp);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页${entityComment}数据
     */
    @GetMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO<IPage<${eentityName}DTO>>> getPage${eentityName}s(
            ${eentityName}Criteria criteria, MbpPage pageable) {
        ReturnCommonDTO<IPage<${eentityName}DTO>> resultDTO = ${entityName}Service.baseFindPage(DOMAIN_NAME, criteria, pageable, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的${entityComment}数量
     */
    @GetMapping("/${entityUrl}-count")
    public ResponseEntity<ReturnCommonDTO<Integer>> get${eentityName}Count(${eentityName}Criteria criteria) {
        ReturnCommonDTO<Integer> resultDTO = ${entityName}Service.baseFindCount(DOMAIN_NAME, criteria, null);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
