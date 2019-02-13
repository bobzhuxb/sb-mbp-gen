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
@Api(description="用户角色关系")
@RestController
@RequestMapping("/api")
public class SystemUserRoleController {

    private final Logger log = LoggerFactory.getLogger(SystemUserRoleController.class);

    private static final String ENTITY_NAME = "systemUserRole";

    @Autowired
    private SystemUserRoleService systemUserRoleService;

    /**
     * 新增
     * @param systemUserRoleDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增用户角色关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-user-role")
    public ResponseEntity<ReturnCommonDTO> createSystemUserRole(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserRoleDTO systemUserRoleDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemUserRole : {}", systemUserRoleDTO);
        if (systemUserRoleDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserRoleService.save(systemUserRoleDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemUserRoleDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改用户角色关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-user-role")
    public ResponseEntity<ReturnCommonDTO> updateSystemUserRole(
            @ApiParam("{\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserRoleDTO systemUserRoleDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemUserRole : {}", systemUserRoleDTO);
        if (systemUserRoleDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserRoleService.save(systemUserRoleDTO);
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
    @ApiOperation(value="单个删除用户角色关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user-role/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUserRole(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemUserRole : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserRoleService.deleteById(id);
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
    @ApiOperation(value="批量删除用户角色关系")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user-role")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUserRoles(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemUserRole : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserRoleService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条用户角色关系数据
     */
    @ApiOperation(value="获取单条-用户角色关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemRole：角色）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-user-role/{id}")
    public ResponseEntity<SystemUserRoleDTO> getSystemUserRole(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemUserRole : {}, {}", id, criteria);
        Optional<SystemUserRoleDTO> data = systemUserRoleService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条用户角色关系数据
     */
    @ApiOperation(value="获取所有-用户角色关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemRole：角色）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemRole.?.equals", paramType="path", value="关联的角色，其中 ? 对应于GET /api/system-role的查询字段"),
    })
    @GetMapping("/system-user-role-all")
    public ResponseEntity<List<SystemUserRoleDTO>> getAllSystemUserRoles(SystemUserRoleCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemUserRole : {}", criteria);
        List<SystemUserRoleDTO> list = systemUserRoleService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页用户角色关系数据
     */
    @ApiOperation(value="获取分页-用户角色关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUser：用户、systemRole：角色）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemRole.?.equals", paramType="path", value="关联的角色，其中 ? 对应于GET /api/system-role的查询字段"),
    })
    @GetMapping("/system-user-role")
    public ResponseEntity<IPage<SystemUserRoleDTO>> getPageSystemUserRoles(SystemUserRoleCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemUserRole : {}, {}", criteria, pageable);
        IPage<SystemUserRoleDTO> page = systemUserRoleService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的用户角色关系数量
     */
    @ApiOperation(value="获取数量-用户角色关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemUser.?.equals", paramType="path", value="关联的用户，其中 ? 对应于GET /api/system-user的查询字段"),
			@ApiImplicitParam(name="systemRole.?.equals", paramType="path", value="关联的角色，其中 ? 对应于GET /api/system-role的查询字段"),
    })
    @GetMapping("/system-user-role-count")
    public ResponseEntity<Integer> getSystemUserRoleCount(SystemUserRoleCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemUserRole : {}", criteria);
        int count = systemUserRoleService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
