package com.bob.sm.service;

import com.bob.sm.dto.help.ExcelTitleDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.dto.help.ReturnFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface CommonService {

    ReturnFileUploadDTO uploadFile(MultipartFile file) throws Exception;

    ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName, String headTitle,
                                List<ExcelTitleDTO> titleList, List<Object> dataList);

}
