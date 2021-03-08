package ${packageName}.dto.help;

import ${packageName}.config.Constants;

/**
 * SAX解析的Excel单元格值
 * @author Bob
 */
public class ExcelSaxReadCellDTO {

	private Constants.excelCellValueType cellType;		// 值类型

	private Object value;		// 值

	public ExcelSaxReadCellDTO(Constants.excelCellValueType cellType, Object value) {
		this.cellType = cellType;
		this.value = value;
	}

	public Constants.excelCellValueType getCellType() {
		return cellType;
	}

	public void setCellType(Constants.excelCellValueType cellType) {
		this.cellType = cellType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
