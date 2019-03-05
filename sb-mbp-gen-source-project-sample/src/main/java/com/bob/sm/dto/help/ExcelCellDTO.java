package com.bob.sm.dto.help;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class ExcelCellDTO {

    private HSSFFont font;                  // 字体
    private HorizontalAlignment horizontal; // 水平对齐方式
    private VerticalAlignment vertical;     // 垂直对齐方式
    private Short backgroundColor;          // 背景色
    private BorderStyle borderTop;          // 上边框
    private BorderStyle borderBottom;       // 下边框
    private BorderStyle borderLeft;         // 左边框
    private BorderStyle borderRight;        // 右边框

    public ExcelCellDTO() {

    }

    public HSSFFont getFont() {
        return font;
    }

    public ExcelCellDTO setFont(HSSFFont font) {
        this.font = font;
        return this;
    }

    public HorizontalAlignment getHorizontal() {
        return horizontal;
    }

    public ExcelCellDTO setHorizontal(HorizontalAlignment horizontal) {
        this.horizontal = horizontal;
        return this;
    }

    public VerticalAlignment getVertical() {
        return vertical;
    }

    public ExcelCellDTO setVertical(VerticalAlignment vertical) {
        this.vertical = vertical;
        return this;
    }

    public Short getBackgroundColor() {
        return backgroundColor;
    }

    public ExcelCellDTO setBackgroundColor(Short backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public BorderStyle getBorderTop() {
        return borderTop;
    }

    public ExcelCellDTO setBorderTop(BorderStyle borderTop) {
        this.borderTop = borderTop;
        return this;
    }

    public BorderStyle getBorderBottom() {
        return borderBottom;
    }

    public ExcelCellDTO setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
        return this;
    }

    public BorderStyle getBorderLeft() {
        return borderLeft;
    }

    public ExcelCellDTO setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
        return this;
    }

    public BorderStyle getBorderRight() {
        return borderRight;
    }

    public ExcelCellDTO setBorderRight(BorderStyle borderRight) {
        this.borderRight = borderRight;
        return this;
    }
}
