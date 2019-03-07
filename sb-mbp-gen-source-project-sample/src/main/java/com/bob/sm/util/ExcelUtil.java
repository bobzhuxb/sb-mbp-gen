package com.bob.sm.util;

import com.bob.sm.dto.help.ExcelCellDTO;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

    /**
     * 获取Excel的XSSFCell的值
     * @param cell 传入的cell
     * @return cell的值
     */
    public static String getCellValueOfExcel(XSSFCell cell) {
        if (cell != null) {
//            if (xssfRow != null) {
//                xssfRow.setCellType(xssfRow.CELL_TYPE_STRING);
//            }
            if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
                String result = "";
                if (cell.getCellStyle().getDataFormat() == 22) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                return result;
            } else {
                return String.valueOf(cell.getStringCellValue());
            }
        } else {
            return null;
        }
    }

    /**
     * 自适应宽度（中文支持）
     * @param sheet
     * @param size
     */
    public static void setSizeColumn(SXSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                SXSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    SXSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    /**
     * 设置背景（注：cellStyle可通过 workbook.createCellStyle() 获取，下同）
     * @param cellStyle 单元格样式
     * @param bg 背景色代码
     * @return 返回设置好的样式
     */
    public static CellStyle setBackgroundColor(CellStyle cellStyle, Short bg) {
        // 背景色代码示例：IndexedColors.WHITE.getIndex();
        if (bg != null) {
            cellStyle.setFillForegroundColor(bg);// 设置背景色
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return cellStyle;
    }

    /**
     * 设置边框
     * @param cellStyle 单元格样式
     * @param top 上边框样式
     * @param bottom 下边框样式
     * @param left 左边框样式
     * @param right 右边框样式
     * @return 返回设置好的样式
     */
    public static CellStyle setBorder(CellStyle cellStyle, BorderStyle top, BorderStyle bottom,
                                      BorderStyle left, BorderStyle right) {
        Optional.ofNullable(top).ifPresent(topIn -> cellStyle.setBorderTop(topIn));//上边框
        Optional.ofNullable(bottom).ifPresent(bottomIn -> cellStyle.setBorderBottom(bottomIn)); //下边框
        Optional.ofNullable(left).ifPresent(leftIn -> cellStyle.setBorderLeft(leftIn));//左边框
        Optional.ofNullable(right).ifPresent(rightIn -> cellStyle.setBorderRight(rightIn));//右边框
        return cellStyle;
    }

    /**
     * 设置居中/居左/居右
     * @param cellStyle 单元格样式
     * @param horizontal 水平
     * @param vertical 垂直
     * @return 返回设置好的样式
     */
    public static CellStyle setAlignment(CellStyle cellStyle, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        Optional.ofNullable(horizontal).ifPresent(horizontalIn -> cellStyle.setAlignment(horizontalIn));//水平
        Optional.ofNullable(vertical).ifPresent(verticalIn -> cellStyle.setVerticalAlignment(verticalIn));//垂直
        return cellStyle;
    }

    /**
     * 设置字体（注：font可通过 workbook.createFont() 获取，下同）
     * @param cellStyle 单元格样式
     * @param workbook Excel工作簿
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param italic 是否斜体
     * @param bold 是否粗体
     * @param color 字体颜色
     * @return 返回设置好的样式
     * font设置示例：
     *      font.setFontName("黑体");// 设置字体名
     *      font.setFontHeightInPoints((short) 16);// 设置字体大小
     *      font.setItalic(true);// 设置为斜体
     *      font.setBold(true);// 设置为粗体
     *      font.setColor(IndexedColors.RED.index);// 设置字体颜色
     */
    public static CellStyle setFont(CellStyle cellStyle, Workbook workbook, String fontName, Short fontSize,
                                    Boolean italic, Boolean bold, Short color) {
        Font font = workbook.createFont();
        Optional.ofNullable(fontName).ifPresent(fontNameIn -> font.setFontName(fontNameIn));//设置字体名
        Optional.ofNullable(fontSize).ifPresent(fontSizeIn -> font.setFontHeightInPoints(fontSizeIn));//设置字体大小
        Optional.ofNullable(italic).ifPresent(italicIn -> font.setItalic(italicIn));//设置是否斜体
        Optional.ofNullable(bold).ifPresent(boldIn -> font.setBold(boldIn));// 设置是否粗体
        Optional.ofNullable(color).ifPresent(colorIn -> font.setColor(colorIn));// 设置字体颜色
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 将数据填入Excel
     * @param dataCellList 数据
     * @param appendRow 绝对开始行（每个单元格的相对行加上该参数，得到该单元格的实际行）
     * @param workbook Excel工作簿
     * @param sheet Excel的sheet
     */
    public static void addDataToExcel(List<ExcelCellDTO> dataCellList, int appendRow, SXSSFWorkbook workbook, SXSSFSheet sheet) {
        Map<Integer, List<ExcelCellDTO>> rowDataMap = dataCellList.stream()
                .reduce(new HashMap<>(), (map, cellDTO) -> {
                    List<ExcelCellDTO> rowCellList = map.get(cellDTO.getRelativeRow());
                    if (rowCellList == null) {
                        rowCellList = new ArrayList<>();
                        map.put(appendRow + cellDTO.getRelativeRow(), rowCellList);
                    }
                    rowCellList.add(cellDTO);
                    return map;
                }, (map1, map2) -> map2);
        for (Map.Entry<Integer, List<ExcelCellDTO>> entry : rowDataMap.entrySet()) {
            // 创建行
            int row = entry.getKey();
            SXSSFRow dataRow = sheet.createRow(row);
            // 填充内容
            List<ExcelCellDTO> rowDataList = entry.getValue();
            for (ExcelCellDTO excelCellDTO : rowDataList) {
                // 单元格格式设定（背景色、位置、边框、字体）
                CellStyle dataStyle = workbook.createCellStyle();
                setAlignment(dataStyle, excelCellDTO.getHorizontal(), excelCellDTO.getVertical());
                setBackgroundColor(dataStyle, excelCellDTO.getBackgroundColor());
                setBorder(dataStyle, excelCellDTO.getBorderTop(), excelCellDTO.getBorderBottom(),
                        excelCellDTO.getBorderLeft(), excelCellDTO.getBorderRight());
                setFont(dataStyle, workbook, excelCellDTO.getFontName(), excelCellDTO.getFontSize(),
                        excelCellDTO.getFontItalic(), excelCellDTO.getFontBold(), excelCellDTO.getFontColor());
                // 创建单元格，填入数据，设置格式
                SXSSFCell cell = dataRow.createCell(excelCellDTO.getColumn());
                cell.setCellValue(excelCellDTO.getValue());
                cell.setCellStyle(dataStyle);
            }
        }
    }

}
