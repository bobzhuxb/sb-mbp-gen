package ${packageName}.service;

import ${packageName}.dto.help.*;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 共通处理类
 * @author Bob
 */
public interface CommonService {

    ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file);

    ReturnCommonDTO downloadFile(HttpServletResponse response, File file, String changeFileName);

    ReturnCommonDTO downloadFile(HttpServletResponse response, String fullFileName, String changeFileName);

    ReturnCommonDTO downloadFile(HttpServletResponse response, InputStream inputStream, String changeFileName);

    ReturnCommonDTO downloadFileBase(HttpServletResponse response, InputStream inputStream, String oriFileName,
                                     String changeFileName);

    ReturnCommonDTO exportExcel(HttpServletResponse response, ExcelExportDTO excelExportDTO);

    ReturnCommonDTO<List<Map<String, String>>> importParseExcel(String fullFileName, int columnCount,
                                                                List<String> columnNameList, List<String> columnKeyList,
                                                                List<String> regexList, List<Boolean> allowNullList);

    ReturnCommonDTO<List<Map<String, String>>> importParseExcel(InputStream fileInputStream, int columnCount,
                                                                List<String> columnNameList, List<String> columnKeyList,
                                                                List<String> regexList, List<Boolean> allowNullList);

    <O> ReturnCommonDTO<O> doGetSingleResult(ReturnCommonDTO<List<O>> resultOfList);

    ReturnCommonDTO<String> getDataFromCache(String cacheName, String key);

    ReturnCommonDTO<String> getDataFromLocalCache(String key);

    ReturnCommonDTO<Integer> getTtlFromLocalCache(String key);

}
