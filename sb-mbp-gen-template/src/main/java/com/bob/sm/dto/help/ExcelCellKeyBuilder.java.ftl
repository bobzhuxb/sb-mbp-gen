package ${packageName}.dto.help;

import ${packageName}.config.Constants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel单元格Builder（Key）
 * @author Bob
 */
public class ExcelCellKeyBuilder implements ExcelCellBuilder {

    private ExcelCellDTO excelCellDTO;

    public ExcelCellKeyBuilder(int relativeRow, int column, String value) {
        ExcelCellDTO excelCellDTO = new ExcelCellDTO().setRelativeRow(relativeRow).setColumn(column).setValue(value);
        this.excelCellDTO = excelCellDTO;
    }

    /**
     * 构建单元格
     * @return
     */
    @Override
    public ExcelCellDTO buildCell() {
        return excelCellDTO.setHorizontal(HorizontalAlignment.CENTER).setBackgroundColor(Constants.EXCEL_THEME_COLOR);
    }

}
