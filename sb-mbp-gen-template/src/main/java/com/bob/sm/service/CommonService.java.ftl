package ${packageName}.service;

import ${packageName}.dto.help.*;
import ${packageName}.util.IExcelReadRowHandler;
import ${packageName}.util.IExcelSaxRowRead;
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

    void exportExcel(HttpServletResponse response, ExcelExportDTO excelExportDTO);

    void exportExcel(HttpServletResponse response, List<ExcelExportDTO> excelExportDTOList);

    void encryptExcel(String fullFileName, String password) throws Exception;

    <T> ReturnCommonDTO<List<T>> importParseExcel(String fullFileName, Class<T> excelParseClass,
                                                  Integer maxErrHintCount);

    <T> ReturnCommonDTO<List<T>> importParseExcel(String excelType, InputStream fileInputStream,
                                                  Class<T> excelParseClass, Integer maxErrHintCount);

    <T> ReturnCommonDTO<List<T>> importParseExcelSmall(String fullFileName, Class<T> excelParseClass,
                                                       Integer maxErrHintCount);

    <T> ReturnCommonDTO<List<T>> importParseExcelSmall(String excelType, InputStream fileInputStream,
                                                       Class<T> excelParseClass, Integer maxErrHintCount);

    <T> ReturnCommonDTO<List<T>> importParseExcelLarge(String fullFileName, Class<T> excelParseClass,
                                                       IExcelSaxRowRead rowRead, IExcelReadRowHandler handler,
                                                       Integer maxErrHintCount);

    <T> ReturnCommonDTO<List<T>> importParseExcelLarge(String excelType, InputStream fileInputStream,
                                                       Class<T> excelParseClass, IExcelSaxRowRead rowRead,
                                                       IExcelReadRowHandler handler, Integer maxErrHintCount);

    <O> ReturnCommonDTO<O> doGetSingleResult(ReturnCommonDTO<List<O>> resultOfList);

    ReturnCommonDTO<String> getDataFromCache(String cacheName, String key);

    ReturnCommonDTO<String> getDataFromLocalCache(String key);

    ReturnCommonDTO<Integer> getTtlFromLocalCache(String key);

}
