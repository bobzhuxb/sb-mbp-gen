package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注意规范：新增的方法以create开头，修改的方法以update开头，删除的方法以delete开头，查询的方法以get开头
 * ${entityComment}
 * @author Bob
 */
@RestController
@RequestMapping("${urlPrefix}")
public class ${eentityName}Controller {

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
    @PreAuthorize("hasCreate('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO> create${eentityName}(
        @RequestBody @Validated(value = {ValidateCreateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", null, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
		ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseSave(DOMAIN_NAME, ${entityName}DTO, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ${entityName}DTO 待修改的实体
	 * @param bindingResult 参数验证结果（注意：不分配groups分组时，默认每次都需要验证）
     * @return 结果返回码和消息
     */
    @PutMapping("/${entityUrl}")
    @PreAuthorize("hasUpdate('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO> update${eentityName}(
        @RequestBody @Validated(value = {ValidateUpdateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", null, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
		ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseSave(DOMAIN_NAME, ${entityName}DTO, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/${entityUrl}/{id}")
    @PreAuthorize("hasDelete('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}(@PathVariable String id) {
        log.debug("Controller ==> 根据ID删除${eentityName} : {}", id);
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseDeleteById(DOMAIN_NAME, id, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/${entityUrl}")
    @PreAuthorize("hasDelete('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}s(@RequestBody List<String> idList) {
        log.debug("Controller ==> 批量删除${eentityName} : {}", idList);
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseDeleteByIdList(DOMAIN_NAME, idList, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param primaryId 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条${entityComment}数据
     */
    @GetMapping("/${entityUrl}/{primaryId}")
    @PreAuthorize("hasRead('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO<${eentityName}DTO>> get${eentityName}(
            @PathVariable String primaryId, ${eentityName}Criteria criteria) {
        log.debug("Controller ==> 根据ID查询${eentityName} : {}, {}", primaryId, criteria);
        ReturnCommonDTO<${eentityName}DTO> resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseFindOne(DOMAIN_NAME, primaryId, criteria, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条${entityComment}数据
     */
    @GetMapping("/${entityUrl}-all")
    @PreAuthorize("hasRead('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO<List<${eentityName}DTO>>> getAll${eentityName}s(
            ${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询所有${eentityName} : {}", criteria);
        ReturnCommonDTO<List<${eentityName}DTO>> resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseFindAll(DOMAIN_NAME, criteria, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 条件查询单条
     * @param criteria 查询条件
     * @return 使用ResponseEntity封装的单条${entityComment}数据
     */
    @GetMapping("/${entityUrl}-one")
    @PreAuthorize("hasRead('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO<${eentityName}DTO>> get${eentityName}One(
            ${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询指定条件下单条${eentityName} : {}", criteria);
        ReturnCommonDTO<${eentityName}DTO> resultDTO = commonService.doWithExceptionHandle(() -> {
            criteria.setLimit(1);
            ReturnCommonDTO<List<${eentityName}DTO>> resultTmp
                    = ${entityName}Service.baseFindAll(DOMAIN_NAME, criteria, null);
            return commonService.doGetSingleResult(resultTmp);}, log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页${entityComment}数据
     */
    @GetMapping("/${entityUrl}")
    @PreAuthorize("hasRead('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO<IPage<${eentityName}DTO>>> getPage${eentityName}s(
            ${eentityName}Criteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询${eentityName} : {}, {}", criteria, pageable);
        ReturnCommonDTO<IPage<${eentityName}DTO>> resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseFindPage(DOMAIN_NAME, criteria, pageable, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的${entityComment}数量
     */
    @GetMapping("/${entityUrl}-count")
    @PreAuthorize("hasRead('${lowerName}')")
    public ResponseEntity<ReturnCommonDTO<Integer>> get${eentityName}Count(${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询数量${eentityName} : {}", criteria);
        ReturnCommonDTO<Integer> resultDTO = commonService.doWithExceptionHandle(
                () -> ${entityName}Service.baseFindCount(DOMAIN_NAME, criteria, null), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
