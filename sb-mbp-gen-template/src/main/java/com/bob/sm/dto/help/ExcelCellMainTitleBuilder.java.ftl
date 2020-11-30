package ${packageName}.dto.help;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel单元格Builder（主标题）
 * @author Bob
 */
public class ExcelCellMainTitleBuilder implements ExcelCellBuilder {

    private ExcelCellDTO excelCellDTO;

    public ExcelCellMainTitleBuilder(String value, int relativeRow, int column) {
        ExcelCellDTO excelCellDTO = new ExcelCellDTO().setValue(value).setRelativeRow(relativeRow).setColumn(column);
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
