package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增${entityComment}")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> create${eentityName}(
            @ApiParam("{\n" +
					<#list fieldList as field>
					"  \"${field.camelName}\": \"${field.comment}\",\n" +
					</#list>
                    "}")
        @RequestBody @Valid ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = ${entityName}Service.save(${entityName}DTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ${entityName}DTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改${entityComment}")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/${entityUrl}")
    public ResponseEntity<ReturnCommonDTO> update${eentityName}(
            @ApiParam("{\n" +
                    <#list fieldList as field>
					"  \"${field.camelName}\": \"${field.comment}\",\n" +
					</#list>
                    "}")
        @RequestBody @Valid ${eentityName}DTO ${entityName}DTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改${eentityName} : {}", ${entityName}DTO);
        if (${entityName}DTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = ${entityName}Service.save(${entityName}DTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除${entityComment}")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/${entityUrl}/{id}")
    public ResponseEntity<ReturnCommonDTO> delete${eentityName}(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除${eentityName} : {}", id);
        ReturnCommonDTO resultDTO = ${entityName}Service.deleteById(id);
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
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段${associationNameComment}")
    })
    @GetMapping("/${entityUrl}/{id}")
    public ResponseEntity<${eentityName}DTO> get${eentityName}(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询${eentityName} : {}, {}", id, criteria);
        Optional<${eentityName}DTO> data = ${entityName}Service.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条${entityComment}数据
     */
    @ApiOperation(value="获取所有-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段${associationNameComment}"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
			<#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals",value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals",value="关联的${toFrom.toFromComment}，?对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @GetMapping("/${entityUrl}-all")
    public ResponseEntity<List<${eentityName}DTO>> getAll${eentityName}s(${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询所有${eentityName} : {}", criteria);
        List<${eentityName}DTO> list = ${entityName}Service.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页${entityComment}数据
     */
    @ApiOperation(value="获取分页-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段${associationNameComment}"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current",value="分页：当前页"),
            @ApiImplicitParam(name="size",value="分页：每页大小"),
            <#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals",value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals",value="关联的${toFrom.toFromComment}，?对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @GetMapping("/${entityUrl}")
    public ResponseEntity<IPage<${eentityName}DTO>> getPage${eentityName}s(${eentityName}Criteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询${eentityName} : {}, {}", criteria, pageable);
        IPage<${eentityName}DTO> page = ${entityName}Service.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的${entityComment}数量
     */
    @ApiOperation(value="获取数量-${entityComment}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            <#list fieldList as field>
            @ApiImplicitParam(name="${field.camelName}.equals",value="${field.comment}"),
			</#list>
			<#list toFromList as toFrom>
			@ApiImplicitParam(name="${toFrom.toFromEntityName}.?.equals",value="关联的${toFrom.toFromComment}，?对应于GET /api/${toFrom.toFromEntityUrl}的查询字段"),
			</#list>
    })
    @GetMapping("/${entityUrl}-count")
    public ResponseEntity<Integer> get${eentityName}Count(${eentityName}Criteria criteria) {
        log.debug("Controller ==> 查询数量${eentityName} : {}", criteria);
        int count = ${entityName}Service.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
