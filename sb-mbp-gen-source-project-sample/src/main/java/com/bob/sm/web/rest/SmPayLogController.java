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
@Api(description="支付日志")
@RestController
@RequestMapping("/api")
public class SmPayLogController {

    private final Logger log = LoggerFactory.getLogger(SmPayLogController.class);

    private static final String ENTITY_NAME = "smPayLog";

    @Autowired
    private SmPayLogService smPayLogService;

    /**
     * 新增
     * @param smPayLogDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增支付日志")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-pay-log")
    public ResponseEntity<ReturnCommonDTO> createSmPayLog(
            @ApiParam("{\n" +
					"  \"interType\": \"接口类型\",\n" +
					"  \"param\": \"传入参数\",\n" +
					"  \"result\": \"返回结果\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmPayLogDTO smPayLogDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmPayLog : {}", smPayLogDTO);
        if (smPayLogDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPayLogService.save(smPayLogDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smPayLogDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改支付日志")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-pay-log")
    public ResponseEntity<ReturnCommonDTO> updateSmPayLog(
            @ApiParam("{\n" +
					"  \"interType\": \"接口类型\",\n" +
					"  \"param\": \"传入参数\",\n" +
					"  \"result\": \"返回结果\",\n" +
					"  \"insertUserId\": \"创建者用户ID\",\n" +
					"  \"operateUserId\": \"操作者用户ID\",\n" +
					"  \"insertTime\": \"插入时间\",\n" +
					"  \"updateTime\": \"更新时间\",\n" +
                    "}")
        @RequestBody @Valid SmPayLogDTO smPayLogDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmPayLog : {}", smPayLogDTO);
        if (smPayLogDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPayLogService.save(smPayLogDTO);
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
    @ApiOperation(value="删除支付日志")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-pay-log/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmPayLog(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmPayLog : {}", id);
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = smPayLogService.deleteById(id);
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
     * @return 使用ResponseEntity封装的单条支付日志数据
     */
    @ApiOperation(value="获取单条-支付日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值")
    })
    @GetMapping("/sm-pay-log/{id}")
    public ResponseEntity<SmPayLogDTO> getSmPayLog(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmPayLog : {}, {}", id, criteria);
        Optional<SmPayLogDTO> data = smPayLogService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条支付日志数据
     */
    @ApiOperation(value="获取所有-支付日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="interType.equals", paramType="path", value="接口类型"),
            @ApiImplicitParam(name="param.equals", paramType="path", value="传入参数"),
            @ApiImplicitParam(name="result.equals", paramType="path", value="返回结果"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-pay-log-all")
    public ResponseEntity<List<SmPayLogDTO>> getAllSmPayLogs(SmPayLogCriteria criteria) {
        log.debug("Controller ==> 查询所有SmPayLog : {}", criteria);
        List<SmPayLogDTO> list = smPayLogService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页支付日志数据
     */
    @ApiOperation(value="获取分页-支付日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", paramType="path", value="关联查询的字段"),
            @ApiImplicitParam(name="dictionaryNameList", paramType="path", value="关联查询的数据字典值"),
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current", paramType="path", value="分页：当前页"),
            @ApiImplicitParam(name="size", paramType="path", value="分页：每页大小"),
            @ApiImplicitParam(name="interType.equals", paramType="path", value="接口类型"),
            @ApiImplicitParam(name="param.equals", paramType="path", value="传入参数"),
            @ApiImplicitParam(name="result.equals", paramType="path", value="返回结果"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-pay-log")
    public ResponseEntity<IPage<SmPayLogDTO>> getPageSmPayLogs(SmPayLogCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmPayLog : {}, {}", criteria, pageable);
        IPage<SmPayLogDTO> page = smPayLogService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的支付日志数量
     */
    @ApiOperation(value="获取数量-支付日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy", paramType="path", value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="interType.equals", paramType="path", value="接口类型"),
            @ApiImplicitParam(name="param.equals", paramType="path", value="传入参数"),
            @ApiImplicitParam(name="result.equals", paramType="path", value="返回结果"),
            @ApiImplicitParam(name="insertUserId.equals", paramType="path", value="创建者用户ID"),
            @ApiImplicitParam(name="operateUserId.equals", paramType="path", value="操作者用户ID"),
            @ApiImplicitParam(name="insertTime.equals", paramType="path", value="插入时间"),
            @ApiImplicitParam(name="updateTime.equals", paramType="path", value="更新时间"),
    })
    @GetMapping("/sm-pay-log-count")
    public ResponseEntity<Integer> getSmPayLogCount(SmPayLogCriteria criteria) {
        log.debug("Controller ==> 查询数量SmPayLog : {}", criteria);
        int count = smPayLogService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
