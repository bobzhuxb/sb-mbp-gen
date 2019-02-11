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
@Api(description="会议嘉宾照片")
@RestController
@RequestMapping("/api")
public class SmMeetingGuestPhotoController {

    private final Logger log = LoggerFactory.getLogger(SmMeetingGuestPhotoController.class);

    private static final String ENTITY_NAME = "smMeetingGuestPhoto";

    @Autowired
    private SmMeetingGuestPhotoService smMeetingGuestPhotoService;

    /**
     * 新增
     * @param smMeetingGuestPhotoDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增会议嘉宾照片")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-meeting-guest-photo")
    public ResponseEntity<ReturnCommonDTO> createSmMeetingGuestPhoto(
            @ApiParam("{\n" +
					"  \"relativePath\": \"照片相对路径\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmMeetingGuestPhoto : {}", smMeetingGuestPhotoDTO);
        if (smMeetingGuestPhotoDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smMeetingGuestPhotoService.save(smMeetingGuestPhotoDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smMeetingGuestPhotoDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改会议嘉宾照片")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-meeting-guest-photo")
    public ResponseEntity<ReturnCommonDTO> updateSmMeetingGuestPhoto(
            @ApiParam("{\n" +
					"  \"relativePath\": \"照片相对路径\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmMeetingGuestPhoto : {}", smMeetingGuestPhotoDTO);
        if (smMeetingGuestPhotoDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smMeetingGuestPhotoService.save(smMeetingGuestPhotoDTO);
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
    @ApiOperation(value="删除会议嘉宾照片")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-meeting-guest-photo/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmMeetingGuestPhoto(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmMeetingGuestPhoto : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smMeetingGuestPhotoService.deleteById(id);
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
     * @return 使用ResponseEntity封装的单条会议嘉宾照片数据
     */
    @ApiOperation(value="获取单条-会议嘉宾照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（smMeetingGuest：会议嘉宾）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/sm-meeting-guest-photo/{id}")
    public ResponseEntity<SmMeetingGuestPhotoDTO> getSmMeetingGuestPhoto(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmMeetingGuestPhoto : {}, {}", id, criteria);
        Optional<SmMeetingGuestPhotoDTO> data = smMeetingGuestPhotoService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条会议嘉宾照片数据
     */
    @ApiOperation(value="获取所有-会议嘉宾照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（smMeetingGuest：会议嘉宾）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="relativePath.equals", paramType="path", value="照片相对路径"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="smMeetingGuest.?.equals", paramType="path", value="关联的会议嘉宾，其中 ? 对应于GET /api/sm-meeting-guest的查询字段"),
    })
    @GetMapping("/sm-meeting-guest-photo-all")
    public ResponseEntity<List<SmMeetingGuestPhotoDTO>> getAllSmMeetingGuestPhotos(SmMeetingGuestPhotoCriteria criteria) {
        log.debug("Controller ==> 查询所有SmMeetingGuestPhoto : {}", criteria);
        List<SmMeetingGuestPhotoDTO> list = smMeetingGuestPhotoService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页会议嘉宾照片数据
     */
    @ApiOperation(value="获取分页-会议嘉宾照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（smMeetingGuest：会议嘉宾）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="relativePath.equals", paramType="path", value="照片相对路径"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="smMeetingGuest.?.equals", paramType="path", value="关联的会议嘉宾，其中 ? 对应于GET /api/sm-meeting-guest的查询字段"),
    })
    @GetMapping("/sm-meeting-guest-photo")
    public ResponseEntity<IPage<SmMeetingGuestPhotoDTO>> getPageSmMeetingGuestPhotos(SmMeetingGuestPhotoCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmMeetingGuestPhoto : {}, {}", criteria, pageable);
        IPage<SmMeetingGuestPhotoDTO> page = smMeetingGuestPhotoService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的会议嘉宾照片数量
     */
    @ApiOperation(value="获取数量-会议嘉宾照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="relativePath.equals", paramType="path", value="照片相对路径"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
			@ApiImplicitParam(name="smMeetingGuest.?.equals", paramType="path", value="关联的会议嘉宾，其中 ? 对应于GET /api/sm-meeting-guest的查询字段"),
    })
    @GetMapping("/sm-meeting-guest-photo-count")
    public ResponseEntity<Integer> getSmMeetingGuestPhotoCount(SmMeetingGuestPhotoCriteria criteria) {
        log.debug("Controller ==> 查询数量SmMeetingGuestPhoto : {}", criteria);
        int count = smMeetingGuestPhotoService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
