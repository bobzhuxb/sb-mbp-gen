package ${packageName}.service;

import ${packageName}.dto.help.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface CommonService {

    ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file);

    ReturnCommonDTO downloadFile(HttpServletResponse response, File file, String changeFileName);

    ReturnCommonDTO downloadFile(HttpServletResponse response, String fullFileName, String changeFileName);

    ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                int maxColumn, int tableStartRow, List<ExcelCellDTO> beforeDataCellList,
                                List<ExcelCellDTO> afterDataCellList, List<ExcelTitleDTO> titleList, List<?> dataList,
                                List<ExcelCellRangeDTO> cellRangeList);

    ReturnCommonDTO<List<Map<String, String>>> importParseExcel(String fullFileName, int columnCount,
                                List<String> columnNameList, List<String> columnKeyList, List<String> regexList,
                                List<Boolean> allowNullList);

}
