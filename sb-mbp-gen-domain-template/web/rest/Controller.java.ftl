package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.annotation.validation.group.*;
import ${packageName}.config.Constants;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.*;
import ${packageName}.service.*;
import ${packageName}.util.ParamValidatorUtil;
import ${packageName}.web.rest.errors.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 注意规范：新增的方法以create开头，修改的方法以update开头，删除的方法以delete开头，查询的方法以get开头
 */
@Api(description="${entityComment}")
@RestController
@RequestMapping("/api")
public class ${eentityName}Controller {

    private final Logger log = LoggerFactory.getLogger(${eentityName}Controller.class);

    private static final String ENTITY_NAME = "${entityName}";

    @Autowired
    private ${eentityName}Service ${entityName}Service;

    /**
     * 新增
     * @param ${entityName}DTO 待新增的实体
	 * @param bindingResult 参数验证结果（注意：不分配groups分组时，默认每次都需要验证）
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="${entityName}DTO", dataType = "string", value = "{\n" +
                    <#list fieldList as field>
					<#if field.camelName != 'insertTime' && field.camelName != 'insertUserId' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>
                    "  \"${field.camelName}\": <#if field.javaType == 'String'>\"</#if>${field.comment}<#if field.javaType == 'String'>\"</#if>,\n" +
					</#if>
					</#list>
                    <#list toFromList as toFrom>
                    "  \"${toFrom.toFromEntityName}Id\": ${toFrom.toFromComment}ID,\n" +
			        </#list>
                    <#list fromToList as fromTo>
                    "  \"${fromTo.fromToEntityName}<#if fromTo.relationType == 'OneToMany'>List</#if>\": <#if fromTo.relationType == 'OneToOne'>{<#else>[</#if>${fromTo.fromToComment}<#if fromTo.relationType == 'OneToMany'>列表</#if>（级联类型：${fromTo.fromToEntityType}）<#if fromTo.relationType == 'OneToOne'>}<#else>]</#if>,\n" +
			        </#list>
                    "}"),
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败<br/>" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> create${eentityName}(
        @RequestBody @Validated(value = {ValidateCreateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = ${entityName}Service.save(${entityName}DTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ${entityName}DTO 待修改的实体
	 * @param bindingResult 参数验证结果（注意：不分配groups分组时，默认每次都需要验证）
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="${entityName}DTO", dataType = "string", value = "{\n" +
                    "  \"id\": \"主键ID\",\n" +
                    <#list fieldList as field>
					<#if field.camelName != 'insertTime' && field.camelName != 'insertUserId' && field.camelName != 'updateTime' && field.camelName != 'operateUserId'>
                    "  \"${field.camelName}\": <#if field.javaType == 'String'>\"</#if>${field.comment}<#if field.javaType == 'String'>\"</#if>,\n" +
					</#if>
					</#list>
                    <#list toFromList as toFrom>
                    "  \"${toFrom.toFromEntityName}Id\": ${toFrom.toFromComment}ID,\n" +
			        </#list>
                    <#list fromToList as fromTo>
                    "  \"${fromTo.fromToEntityName}<#if fromTo.relationType == 'OneToMany'>List</#if>\": <#if fromTo.relationType == 'OneToOne'>{<#else>[</#if>${fromTo.fromToComment}<#if fromTo.relationType == 'OneToMany'>列表</#if>（级联类型：${fromTo.fromToEntityType}）<#if fromTo.relationType == 'OneToOne'>}<#else>]</#if>,\n" +
			        </#list>
                    "}"),
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败<br/>" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> update${eentityName}(
        @RequestBody @Validated(value = {ValidateUpdateGroup.class}) ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = ${entityName}Service.save(${entityName}DTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="单个删除${entityComment}")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败<br/>" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/${entityUrl}/{id}")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除${eentityName} : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = ${entityName}Service.deleteById(id);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @ApiOperation(value="批量删除${entityComment}")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败<br/>" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}s(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除${eentityName} : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = ${entityName}Service.deleteByIdList(idList);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param id 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条${entityComment}数据
     */
    @ApiOperation(value="获取单条-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", dataType="string", value="关联查询的字段${associationNameComment}"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", dataType="string", value="关联查询的数据字典值${dictionaryNameList}")
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：查询成功  2：查询失败<br/>" +
                    "errMsg - 错误消息<br/>" +
                    "data - 查询结果", response=Object.class)
    })
    @GetMapping("/${entityUrl}/{id}")
    public ResponseEntity<ReturnCommonDTO<${eentityName}DTO>> get${eentityName}(
            @ApiParam(name="主键ID") @PathVariable Long id, @ApiIgnore BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询${eentityName} : {}, {}", id, criteria);
        ReturnCommonDTO<${eentityName}DTO> data = ${entityName}Service.findOne(id, criteria);
        return ResponseEntity.ok().headers(null).body(data);
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条${entityComment}数据
     */
    @ApiOperation(value="获取所有-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", dataType="string", value="关联查询的字段${associationNameComment}"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", dataType="string", value="关联查询的数据字典值${dictionaryNameList}"),
            @ApiImplicitParam(name="orderBy", paramType="path", dataType="string", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
			<#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals", paramType="path",<#if field.javaType == 'Integer'> dataType="int",<#elseif field.javaType == 'Long'> dataType="long",<#elseif field.javaType == 'Double'> dataType="double",<#else> dataType="string",</#if> value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}Id.equals", paramType="path", value="关联的${toFrom.toFromComment}ID"),
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals", paramType="path", value="关联的${toFrom.toFromComment}，其中 ? 对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：查询成功  2：查询失败<br/>" +
                    "errMsg - 错误消息<br/>" +
                    "data - 查询结果", response=Object.class)
    })
    @GetMapping("/${entityUrl}-all")
    public ResponseEntity<ReturnCommonDTO<List<${eentityName}DTO>>> getAll${eentityName}s(
            @ApiIgnore ${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询所有${eentityName} : {}", criteria);
        ReturnCommonDTO<List<${eentityName}DTO>> data = ${entityName}Service.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(data);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页${entityComment}数据
     */
    @ApiOperation(value="获取分页-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", dataType="string", value="关联查询的字段${associationNameComment}"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", dataType="string", value="关联查询的数据字典值${dictionaryNameList}"),
            @ApiImplicitParam(name="orderBy", paramType="path", dataType="string", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", dataType="long", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", dataType="long", value="分页：每页大小"),
            <#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals", paramType="path",<#if field.javaType == 'Integer'> dataType="int",<#elseif field.javaType == 'Long'> dataType="long",<#elseif field.javaType == 'Double'> dataType="double",<#else> dataType="string",</#if> value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}Id.equals", paramType="path", value="关联的${toFrom.toFromComment}ID"),
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals", paramType="path", value="关联的${toFrom.toFromComment}，其中 ? 对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：查询成功  2：查询失败<br/>" +
                    "errMsg - 错误消息<br/>" +
                    "data - 查询结果", response=Object.class)
    })
    @GetMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO<IPage<${eentityName}DTO>>> getPage${eentityName}s(
            @ApiIgnore ${eentityName}Criteria criteria, @ApiIgnore MbpPage pageable) {
        log.debug("Controller ==> 分页查询${eentityName} : {}, {}", criteria, pageable);
        ReturnCommonDTO<IPage<${eentityName}DTO>> data = ${entityName}Service.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(data);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的${entityComment}数量
     */
    @ApiOperation(value="获取数量-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", dataType="string", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            <#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals", paramType="path",<#if field.javaType == 'Integer'> dataType="int",<#elseif field.javaType == 'Long'> dataType="long",<#elseif field.javaType == 'Double'> dataType="double",<#else> dataType="string",</#if> value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}Id.equals", paramType="path", value="关联的${toFrom.toFromComment}ID"),
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals", paramType="path", value="关联的${toFrom.toFromComment}，其中 ? 对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：查询成功  2：查询失败<br/>" +
                    "errMsg - 错误消息<br/>" +
                    "data - 查询结果", response=Object.class)
    })
    @GetMapping("/${entityUrl}-count")
    public ResponseEntity<ReturnCommonDTO<Integer>> get${eentityName}Count(@ApiIgnore ${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询数量${eentityName} : {}", criteria);
        ReturnCommonDTO<Integer> data = ${entityName}Service.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(data);
    }

}
