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
@Api(description="资源")
@RestController
@RequestMapping("/api")
public class SystemResourceController {

    private final Logger log = LoggerFactory.getLogger(SystemResourceController.class);

    private static final String ENTITY_NAME = "systemResource";

    @Autowired
    private SystemResourceService systemResourceService;

    /**
     * 新增
     * @param systemResourceDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增资源")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-resource")
    public ResponseEntity<ReturnCommonDTO> createSystemResource(
            @ApiParam("{\n" +
					"  \"resourceType\": \"资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）\",\n" +
					"  \"name\": \"资源名称\",\n" +
					"  \"description\": \"资源描述\",\n" +
					"  \"systemCode\": \"资源所属系统代码（只有在多系统联合配置权限时使用）\",\n" +
					"  \"identify\": \"资源特性标识（同一系统，同一类别内资源特性标识）\",\n" +
					"  \"currentLevel\": \"当前层级\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemResourceDTO systemResourceDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemResource : {}", systemResourceDTO);
        if (systemResourceDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourceService.save(systemResourceDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemResourceDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改资源")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-resource")
    public ResponseEntity<ReturnCommonDTO> updateSystemResource(
            @ApiParam("{\n" +
					"  \"resourceType\": \"资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）\",\n" +
					"  \"name\": \"资源名称\",\n" +
					"  \"description\": \"资源描述\",\n" +
					"  \"systemCode\": \"资源所属系统代码（只有在多系统联合配置权限时使用）\",\n" +
					"  \"identify\": \"资源特性标识（同一系统，同一类别内资源特性标识）\",\n" +
					"  \"currentLevel\": \"当前层级\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemResourceDTO systemResourceDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemResource : {}", systemResourceDTO);
        if (systemResourceDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourceService.save(systemResourceDTO);
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
    @ApiOperation(value="单个删除资源")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-resource/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemResource(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemResource : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourceService.deleteById(id);
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
    @ApiOperation(value="批量删除资源")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-resource")
    public ResponseEntity<ReturnCommonDTO> deleteSystemResources(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemResource : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemResourceService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条资源数据
     */
    @ApiOperation(value="获取单条-资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子资源列表、systemResourcePermissionList：许可列表、systemRoleResourceList：角色列表、systemUserResourceList：用户列表、parent：父资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-resource/{id}")
    public ResponseEntity<SystemResourceDTO> getSystemResource(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemResource : {}, {}", id, criteria);
        Optional<SystemResourceDTO> data = systemResourceService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条资源数据
     */
    @ApiOperation(value="获取所有-资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子资源列表、systemResourcePermissionList：许可列表、systemRoleResourceList：角色列表、systemUserResourceList：用户列表、parent：父资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="resourceType.equals", paramType="path", value="资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="资源名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="资源描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="资源所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="identify.equals", paramType="path", value="资源特性标识（同一系统，同一类别内资源特性标识）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-resource-all")
    public ResponseEntity<List<SystemResourceDTO>> getAllSystemResources(SystemResourceCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemResource : {}", criteria);
        List<SystemResourceDTO> list = systemResourceService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页资源数据
     */
    @ApiOperation(value="获取分页-资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（childList：子资源列表、systemResourcePermissionList：许可列表、systemRoleResourceList：角色列表、systemUserResourceList：用户列表、parent：父资源）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="resourceType.equals", paramType="path", value="资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="资源名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="资源描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="资源所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="identify.equals", paramType="path", value="资源特性标识（同一系统，同一类别内资源特性标识）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-resource")
    public ResponseEntity<IPage<SystemResourceDTO>> getPageSystemResources(SystemResourceCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemResource : {}, {}", criteria, pageable);
        IPage<SystemResourceDTO> page = systemResourceService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的资源数量
     */
    @ApiOperation(value="获取数量-资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="resourceType.equals", paramType="path", value="资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="资源名称"),
            @ApiImplicitParam(name="description.equals", paramType="path", value="资源描述"),
            @ApiImplicitParam(name="systemCode.equals", paramType="path", value="资源所属系统代码（只有在多系统联合配置权限时使用）"),
            @ApiImplicitParam(name="identify.equals", paramType="path", value="资源特性标识（同一系统，同一类别内资源特性标识）"),
            @ApiImplicitParam(name="currentLevel.equals", paramType="path", value="当前层级"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="parent.?.equals", paramType="path", value="关联的父资源，其中 ? 对应于GET /api/system-resource的查询字段"),
    })
    @GetMapping("/system-resource-count")
    public ResponseEntity<Integer> getSystemResourceCount(SystemResourceCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemResource : {}", criteria);
        int count = systemResourceService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
