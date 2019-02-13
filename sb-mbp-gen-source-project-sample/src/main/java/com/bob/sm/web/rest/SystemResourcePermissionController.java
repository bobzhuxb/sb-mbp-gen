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
@Api(description="资源许可关系")
@RestController
@RequestMapping("/api")
public class SystemResourcePermissionController {

    private final Logger log = LoggerFactory.getLogger(SystemResourcePermissionController.class);

    private static final String ENTITY_NAME = "systemResourcePermission";

    @Autowired
    private SystemResourcePermissionService systemResourcePermissionService;

    /**
     * 新增
     * @param systemResourcePermissionDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增资源许可关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-resource-permission")
    public ResponseEntity<ReturnCommonDTO> createSystemResourcePermission(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemResourcePermissionDTO systemResourcePermissionDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemResourcePermission : {}", systemResourcePermissionDTO);
        if (systemResourcePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourcePermissionService.save(systemResourcePermissionDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemResourcePermissionDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改资源许可关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-resource-permission")
    public ResponseEntity<ReturnCommonDTO> updateSystemResourcePermission(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemResourcePermissionDTO systemResourcePermissionDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemResourcePermission : {}", systemResourcePermissionDTO);
        if (systemResourcePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourcePermissionService.save(systemResourcePermissionDTO);
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
    @ApiOperation(value="单个删除资源许可关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-resource-permission/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemResourcePermission(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemResourcePermission : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourcePermissionService.deleteById(id);
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
    @ApiOperation(value="批量删除资源许可关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-resource-permission")
    public ResponseEntity<ReturnCommonDTO> deleteSystemResourcePermissions(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemResourcePermission : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourcePermissionService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条资源许可关系数据
     */
    @ApiOperation(value="获取单条-资源许可关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemResource：资源、systemPermission：许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-resource-permission/{id}")
    public ResponseEntity<SystemResourcePermissionDTO> getSystemResourcePermission(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemResourcePermission : {}, {}", id, criteria);
        Optional<SystemResourcePermissionDTO> data = systemResourcePermissionService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条资源许可关系数据
     */
    @ApiOperation(value="获取所有-资源许可关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemResource：资源、systemPermission：许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
			@ApiImplicitParam(name="systemPermission.?.equals", paramType="path", value="关联的许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-resource-permission-all")
    public ResponseEntity<List<SystemResourcePermissionDTO>> getAllSystemResourcePermissions(SystemResourcePermissionCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemResourcePermission : {}", criteria);
        List<SystemResourcePermissionDTO> list = systemResourcePermissionService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页资源许可关系数据
     */
    @ApiOperation(value="获取分页-资源许可关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemResource：资源、systemPermission：许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
			@ApiImplicitParam(name="systemPermission.?.equals", paramType="path", value="关联的许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-resource-permission")
    public ResponseEntity<IPage<SystemResourcePermissionDTO>> getPageSystemResourcePermissions(SystemResourcePermissionCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemResourcePermission : {}, {}", criteria, pageable);
        IPage<SystemResourcePermissionDTO> page = systemResourcePermissionService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的资源许可关系数量
     */
    @ApiOperation(value="获取数量-资源许可关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemResource.?.equals", paramType="path", value="关联的资源，其中 ? 对应于GET /api/system-resource的查询字段"),
			@ApiImplicitParam(name="systemPermission.?.equals", paramType="path", value="关联的许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-resource-permission-count")
    public ResponseEntity<Integer> getSystemResourcePermissionCount(SystemResourcePermissionCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemResourcePermission : {}", criteria);
        int count = systemResourcePermissionService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
