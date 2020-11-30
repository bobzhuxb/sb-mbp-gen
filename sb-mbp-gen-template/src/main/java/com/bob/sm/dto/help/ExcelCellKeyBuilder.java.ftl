package ${packageName}.dto.help;

import ${packageName}.config.Constants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel单元格Builder（带边框的Key）
 * @author Bob
 */
public class ExcelCellKeyBuilder implements ExcelCellBuilder {

    private ExcelCellDTO excelCellDTO;

    public ExcelCellKeyBuilder(String value, int relativeRow, int column) {
        ExcelCellDTO excelCellDTO = new ExcelCellDTO().setValue(value).setRelativeRow(relativeRow).setColumn(column);
        this.excelCellDTO = excelCellDTO;
    }

    /**
     * 构建单元格
     * @return
     */
    @Override
    public ExcelCellDTO buildCell() {
        return excelCellDTO.setHorizontal(HorizontalAlignment.CENTER).setBackgroundColor(Constants.EXCEL_THEME_COLOR)
                .setAllBorder(BorderStyle.THIN);
    }

}
