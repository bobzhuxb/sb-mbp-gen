package com.bob.at.service.impl;

import com.bob.at.dto.help.ReturnFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface CommonService {

    ReturnFileUploadDTO uploadFileToLocal(MultipartFile file, boolean changeFileName, Date nowDate);

}
