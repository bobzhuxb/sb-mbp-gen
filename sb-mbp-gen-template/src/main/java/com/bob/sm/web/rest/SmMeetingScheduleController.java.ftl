package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.config.Constants;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.*;
import ${packageName}.service.*;
import ${packageName}.util.ParamValidatorUtil;
import ${packageName}.web.rest.errors.*;
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
@Api(description="会议日程")
@RestController
@RequestMapping("/api")
public class SmMeetingScheduleController {

    private final Logger log = LoggerFactory.getLogger(SmMeetingScheduleController.class);

    private static final String ENTITY_NAME = "smMeetingSchedule";

    @Autowired
    private SmMeetingScheduleService smMeetingScheduleService;

    /**
     * 新增
     * @param smMeetingScheduleDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增会议日程")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-meeting-schedules")
    public ResponseEntity<ReturnCommonDTO> createSmMeetingSchedule(
            @ApiParam("{\n" +
					"  \"startTime\": \"开始日期\",\n" +
					"  \"entTime\": \"结束日期\",\n" +
					"  \"content\": \"内容\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingScheduleDTO smMeetingScheduleDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmMeetingSchedule : {}", smMeetingScheduleDTO);
        if (smMeetingScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingScheduleService.save(smMeetingScheduleDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smMeetingScheduleDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改会议日程")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-meeting-schedules")
    public ResponseEntity<ReturnCommonDTO> updateSmMeetingSchedule(
            @ApiParam("{\n" +
					"  \"startTime\": \"开始日期\",\n" +
					"  \"entTime\": \"结束日期\",\n" +
					"  \"content\": \"内容\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingScheduleDTO smMeetingScheduleDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmMeetingSchedule : {}", smMeetingScheduleDTO);
        if (smMeetingScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingScheduleService.save(smMeetingScheduleDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除会议日程")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-meeting-schedules/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmMeetingSchedule(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmMeetingSchedule : {}", id);
        ReturnCommonDTO resultDTO = smMeetingScheduleService.deleteById(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param id 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条会议日程数据
     */
    @ApiOperation(value="获取单条-会议日程")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段")
    })
    @GetMapping("/sm-meeting-schedules/{id}")
    public ResponseEntity<SmMeetingScheduleDTO> getSmMeetingSchedule(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmMeetingSchedule : {}, {}", id, criteria);
        Optional<SmMeetingScheduleDTO> data = smMeetingScheduleService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条会议日程数据
     */
    @ApiOperation(value="获取所有-会议日程")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="startTime.equals",value="开始日期"),
            @ApiImplicitParam(name="entTime.equals",value="结束日期"),
            @ApiImplicitParam(name="content.equals",value="内容")
    })
    @GetMapping("/sm-meeting-schedule-all")
    public ResponseEntity<List<SmMeetingScheduleDTO>> getAllSmMeetingSchedules(SmMeetingScheduleCriteria criteria) {
        log.debug("Controller ==> 查询所有SmMeetingSchedule : {}", criteria);
        List<SmMeetingScheduleDTO> list = smMeetingScheduleService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页会议日程数据
     */
    @ApiOperation(value="获取分页-会议日程")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current",value="分页：当前页"),
            @ApiImplicitParam(name="size",value="分页：每页大小"),
            @ApiImplicitParam(name="startTime.equals",value="开始日期"),
            @ApiImplicitParam(name="entTime.equals",value="结束日期"),
            @ApiImplicitParam(name="content.equals",value="内容")
    })
    @GetMapping("/sm-meeting-schedules")
    public ResponseEntity<IPage<SmMeetingScheduleDTO>> getPageSmMeetingSchedules(SmMeetingScheduleCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmMeetingSchedule : {}, {}", criteria, pageable);
        IPage<SmMeetingScheduleDTO> page = smMeetingScheduleService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的会议日程数量
     */
    @ApiOperation(value="获取数量-会议日程")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="startTime.equals",value="开始日期"),
            @ApiImplicitParam(name="entTime.equals",value="结束日期"),
            @ApiImplicitParam(name="content.equals",value="内容")
    })
    @GetMapping("/sm-meeting-schedule-count")
    public ResponseEntity<Integer> getSmMeetingScheduleCount(SmMeetingScheduleCriteria criteria) {
        log.debug("Controller ==> 查询数量SmMeetingSchedule : {}", criteria);
        int count = smMeetingScheduleService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
