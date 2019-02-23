package ${packageName}.service;

import ${packageName}.dto.help.ExcelTitleDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface CommonService {

    ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file);

    ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName, String headTitle,
                                List<ExcelTitleDTO> titleList, List<Object> dataList);

}
