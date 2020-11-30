package ${packageName}.dto.help;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel单元格Builder（带边框的数据）
 * @author Bob
 */
public class ExcelCellValueBuilder implements ExcelCellBuilder {

    private ExcelCellDTO excelCellDTO;

    public ExcelCellValueBuilder(String value, int relativeRow, int column) {
        ExcelCellDTO excelCellDTO = new ExcelCellDTO().setValue(value).setRelativeRow(relativeRow).setColumn(column);
        this.excelCellDTO = excelCellDTO;
    }

    /**
     * 构建单元格
     * @return
     */
    @Override
    public ExcelCellDTO buildCell() {
        return excelCellDTO.setHorizontal(HorizontalAlignment.CENTER).setAllBorder(BorderStyle.THIN);
    }

}
