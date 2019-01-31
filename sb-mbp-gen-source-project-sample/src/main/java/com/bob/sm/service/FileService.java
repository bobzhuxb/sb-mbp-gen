package com.bob.sm.service;

import com.bob.sm.dto.help.ReturnFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ReturnFileUploadDTO uploadFile(MultipartFile file) throws Exception;

}
