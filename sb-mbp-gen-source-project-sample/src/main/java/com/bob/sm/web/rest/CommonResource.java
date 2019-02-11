package com.bob.sm.web.rest;

import com.bob.sm.dto.help.ReturnFileUploadDTO;
import com.bob.sm.service.CommonService;
import com.bob.sm.web.rest.errors.InternalServerErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description="共通接口")
@RestController
@RequestMapping("/api")
public class CommonResource {

    private final Logger log = LoggerFactory.getLogger(CommonResource.class);

    @Autowired
    private CommonService commonService;

    /**
     * 上传文件
     * @param file 待上传的文件
     * @return 上传的文件
     */
    @ApiOperation(value="上传文件")
    @PostMapping("/file-upload")
    public ResponseEntity<ReturnFileUploadDTO> uploadFile(
            @ApiParam("{\n" +
                    "  \"file\": \"待上传的文件\",\n" +
                    "}")
            @RequestParam(value = "file", required = false)MultipartFile file) {
        log.debug("REST request to upload file : {}", file.getOriginalFilename());
        try {
            ReturnFileUploadDTO fileUploadDTO = commonService.uploadFile(file);
            return ResponseEntity.ok().headers(null).body(fileUploadDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
