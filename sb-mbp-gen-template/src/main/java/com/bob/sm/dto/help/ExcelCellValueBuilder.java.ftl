package ${packageName}.dto.help;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel单元格Builder（数据）
 * @author Bob
 */
public class ExcelCellValueBuilder implements ExcelCellBuilder {

    private ExcelCellDTO excelCellDTO;

    public ExcelCellValueBuilder(int relativeRow, int column, String value) {
        ExcelCellDTO excelCellDTO = new ExcelCellDTO().setRelativeRow(relativeRow).setColumn(column).setValue(value);
        this.excelCellDTO = excelCellDTO;
    }

    /**
     * 构建单元格
     * @return
     */
    @Override
    public ExcelCellDTO buildCell() {
        return excelCellDTO.setHorizontal(HorizontalAlignment.CENTER);
    }

}
