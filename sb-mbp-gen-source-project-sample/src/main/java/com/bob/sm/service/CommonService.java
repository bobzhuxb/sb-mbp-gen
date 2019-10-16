package com.bob.sm.service;

import com.bob.sm.dto.help.*;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface CommonService {

    ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file);

    ReturnCommonDTO downloadFile(HttpServletResponse response, File file, String changeFileName);

    ReturnCommonDTO downloadFile(HttpServletResponse response, String fullFileName, String changeFileName);

    ReturnCommonDTO downloadFile(HttpServletResponse response, InputStream inputStream, String changeFileName);

    ReturnCommonDTO downloadFileBase(HttpServletResponse response, InputStream inputStream, String oriFileName,
                                     String changeFileName);

    ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                int maxColumn, int tableStartRow, List<ExcelCellDTO> beforeDataCellList,
                                List<ExcelCellDTO> afterDataCellList, List<ExcelTitleDTO> titleList, List<?> dataList,
                                List<ExcelCellRangeDTO> cellRangeList);

    ReturnCommonDTO<List<Map<String, String>>> importParseExcel(String fullFileName, int columnCount,
                                List<String> columnNameList, List<String> columnKeyList, List<String> regexList,
                                List<Boolean> allowNullList);

    <O> ReturnCommonDTO<O> doWithExceptionHandle(Supplier<ReturnCommonDTO<O>> supplier, Logger log);

    <O> ReturnCommonDTO<O> doGetSingleResult(ReturnCommonDTO<List<O>> resultOfList);

}
