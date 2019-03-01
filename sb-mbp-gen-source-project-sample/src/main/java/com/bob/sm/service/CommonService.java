package com.bob.sm.service;

import com.bob.sm.dto.help.ExcelTitleDTO;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.dto.help.ReturnFileUploadDTO;
import com.bob.sm.dto.help.ReturnUploadCommonDTO;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface CommonService {

    ReturnUploadCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file);

    ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName, String headTitle,
                                List<ExcelTitleDTO> titleList, List<Object> dataList);

    ReturnCommonDTO<List<Map<String, String>>> importParseExcel(String fullFileName, int columnCount,
                                List<String> columnNameList, List<String> columnKeyList, List<String> regexList,
                                List<Boolean> allowNullList);

    String getCellValueOfExcel(XSSFCell cell);

}
