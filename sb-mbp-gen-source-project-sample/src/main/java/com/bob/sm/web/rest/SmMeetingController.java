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
@Api(description="会议")
@RestController
@RequestMapping("/api")
public class SmMeetingController {

    private final Logger log = LoggerFactory.getLogger(SmMeetingController.class);

    private static final String ENTITY_NAME = "smMeeting";

    @Autowired
    private SmMeetingService smMeetingService;

    /**
     * 新增
     * @param smMeetingDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增会议")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-meetings")
    public ResponseEntity<ReturnCommonDTO> createSmMeeting(
            @ApiParam("{\n" +
					"  \"meetingNo\": \"会议编号\",\n" +
					"  \"name\": \"会议名称\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingDTO smMeetingDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmMeeting : {}", smMeetingDTO);
        if (smMeetingDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingService.save(smMeetingDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smMeetingDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改会议")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-meetings")
    public ResponseEntity<ReturnCommonDTO> updateSmMeeting(
            @ApiParam("{\n" +
					"  \"meetingNo\": \"会议编号\",\n" +
					"  \"name\": \"会议名称\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingDTO smMeetingDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmMeeting : {}", smMeetingDTO);
        if (smMeetingDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingService.save(smMeetingDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除会议")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-meetings/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmMeeting(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmMeeting : {}", id);
        ReturnCommonDTO resultDTO = smMeetingService.deleteById(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param id 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条会议数据
     */
    @ApiOperation(value="获取单条-会议")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段")
    })
    @GetMapping("/sm-meetings/{id}")
    public ResponseEntity<SmMeetingDTO> getSmMeeting(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmMeeting : {}, {}", id, criteria);
        Optional<SmMeetingDTO> data = smMeetingService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条会议数据
     */
    @ApiOperation(value="获取所有-会议")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="meetingNo.equals",value="会议编号"),
            @ApiImplicitParam(name="name.equals",value="会议名称")
    })
    @GetMapping("/sm-meeting-all")
    public ResponseEntity<List<SmMeetingDTO>> getAllSmMeetings(SmMeetingCriteria criteria) {
        log.debug("Controller ==> 查询所有SmMeeting : {}", criteria);
        List<SmMeetingDTO> list = smMeetingService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页会议数据
     */
    @ApiOperation(value="获取分页-会议")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current",value="分页：当前页"),
            @ApiImplicitParam(name="size",value="分页：每页大小"),
            @ApiImplicitParam(name="meetingNo.equals",value="会议编号"),
            @ApiImplicitParam(name="name.equals",value="会议名称")
    })
    @GetMapping("/sm-meetings")
    public ResponseEntity<IPage<SmMeetingDTO>> getPageSmMeetings(SmMeetingCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmMeeting : {}, {}", criteria, pageable);
        IPage<SmMeetingDTO> page = smMeetingService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的会议数量
     */
    @ApiOperation(value="获取数量-会议")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="meetingNo.equals",value="会议编号"),
            @ApiImplicitParam(name="name.equals",value="会议名称")
    })
    @GetMapping("/sm-meeting-count")
    public ResponseEntity<Integer> getSmMeetingCount(SmMeetingCriteria criteria) {
        log.debug("Controller ==> 查询数量SmMeeting : {}", criteria);
        int count = smMeetingService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
