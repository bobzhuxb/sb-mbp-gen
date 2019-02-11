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
@Api(description="工作人员")
@RestController
@RequestMapping("/api")
public class SmPersonController {

    private final Logger log = LoggerFactory.getLogger(SmPersonController.class);

    private static final String ENTITY_NAME = "smPerson";

    @Autowired
    private SmPersonService smPersonService;

    /**
     * 新增
     * @param smPersonDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增工作人员")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-person")
    public ResponseEntity<ReturnCommonDTO> createSmPerson(
            @ApiParam("{\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"cell\": \"手机号\",\n" +
					"  \"nationCode\": \"民族代码\",\n" +
					"  \"countryCode\": \"国家代码\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmPersonDTO smPersonDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmPerson : {}", smPersonDTO);
        if (smPersonDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPersonService.save(smPersonDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smPersonDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改工作人员")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-person")
    public ResponseEntity<ReturnCommonDTO> updateSmPerson(
            @ApiParam("{\n" +
					"  \"name\": \"姓名\",\n" +
					"  \"cell\": \"手机号\",\n" +
					"  \"nationCode\": \"民族代码\",\n" +
					"  \"countryCode\": \"国家代码\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmPersonDTO smPersonDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmPerson : {}", smPersonDTO);
        if (smPersonDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPersonService.save(smPersonDTO);
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
    @ApiOperation(value="删除工作人员")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-person/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmPerson(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmPerson : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPersonService.deleteById(id);
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
     * @return 使用ResponseEntity封装的单条工作人员数据
     */
    @ApiOperation(value="获取单条-工作人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（asmMeetingList：会议列表、bsmMeetingList：会议列表）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值（nationValue：民族值、countryValue：国家值）")
    })
    @GetMapping("/sm-person/{id}")
    public ResponseEntity<SmPersonDTO> getSmPerson(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmPerson : {}, {}", id, criteria);
        Optional<SmPersonDTO> data = smPersonService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条工作人员数据
     */
    @ApiOperation(value="获取所有-工作人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（asmMeetingList：会议列表、bsmMeetingList：会议列表）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值（nationValue：民族值、countryValue：国家值）"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="nationCode.equals", paramType="path", value="民族代码"),
            @ApiImplicitParam(name="countryCode.equals", paramType="path", value="国家代码"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-person-all")
    public ResponseEntity<List<SmPersonDTO>> getAllSmPersons(SmPersonCriteria criteria) {
        log.debug("Controller ==> 查询所有SmPerson : {}", criteria);
        List<SmPersonDTO> list = smPersonService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页工作人员数据
     */
    @ApiOperation(value="获取分页-工作人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段（asmMeetingList：会议列表、bsmMeetingList：会议列表）"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值（nationValue：民族值、countryValue：国家值）"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="nationCode.equals", paramType="path", value="民族代码"),
            @ApiImplicitParam(name="countryCode.equals", paramType="path", value="国家代码"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-person")
    public ResponseEntity<IPage<SmPersonDTO>> getPageSmPersons(SmPersonCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmPerson : {}, {}", criteria, pageable);
        IPage<SmPersonDTO> page = smPersonService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的工作人员数量
     */
    @ApiOperation(value="获取数量-工作人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="name.equals", paramType="path", value="姓名"),
            @ApiImplicitParam(name="cell.equals", paramType="path", value="手机号"),
            @ApiImplicitParam(name="nationCode.equals", paramType="path", value="民族代码"),
            @ApiImplicitParam(name="countryCode.equals", paramType="path", value="国家代码"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-person-count")
    public ResponseEntity<Integer> getSmPersonCount(SmPersonCriteria criteria) {
        log.debug("Controller ==> 查询数量SmPerson : {}", criteria);
        int count = smPersonService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
