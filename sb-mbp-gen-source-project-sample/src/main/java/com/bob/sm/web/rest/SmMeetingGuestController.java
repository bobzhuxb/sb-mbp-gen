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
@Api(description="会议嘉宾")
@RestController
@RequestMapping("/api")
public class SmMeetingGuestController {

    private final Logger log = LoggerFactory.getLogger(SmMeetingGuestController.class);

    private static final String ENTITY_NAME = "smMeetingGuest";

    @Autowired
    private SmMeetingGuestService smMeetingGuestService;

    /**
     * 新增
     * @param smMeetingGuestDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增会议嘉宾")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-meeting-guests")
    public ResponseEntity<ReturnCommonDTO> createSmMeetingGuest(
            @ApiParam("{\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"desc\": \"描述\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingGuestDTO smMeetingGuestDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmMeetingGuest : {}", smMeetingGuestDTO);
        if (smMeetingGuestDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingGuestService.save(smMeetingGuestDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smMeetingGuestDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改会议嘉宾")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-meeting-guests")
    public ResponseEntity<ReturnCommonDTO> updateSmMeetingGuest(
            @ApiParam("{\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"desc\": \"描述\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingGuestDTO smMeetingGuestDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmMeetingGuest : {}", smMeetingGuestDTO);
        if (smMeetingGuestDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingGuestService.save(smMeetingGuestDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除会议嘉宾")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-meeting-guests/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmMeetingGuest(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmMeetingGuest : {}", id);
        ReturnCommonDTO resultDTO = smMeetingGuestService.deleteById(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param id 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条会议嘉宾数据
     */
    @ApiOperation(value="获取单条-会议嘉宾")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段")
    })
    @GetMapping("/sm-meeting-guests/{id}")
    public ResponseEntity<SmMeetingGuestDTO> getSmMeetingGuest(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmMeetingGuest : {}, {}", id, criteria);
        Optional<SmMeetingGuestDTO> data = smMeetingGuestService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条会议嘉宾数据
     */
    @ApiOperation(value="获取所有-会议嘉宾")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals",value="姓名"),
            @ApiImplicitParam(name="desc.equals",value="描述")
    })
    @GetMapping("/sm-meeting-guest-all")
    public ResponseEntity<List<SmMeetingGuestDTO>> getAllSmMeetingGuests(SmMeetingGuestCriteria criteria) {
        log.debug("Controller ==> 查询所有SmMeetingGuest : {}", criteria);
        List<SmMeetingGuestDTO> list = smMeetingGuestService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页会议嘉宾数据
     */
    @ApiOperation(value="获取分页-会议嘉宾")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current",value="分页：当前页"),
            @ApiImplicitParam(name="size",value="分页：每页大小"),
            @ApiImplicitParam(name="name.equals",value="姓名"),
            @ApiImplicitParam(name="desc.equals",value="描述")
    })
    @GetMapping("/sm-meeting-guests")
    public ResponseEntity<IPage<SmMeetingGuestDTO>> getPageSmMeetingGuests(SmMeetingGuestCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmMeetingGuest : {}, {}", criteria, pageable);
        IPage<SmMeetingGuestDTO> page = smMeetingGuestService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的会议嘉宾数量
     */
    @ApiOperation(value="获取数量-会议嘉宾")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals",value="姓名"),
            @ApiImplicitParam(name="desc.equals",value="描述")
    })
    @GetMapping("/sm-meeting-guest-count")
    public ResponseEntity<Integer> getSmMeetingGuestCount(SmMeetingGuestCriteria criteria) {
        log.debug("Controller ==> 查询数量SmMeetingGuest : {}", criteria);
        int count = smMeetingGuestService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
