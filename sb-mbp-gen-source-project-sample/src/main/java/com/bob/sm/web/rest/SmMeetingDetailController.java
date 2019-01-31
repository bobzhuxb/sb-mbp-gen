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
@Api(description="会议详细")
@RestController
@RequestMapping("/api")
public class SmMeetingDetailController {

    private final Logger log = LoggerFactory.getLogger(SmMeetingDetailController.class);

    private static final String ENTITY_NAME = "smMeetingDetail";

    @Autowired
    private SmMeetingDetailService smMeetingDetailService;

    /**
     * 新增
     * @param smMeetingDetailDTO 待新增的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="新增会议详细")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PostMapping("/sm-meeting-details")
    public ResponseEntity<ReturnCommonDTO> createSmMeetingDetail(
            @ApiParam("{\n" +
					"  \"detailInfo\": \"详细描述\",\n" +
					"  \"detailViews\": \"详情浏览次数\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingDetailDTO smMeetingDetailDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 新增SmMeetingDetail : {}", smMeetingDetailDTO);
        if (smMeetingDetailDTO.getId() != null) {
            throw new BadRequestAlertException("id必须为空", ENTITY_NAME, "idexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingDetailService.save(smMeetingDetailDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param smMeetingDetailDTO 待修改的实体
	 * @param bindingResult 参数验证结果
     * @return 结果返回码和消息
     */
    @ApiOperation(value="修改会议详细")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @PutMapping("/sm-meeting-details")
    public ResponseEntity<ReturnCommonDTO> updateSmMeetingDetail(
            @ApiParam("{\n" +
					"  \"detailInfo\": \"详细描述\",\n" +
					"  \"detailViews\": \"详情浏览次数\",\n" +
                    "}")
        @RequestBody @Valid SmMeetingDetailDTO smMeetingDetailDTO, BindingResult bindingResult) {
        log.debug("Controller ==> 修改SmMeetingDetail : {}", smMeetingDetailDTO);
        if (smMeetingDetailDTO.getId() == null) {
            throw new BadRequestAlertException("id不得为空", ENTITY_NAME, "idnotexists");
        }
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = smMeetingDetailService.save(smMeetingDetailDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @ApiOperation(value="删除会议详细")
    @ApiResponses({
            @ApiResponse(code=200, message="resultCode - 1：操作成功  2：操作失败\n" +
                    "errMsg - 错误消息")
    })
    @DeleteMapping("/sm-meeting-details/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteSmMeetingDetail(@ApiParam(name="主键ID") @PathVariable Long id) {
        log.debug("Controller ==> 根据ID删除SmMeetingDetail : {}", id);
        ReturnCommonDTO resultDTO = smMeetingDetailService.deleteById(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param id 主键ID
	 * @param criteria 附带查询条件
     * @return 使用ResponseEntity封装的单条会议详细数据
     */
    @ApiOperation(value="获取单条-会议详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段")
    })
    @GetMapping("/sm-meeting-details/{id}")
    public ResponseEntity<SmMeetingDetailDTO> getSmMeetingDetail(@ApiParam(name="主键ID") @PathVariable Long id, BaseCriteria criteria) {
        log.debug("Controller ==> 根据ID查询SmMeetingDetail : {}, {}", id, criteria);
        Optional<SmMeetingDetailDTO> data = smMeetingDetailService.findOne(id, criteria);
        return data.map(response -> ResponseEntity.ok().headers(null).body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条会议详细数据
     */
    @ApiOperation(value="获取所有-会议详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="detailInfo.equals",value="详细描述"),
            @ApiImplicitParam(name="detailViews.equals",value="详情浏览次数")
    })
    @GetMapping("/sm-meeting-detail-all")
    public ResponseEntity<List<SmMeetingDetailDTO>> getAllSmMeetingDetails(SmMeetingDetailCriteria criteria) {
        log.debug("Controller ==> 查询所有SmMeetingDetail : {}", criteria);
        List<SmMeetingDetailDTO> list = smMeetingDetailService.findAll(criteria);
        return ResponseEntity.ok().headers(null).body(list);
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 使用ResponseEntity封装的分页会议详细数据
     */
    @ApiOperation(value="获取分页-会议详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name="associationNameList", value="关联查询的字段"),
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="current",value="分页：当前页"),
            @ApiImplicitParam(name="size",value="分页：每页大小"),
            @ApiImplicitParam(name="detailInfo.equals",value="详细描述"),
            @ApiImplicitParam(name="detailViews.equals",value="详情浏览次数")
    })
    @GetMapping("/sm-meeting-details")
    public ResponseEntity<IPage<SmMeetingDetailDTO>> getPageSmMeetingDetails(SmMeetingDetailCriteria criteria, MbpPage pageable) {
        log.debug("Controller ==> 分页查询SmMeetingDetail : {}, {}", criteria, pageable);
        IPage<SmMeetingDetailDTO> page = smMeetingDetailService.findPage(criteria, pageable);
        return ResponseEntity.ok().headers(null).body(page);
    }

    /**
     * 查询数量
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的会议详细数量
     */
    @ApiOperation(value="获取数量-会议详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderBy",value="排序（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）"),
            @ApiImplicitParam(name="detailInfo.equals",value="详细描述"),
            @ApiImplicitParam(name="detailViews.equals",value="详情浏览次数")
    })
    @GetMapping("/sm-meeting-detail-count")
    public ResponseEntity<Integer> getSmMeetingDetailCount(SmMeetingDetailCriteria criteria) {
        log.debug("Controller ==> 查询数量SmMeetingDetail : {}", criteria);
        int count = smMeetingDetailService.findCount(criteria);
        return ResponseEntity.ok().headers(null).body(count);
    }

}
