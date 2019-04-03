package ${packageName}.web.rest;

import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import ${packageName}.service.CommonService;
import ${packageName}.web.rest.errors.CommonException;
import ${packageName}.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

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

}
