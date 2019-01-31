package ${packageName}.service;

import ${packageName}.dto.help.ReturnFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ReturnFileUploadDTO uploadFile(MultipartFile file) throws Exception;

}
