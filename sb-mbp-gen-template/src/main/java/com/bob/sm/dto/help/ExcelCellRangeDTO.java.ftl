package ${packageName}.dto.help;

public class ExcelCellRangeDTO {

    private int fromRow;
    private int toRow;
    private int fromColumn;
    private int toColumn;

    public ExcelCellRangeDTO() {

    }

    public ExcelCellRangeDTO(int fromRow, int toRow, int fromColumn, int toColumn) {
        this.fromRow = fromRow;
        this.toRow = toRow;
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public int getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(int fromColumn) {
        this.fromColumn = fromColumn;
    }

    public int getToColumn() {
        return toColumn;
    }

    public void setToColumn(int toColumn) {
        this.toColumn = toColumn;
    }
}
