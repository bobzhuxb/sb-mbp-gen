package com.bob.sm.web.rest;

import com.bob.sm.annotation.validation.group.ValidateCreateGroup;
import com.bob.sm.config.Constants;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.dto.RdtsOperateRecordDTO;
import com.bob.sm.dto.help.FileDownloadDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.dto.help.ReturnFileUploadDTO;
import com.bob.sm.service.CommonService;
import com.bob.sm.util.ParamValidatorUtil;
import com.bob.sm.web.rest.errors.BadRequestAlertException;
import com.bob.sm.web.rest.errors.CommonException;
import com.bob.sm.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@RequestMapping("/api")
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private CommonService commonService;

    /**
     * 上传文件
     * @param file 待上传的文件
     * @return 上传的文件
     */
    @PostMapping("/file-upload")
    public ResponseEntity<ReturnCommonDTO<ReturnFileUploadDTO>> uploadFile (
            @RequestParam(value = "file", required = false)MultipartFile file) {
        log.debug("REST request to upload file : {}", file.getOriginalFilename());
        ReturnCommonDTO resultDTO = null;
        try {
            ReturnCommonDTO<ReturnFileUploadDTO> fileUploadDTO = commonService.uploadFile(file);
            return ResponseEntity.ok().headers(null).body(fileUploadDTO);
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 下载文件
     * @param fileDownloadDTO 待下载的文件路径和相关信息
     * @return 下载的文件
     */
    @PostMapping("/file-download")
    public ResponseEntity<ReturnCommonDTO> downloadFile (HttpServletResponse response,
            @RequestBody FileDownloadDTO fileDownloadDTO, BindingResult bindingResult) {
        log.debug("REST request to download file : {}", fileDownloadDTO);
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            File downloadFile = new File(ymlConfig.getLocation() + fileDownloadDTO.getRelativePath());
            resultDTO = commonService.downloadFile(response, downloadFile, fileDownloadDTO.getChangeFileName());
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
