package ${packageName}.web.rest;

import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.FileDownloadDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import ${packageName}.service.CommonService;
import ${packageName}.util.ParamValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 共通Controller
 * @author Bob
 */
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
        ReturnCommonDTO<ReturnFileUploadDTO> resultDTO = commonService.uploadFile(file);
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
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        String relativePath = fileDownloadDTO.getRelativePath();
        if (!relativePath.startsWith(File.separator)) {
            relativePath = File.separator + relativePath;
        }
        File downloadFile = new File(ymlConfig.getLocation() + relativePath);
        ReturnCommonDTO resultDTO = commonService.downloadFile(response, downloadFile, fileDownloadDTO.getChangeFileName());
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
