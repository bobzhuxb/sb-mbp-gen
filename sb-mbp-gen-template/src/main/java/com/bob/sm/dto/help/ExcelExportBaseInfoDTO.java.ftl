package ${packageName}.dto.help;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;

public class ExcelExportBaseInfoDTO {

    private String fileName;            // 最终文件名

    private SXSSFWorkbook workbook;     // Excel工作簿

    private OutputStream outputStream;  // 输出流

    public ExcelExportBaseInfoDTO() {}

    public ExcelExportBaseInfoDTO(String fileName, SXSSFWorkbook workbook, OutputStream outputStream) {
        this.fileName = fileName;
        this.workbook = workbook;
        this.outputStream = outputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(SXSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
