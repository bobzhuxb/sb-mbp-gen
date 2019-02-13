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
@Api(description="用户")
@RestController
@RequestMapping("/api")
public class SystemUserController {

    private final Logger log = LoggerFactory.getLogger(SystemUserController.class);

    private static final String ENTITY_NAME = "systemUser";

    @Autowired
    private SystemUserService systemUserService;

    /**
     * 新增
     * @param systemUserDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增用户")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/system-user")
    public ResponseEntity<ReturnCommonDTO> createSystemUser(
            @ApiParam("{\n" +
					"  \"login\": \"登录账号\",\n" +
					"  \"password\": \"密码\",\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"cell\": \"手机号\",\n" +
					"  \"contractInfo\": \"其他联系方式\",\n" +
					"  \"identifyNo\": \"身份证号\",\n" +
					"  \"email\": \"邮箱\",\n" +
					"  \"imgRelativePath\": \"头像图片相对路径\",\n" +
					"  \"memo\": \"备注\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserDTO systemUserDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SystemUser : {}", systemUserDTO);
        if (systemUserDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserService.save(systemUserDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param systemUserDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改用户")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/system-user")
    public ResponseEntity<ReturnCommonDTO> updateSystemUser(
            @ApiParam("{\n" +
					"  \"login\": \"登录账号\",\n" +
					"  \"password\": \"密码\",\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"cell\": \"手机号\",\n" +
					"  \"contractInfo\": \"其他联系方式\",\n" +
					"  \"identifyNo\": \"身份证号\",\n" +
					"  \"email\": \"邮箱\",\n" +
					"  \"imgRelativePath\": \"头像图片相对路径\",\n" +
					"  \"memo\": \"备注\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SystemUserDTO systemUserDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SystemUser : {}", systemUserDTO);
        if (systemUserDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserService.save(systemUserDTO);
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
    @ApiOperation(value="单个删除用户")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUser(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SystemUser : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserService.deleteById(id);
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
    @ApiOperation(value="批量删除用户")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/system-user")
    public ResponseEntity<ReturnCommonDTO> deleteSystemUsers(
            @ApiParam("[\n" +
					"id1,id2,id3\n" +
                    "]")
        @RequestBody List<Long> idList) {
        log.debug("Controller ==> 批量删除SystemUser : {}", idList);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = systemUserService.deleteByIdList(idList);
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
     * @return 使用ResponseEntity封装的单条用户数据
     */
    @ApiOperation(value="获取单条-用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUserRoleList：角色列表、systemUserResourceList：资源列表、systemOrganization：组织机构）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/system-user/{id}")
    public ResponseEntity<SystemUserDTO> getSystemUser(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SystemUser : {}, {}", id, criteria);
        Optional<SystemUserDTO> data = systemUserService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条用户数据
     */
    @ApiOperation(value="获取所有-用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUserRoleList：角色列表、systemUserResourceList：资源列表、systemOrganization：组织机构）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="login.equals", paramType="path", value="登录账号"),
            @ApiImplicitParam(name="password.equals", paramType="path", value="密码"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="contractInfo.equals", paramType="path", value="其他联系方式"),
            @ApiImplicitParam(name="identifyNo.equals", paramType="path", value="身份证号"),
            @ApiImplicitParam(name="email.equals", paramType="path", value="邮箱"),
            @ApiImplicitParam(name="imgRelativePath.equals", paramType="path", value="头像图片相对路径"),
            @ApiImplicitParam(name="memo.equals", paramType="path", value="备注"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemOrganization.?.equals", paramType="path", value="关联的组织机构，其中 ? 对应于GET /api/system-organization的查询字段"),
    })
    @GetMapping("/system-user-all")
    public ResponseEntity<List<SystemUserDTO>> getAllSystemUsers(SystemUserCriteria criteria) {
        log.debug("Controller ==> 查询所有SystemUser : {}", criteria);
        List<SystemUserDTO> list = systemUserService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页用户数据
     */
    @ApiOperation(value="获取分页-用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（systemUserRoleList：角色列表、systemUserResourceList：资源列表、systemOrganization：组织机构）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="login.equals", paramType="path", value="登录账号"),
            @ApiImplicitParam(name="password.equals", paramType="path", value="密码"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="contractInfo.equals", paramType="path", value="其他联系方式"),
            @ApiImplicitParam(name="identifyNo.equals", paramType="path", value="身份证号"),
            @ApiImplicitParam(name="email.equals", paramType="path", value="邮箱"),
            @ApiImplicitParam(name="imgRelativePath.equals", paramType="path", value="头像图片相对路径"),
            @ApiImplicitParam(name="memo.equals", paramType="path", value="备注"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemOrganization.?.equals", paramType="path", value="关联的组织机构，其中 ? 对应于GET /api/system-organization的查询字段"),
    })
    @GetMapping("/system-user")
    public ResponseEntity<IPage<SystemUserDTO>> getPageSystemUsers(SystemUserCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SystemUser : {}, {}", criteria, pageable);
        IPage<SystemUserDTO> page = systemUserService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的用户数量
     */
    @ApiOperation(value="获取数量-用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="login.equals", paramType="path", value="登录账号"),
            @ApiImplicitParam(name="password.equals", paramType="path", value="密码"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="contractInfo.equals", paramType="path", value="其他联系方式"),
            @ApiImplicitParam(name="identifyNo.equals", paramType="path", value="身份证号"),
            @ApiImplicitParam(name="email.equals", paramType="path", value="邮箱"),
            @ApiImplicitParam(name="imgRelativePath.equals", paramType="path", value="头像图片相对路径"),
            @ApiImplicitParam(name="memo.equals", paramType="path", value="备注"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="systemOrganization.?.equals", paramType="path", value="关联的组织机构，其中 ? 对应于GET /api/system-organization的查询字段"),
    })
    @GetMapping("/system-user-count")
    public ResponseEntity<Integer> getSystemUserCount(SystemUserCriteria criteria) {
        log.debug("Controller ==> 查询数量SystemUser : {}", criteria);
        int count = systemUserService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
