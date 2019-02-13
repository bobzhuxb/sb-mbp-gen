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
@Api(description="操作许可")
@RestController
@RequestMapping("/api")
public class SystemPermissionController {

    private final Logger log = LoggerFactory.getLogger(SystemPermissionController.class);

    private static final String ENTITY_NAME = "systemPermission";

    @Autowired
    private SystemPermissionService systemPermissionService;

    /**
     * 新增
     * @param systemPermissionDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增操作许可")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-permission")
    public ResponseEntity<ReturnCommonDTO> createSystemPermission(
            @ApiParam("{\n" +
					"  \"name\": \"操作许可名称\",\n" +
					"  \"description\": \"操作许可描述\",\n" +
					"  \"systemCode\": \"许可所属系统代码（只有在多系统联合配置权限时使用）\",\n" +
					"  \"httpType\": \"操作许可类别（例如：GET/POST/PUT/DELETE等）\",\n" +
					"  \"httpUrl\": \"访问URL\",\n" +
					"  \"functionCategroy\": \"功能归类\",\n" +
					"  \"nameModified\": \"名称是否已更改（1：未更改  2：已更改）\",\n" +
					"  \"currentLevel\": \"当前层级\",\n" +
					"  \"allowConfig\": \"是否允许配置（1：是   2：否）\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemPermissionDTO systemPermissionDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemPermission : {}", systemPermissionDTO);
        if (systemPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemPermissionService.save(systemPermissionDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemPermissionDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改操作许可")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-permission")
    public ResponseEntity<ReturnCommonDTO> updateSystemPermission(
            @ApiParam("{\n" +
					"  \"name\": \"操作许可名称\",\n" +
					"  \"description\": \"操作许可描述\",\n" +
					"  \"systemCode\": \"许可所属系统代码（只有在多系统联合配置权限时使用）\",\n" +
					"  \"httpType\": \"操作许可类别（例如：GET/POST/PUT/DELETE等）\",\n" +
					"  \"httpUrl\": \"访问URL\",\n" +
					"  \"functionCategroy\": \"功能归类\",\n" +
					"  \"nameModified\": \"名称是否已更改（1：未更改  2：已更改）\",\n" +
					"  \"currentLevel\": \"当前层级\",\n" +
					"  \"allowConfig\": \"是否允许配置（1：是   2：否）\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemPermissionDTO systemPermissionDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemPermission : {}", systemPermissionDTO);
        if (systemPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemPermissionService.save(systemPermissionDTO);
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
    @ApiOperation(value="单个删除操作许可")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-permission/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemPermission(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemPermission : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemPermissionService.deleteById(id);
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
    @ApiOperation(value="批量删除操作许可")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-permission")
    public ResponseEntity<ReturnCommonDTO> deleteSystemPermissions(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemPermission : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemPermissionService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条操作许可数据
     */
    @ApiOperation(value="获取单条-操作许可")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子操作许可列表、systemResourcePermissionList：资源列表、parent：父操作许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-permission/{id}")
    public ResponseEntity<SystemPermissionDTO> getSystemPermission(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemPermission : {}, {}", id, criteria);
        Optional<SystemPermissionDTO> data = systemPermissionService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条操作许可数据
     */
    @ApiOperation(value="获取所有-操作许可")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子操作许可列表、systemResourcePermissionList：资源列表、parent：父操作许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="操作许可名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="操作许可描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="许可所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="httpType.equals", paramType="path", value="操作许可类别（例如：GET/POST/PUT/DELETE等）"),
            @ApiImplicitParam(name="httpUrl.equals", paramType="path", value="访问URL"),
            @ApiImplicitParam(name="functionCategroy.equals", paramType="path", value="功能归类"),
            @ApiImplicitParam(name="nameModified.equals", paramType="path", value="名称是否已更改（1：未更改  2：已更改）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="allowConfig.equals", paramType="path", value="是否允许配置（1：是   2：否）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父操作许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-permission-all")
    public ResponseEntity<List<SystemPermissionDTO>> getAllSystemPermissions(SystemPermissionCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemPermission : {}", criteria);
        List<SystemPermissionDTO> list = systemPermissionService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页操作许可数据
     */
    @ApiOperation(value="获取分页-操作许可")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子操作许可列表、systemResourcePermissionList：资源列表、parent：父操作许可）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="操作许可名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="操作许可描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="许可所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="httpType.equals", paramType="path", value="操作许可类别（例如：GET/POST/PUT/DELETE等）"),
            @ApiImplicitParam(name="httpUrl.equals", paramType="path", value="访问URL"),
            @ApiImplicitParam(name="functionCategroy.equals", paramType="path", value="功能归类"),
            @ApiImplicitParam(name="nameModified.equals", paramType="path", value="名称是否已更改（1：未更改  2：已更改）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="allowConfig.equals", paramType="path", value="是否允许配置（1：是   2：否）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父操作许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-permission")
    public ResponseEntity<IPage<SystemPermissionDTO>> getPageSystemPermissions(SystemPermissionCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemPermission : {}, {}", criteria, pageable);
        IPage<SystemPermissionDTO> page = systemPermissionService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的操作许可数量
     */
    @ApiOperation(value="获取数量-操作许可")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="操作许可名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="操作许可描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="许可所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="httpType.equals", paramType="path", value="操作许可类别（例如：GET/POST/PUT/DELETE等）"),
            @ApiImplicitParam(name="httpUrl.equals", paramType="path", value="访问URL"),
            @ApiImplicitParam(name="functionCategroy.equals", paramType="path", value="功能归类"),
            @ApiImplicitParam(name="nameModified.equals", paramType="path", value="名称是否已更改（1：未更改  2：已更改）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="allowConfig.equals", paramType="path", value="是否允许配置（1：是   2：否）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父操作许可，其中 ? 对应于GET /api/system-permission的查询字段"),
    })
    @GetMapping("/system-permission-count")
    public ResponseEntity<Integer> getSystemPermissionCount(SystemPermissionCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemPermission : {}", criteria);
        int count = systemPermissionService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
