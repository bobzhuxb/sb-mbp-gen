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
@Api(description="用户资源关系")
@RestController
@RequestMapping("/api")
public class SystemUserResourceController {

    private final Logger log = LoggerFactory.getLogger(SystemUserResourceController.class);

    private static final String ENTITY_NAME = "systemUserResource";

    @Autowired
    private SystemUserResourceService systemUserResourceService;

    /**
     * 新增
     * @param systemUserResourceDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增用户资源关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-user-resource")
    public ResponseEntity<ReturnCommonDTO> createSystemUserResource(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserResourceDTO systemUserResourceDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemUserResource : {}", systemUserResourceDTO);
        if (systemUserResourceDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserResourceService.save(systemUserResourceDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemUserResourceDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改用户资源关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-user-resource")
    public ResponseEntity<ReturnCommonDTO> updateSystemUserResource(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserResourceDTO systemUserResourceDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemUserResource : {}", systemUserResourceDTO);
        if (systemUserResourceDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserResourceService.save(systemUserResourceDTO);
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
    @ApiOperation(value="单个删除用户资源关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user-resource/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUserResource(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemUserResource : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserResourceService.deleteById(id);
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
    @ApiOperation(value="批量删除用户资源关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user-resource")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUserResources(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemUserResource : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserResourceService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条用户资源关系数据
     */
    @ApiOperation(value="获取单条-用户资源关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemResource：资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-user-resource/{id}")
    public ResponseEntity<SystemUserResourceDTO> getSystemUserResource(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemUserResource : {}, {}", id, criteria);
        Optional<SystemUserResourceDTO> data = systemUserResourceService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条用户资源关系数据
     */
    @ApiOperation(value="获取所有-用户资源关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemResource：资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-user-resource-all")
    public ResponseEntity<List<SystemUserResourceDTO>> getAllSystemUserResources(SystemUserResourceCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemUserResource : {}", criteria);
        List<SystemUserResourceDTO> list = systemUserResourceService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页用户资源关系数据
     */
    @ApiOperation(value="获取分页-用户资源关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemResource：资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-user-resource")
    public ResponseEntity<IPage<SystemUserResourceDTO>> getPageSystemUserResources(SystemUserResourceCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemUserResource : {}, {}", criteria, pageable);
        IPage<SystemUserResourceDTO> page = systemUserResourceService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的用户资源关系数量
     */
    @ApiOperation(value="获取数量-用户资源关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-user-resource-count")
    public ResponseEntity<Integer> getSystemUserResourceCount(SystemUserResourceCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemUserResource : {}", criteria);
        int count = systemUserResourceService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
