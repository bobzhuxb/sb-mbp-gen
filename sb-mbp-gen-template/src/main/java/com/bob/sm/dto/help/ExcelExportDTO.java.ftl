package ${packageName}.dto.help;

import java.util.List;
import java.util.Map;

/**
 * 用于Excel导出使用的参数DTO
 * @author Bob
 */
public class ExcelExportDTO {

    private String fileName;                        // Excel文件名（不包含后缀）

    private String sheetName;                       // sheet名

    private int maxColumn;                          // 最大列数（用于自适应列宽）

    private int tableStartRow;                      // 实际表格（包括标题行）的开始行

    private List<ExcelCellDTO> beforeDataCellList;  // 在实际表格前面部分的单元格

    private List<ExcelCellDTO> afterDataCellList;   // 在实际表格后面部分的单元格

    private List<ExcelTitleDTO> titleList;          // 标题行

    private List<?> dataList;                       // 数据

    private List<ExcelCellRangeDTO> cellRangeList;  // 要合并的单元格

    private String wrapSpecial = "\\n";             // 换行符特殊标识

    private Map<Integer, ExcelCellDTO> dataSpecialStyleMap;    // 特殊列的样式（Key：列序号  Value：样式）

    public String getFileName() {
        return fileName;
    }

    public ExcelExportDTO setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelExportDTO setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public int getMaxColumn() {
        return maxColumn;
    }

    public ExcelExportDTO setMaxColumn(int maxColumn) {
        this.maxColumn = maxColumn;
        return this;
    }

    public int getTableStartRow() {
        return tableStartRow;
    }

    public ExcelExportDTO setTableStartRow(int tableStartRow) {
        this.tableStartRow = tableStartRow;
        return this;
    }

    public List<ExcelCellDTO> getBeforeDataCellList() {
        return beforeDataCellList;
    }

    public ExcelExportDTO setBeforeDataCellList(List<ExcelCellDTO> beforeDataCellList) {
        this.beforeDataCellList = beforeDataCellList;
        return this;
    }

    public List<ExcelCellDTO> getAfterDataCellList() {
        return afterDataCellList;
    }

    public ExcelExportDTO setAfterDataCellList(List<ExcelCellDTO> afterDataCellList) {
        this.afterDataCellList = afterDataCellList;
        return this;
    }

    public List<ExcelTitleDTO> getTitleList() {
        return titleList;
    }

    public ExcelExportDTO setTitleList(List<ExcelTitleDTO> titleList) {
        this.titleList = titleList;
        return this;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public ExcelExportDTO setDataList(List<?> dataList) {
        this.dataList = dataList;
        return this;
    }

    public List<ExcelCellRangeDTO> getCellRangeList() {
        return cellRangeList;
    }

    public ExcelExportDTO setCellRangeList(List<ExcelCellRangeDTO> cellRangeList) {
        this.cellRangeList = cellRangeList;
        return this;
    }

    public String getWrapSpecial() {
        return wrapSpecial;
    }

    public ExcelExportDTO setWrapSpecial(String wrapSpecial) {
        this.wrapSpecial = wrapSpecial;
        return this;
    }

    public Map<Integer, ExcelCellDTO> getDataSpecialStyleMap() {
        return dataSpecialStyleMap;
    }

    public ExcelExportDTO setDataSpecialStyleMap(Map<Integer, ExcelCellDTO> dataSpecialStyleMap) {
        this.dataSpecialStyleMap = dataSpecialStyleMap;
        return this;
    }
}
