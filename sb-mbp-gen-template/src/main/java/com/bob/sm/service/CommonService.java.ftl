package ${packageName}.service;

import ${packageName}.dto.help.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 共通方法
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

    <T> ReturnCommonDTO<List<T>> importParseExcel(String fullFileName, Class<T> excelParseClass);

    <T> ReturnCommonDTO<List<T>> importParseExcel(InputStream fileInputStream, Class<T> excelParseClass);

    <O> ReturnCommonDTO<O> doGetSingleResult(ReturnCommonDTO<List<O>> resultOfList);

    ReturnCommonDTO<String> getDataFromCache(String cacheName, String key);

    ReturnCommonDTO<String> getDataFromLocalCache(String key);

    ReturnCommonDTO<Integer> getTtlFromLocalCache(String key);

}
