package com.bob.sm.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bob.sm.config.Constants;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.*;
import com.bob.sm.dto.help.*;
import com.bob.sm.service.*;
import com.bob.sm.util.ParamValidatorUtil;
import com.bob.sm.web.rest.errors.*;
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
@Api(description="数据字典")
@RestController
@RequestMapping("/api")
public class BaseDictionaryController {

    private final Logger log = LoggerFactory.getLogger(BaseDictionaryController.class);

    private static final String ENTITY_NAME = "baseDictionary";

    @Autowired
    private BaseDictionaryService baseDictionaryService;

    /**
     * 新增
     * @param baseDictionaryDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增数据字典")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/base-dictionary")
    public ResponseEntity<ReturnCommonDTO> createBaseDictionary(
            @ApiParam("{\n" +
					"  \"dicType\": \"数据字典类别编码\",\n" +
					"  \"dicCode\": \"数据字典代码\",\n" +
					"  \"dicValue\": \"数据字典中文值\",\n" +
					"  \"dicDiscription\": \"数据字典描述\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid BaseDictionaryDTO baseDictionaryDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增BaseDictionary : {}", baseDictionaryDTO);
        if (baseDictionaryDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = baseDictionaryService.save(baseDictionaryDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param baseDictionaryDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改数据字典")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/base-dictionary")
    public ResponseEntity<ReturnCommonDTO> updateBaseDictionary(
            @ApiParam("{\n" +
					"  \"dicType\": \"数据字典类别编码\",\n" +
					"  \"dicCode\": \"数据字典代码\",\n" +
					"  \"dicValue\": \"数据字典中文值\",\n" +
					"  \"dicDiscription\": \"数据字典描述\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid BaseDictionaryDTO baseDictionaryDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改BaseDictionary : {}", baseDictionaryDTO);
        if (baseDictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = baseDictionaryService.save(baseDictionaryDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除数据字典")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/base-dictionary/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteBaseDictionary(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除BaseDictionary : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = baseDictionaryService.deleteById(id);
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
     * @return 使用ResponseEntity封装的单条数据字典数据
     */
    @ApiOperation(value="获取单条-数据字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子数据字典列表、parent：父数据字典）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/base-dictionary/{id}")
    public ResponseEntity<BaseDictionaryDTO> getBaseDictionary(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询BaseDictionary : {}, {}", id, criteria);
        Optional<BaseDictionaryDTO> data = baseDictionaryService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条数据字典数据
     */
    @ApiOperation(value="获取所有-数据字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子数据字典列表、parent：父数据字典）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="dicType.equals", paramType="path", value="数据字典类别编码"),
            @ApiImplicitParam(name="dicCode.equals", paramType="path", value="数据字典代码"),
            @ApiImplicitParam(name="dicValue.equals", paramType="path", value="数据字典中文值"),
            @ApiImplicitParam(name="dicDiscription.equals", paramType="path", value="数据字典描述"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父数据字典，其中 ? 对应于GET /api/base-dictionary的查询字段"),
    })
    @GetMapping("/base-dictionary-all")
    public ResponseEntity<List<BaseDictionaryDTO>> getAllBaseDictionarys(BaseDictionaryCriteria criteria) {
        log.debug("Controller ==> 查询所有BaseDictionary : {}", criteria);
        List<BaseDictionaryDTO> list = baseDictionaryService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页数据字典数据
     */
    @ApiOperation(value="获取分页-数据字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子数据字典列表、parent：父数据字典）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="dicType.equals", paramType="path", value="数据字典类别编码"),
            @ApiImplicitParam(name="dicCode.equals", paramType="path", value="数据字典代码"),
            @ApiImplicitParam(name="dicValue.equals", paramType="path", value="数据字典中文值"),
            @ApiImplicitParam(name="dicDiscription.equals", paramType="path", value="数据字典描述"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父数据字典，其中 ? 对应于GET /api/base-dictionary的查询字段"),
    })
    @GetMapping("/base-dictionary")
    public ResponseEntity<IPage<BaseDictionaryDTO>> getPageBaseDictionarys(BaseDictionaryCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询BaseDictionary : {}, {}", criteria, pageable);
        IPage<BaseDictionaryDTO> page = baseDictionaryService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的数据字典数量
     */
    @ApiOperation(value="获取数量-数据字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="dicType.equals", paramType="path", value="数据字典类别编码"),
            @ApiImplicitParam(name="dicCode.equals", paramType="path", value="数据字典代码"),
            @ApiImplicitParam(name="dicValue.equals", paramType="path", value="数据字典中文值"),
            @ApiImplicitParam(name="dicDiscription.equals", paramType="path", value="数据字典描述"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父数据字典，其中 ? 对应于GET /api/base-dictionary的查询字段"),
    })
    @GetMapping("/base-dictionary-count")
    public ResponseEntity<Integer> getBaseDictionaryCount(BaseDictionaryCriteria criteria) {
        log.debug("Controller ==> 查询数量BaseDictionary : {}", criteria);
        int count = baseDictionaryService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
