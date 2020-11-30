package ${packageName}.util;

import ${packageName}.annotation.ExcelProperty;
import ${packageName}.config.Constants;
import ${packageName}.dto.help.ExcelCellBuilder;
import ${packageName}.dto.help.ExcelCellDTO;
import ${packageName}.dto.help.ExcelCellRangeDTO;
import ${packageName}.dto.help.ExcelRowCellsDTO;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel工具类
 * @author Bob
 */
public class ExcelUtil {

    /**
     * 获取Excel的XSSFCell的值
     * @param cell 传入的cell
     * @param dateFormat 指定的日期格式
     * @return cell的值
     */
    public static String getCellValueOfExcel(Cell cell, String dateFormat) {
        if (cell == null) {
            return "";
        }
        String cellValue = "";
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                if (dateFormat == null) {
                    // 默认的日期格式
                    dateFormat = "yyyy-MM-dd";
                }
                cellValue = DateFormatUtils.format(cell.getDateCellValue(), dateFormat);
            } else {
                NumberFormat nf = NumberFormat.getInstance();
                cellValue = String.valueOf(nf.format(cell.getNumericCellValue())).replace(",", "");
            }
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            cellValue = String.valueOf(cell.getStringCellValue());
        } else if(cell.getCellTypeEnum() == CellType.BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.ERROR) {
            cellValue = null;
        } else {
            cellValue = "";
        }
        return cellValue;
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
                SXSSFRow currentRow = null;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    // BUG修复：可能会导致java.lang.IllegalArgumentException:
                    // Attempting to write a row[0] in the range [0,3850] that is already written to disk.
                    // 原因：SXSSFWorkbook这个类是专门用来往Excel中写入大量数据的，它会偷偷摸摸的把数据写会磁盘，显得比较快。
//                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow != null && currentRow.getCell(columnNum) != null) {
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
     * 设置合并单元格的边框
     * @param sheet Excel工作表
     * @param top 上边框样式
     * @param bottom 下边框样式
     * @param left 左边框样式
     * @param right 右边框样式
     * @return 返回设置好的样式
     */
    public static void setRegionBorder(SXSSFSheet sheet, CellRangeAddress cellRangeAddress,
                                       BorderStyle top, BorderStyle bottom, BorderStyle left, BorderStyle right) {
        Optional.ofNullable(top).ifPresent(topIn -> RegionUtil.setBorderTop(topIn, cellRangeAddress, sheet));//上边框
        Optional.ofNullable(bottom).ifPresent(bottomIn -> RegionUtil.setBorderBottom(bottomIn, cellRangeAddress, sheet));//下边框
        Optional.ofNullable(left).ifPresent(leftIn -> RegionUtil.setBorderLeft(leftIn, cellRangeAddress, sheet));//左边框
        Optional.ofNullable(right).ifPresent(rightIn -> RegionUtil.setBorderRight(rightIn, cellRangeAddress, sheet));//右边框
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
     * 设置换行
     * @param cellStyle 单元格样式
     * @param wrapText 是否换行（默认将\n转换为换行）
     * @return 返回设置好的样式
     */
    public static CellStyle setWrapText(CellStyle cellStyle, Boolean wrapText) {
        Optional.ofNullable(wrapText).ifPresent(wrap -> cellStyle.setWrapText(wrap));
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
     * @param maxWidthMap 存储的最大列宽（Key：列序号  Value：列宽）
     * @param lastRowToMergeMap 要合并的单元格（Key：要合并的单元格的最后一行  Value：最后一行是该数值的所有合并单元格）
     * @param cellRangeList 要合并的单元格
     * @param wrapSpecial 自动换行标识符
     * @param workbook Excel工作簿
     * @param sheet Excel的sheet
     */
    public static void addDataToExcel(List<ExcelCellDTO> dataCellList, int appendRow, Map<Integer, Integer> maxWidthMap,
                                      Map<Integer, List<ExcelCellRangeDTO>> lastRowToMergeMap,
                                      List<ExcelCellRangeDTO> cellRangeList, String wrapSpecial,
                                      SXSSFWorkbook workbook, SXSSFSheet sheet) {
        List<ExcelRowCellsDTO> rowDataCellsList = new ArrayList<>(dataCellList.stream()
                // 按所在列分组
                .reduce(new HashMap<Integer, List<ExcelCellDTO>>(), (map, cellDTO) -> {
                    List<ExcelCellDTO> rowCellList = map.get(appendRow + cellDTO.getRelativeRow());
                    if (rowCellList == null) {
                        rowCellList = new ArrayList<>();
                        map.put(appendRow + cellDTO.getRelativeRow(), rowCellList);
                    }
                    rowCellList.add(cellDTO);
                    return map;
                }, (map1, map2) -> map2).entrySet()).stream()
                // 类型转换
                .map(rowDataEntry -> new ExcelRowCellsDTO(rowDataEntry.getKey(), rowDataEntry.getValue()))
                // 按列排序（避免顺序错乱导致Attempting to write ... that is already written to disk）
                .sorted(Comparator.comparing(ExcelRowCellsDTO::getRow))
                // 返回
                .collect(Collectors.toList());
        for (ExcelRowCellsDTO rowDataCell : rowDataCellsList) {
            // 创建行
            int nowRow = rowDataCell.getRow();
            SXSSFRow dataRow = sheet.createRow(nowRow);
            // 填充内容
            List<ExcelCellDTO> rowCellList = rowDataCell.getCellList();
            // 每列的单元格Map（Key：列号  Value：单元格）
            Map<Integer, SXSSFCell> cellMapOfRow = new HashMap<>();
            for (ExcelCellDTO excelCellDTO : rowCellList) {
                // 单元格格式设定（背景色、位置、边框、字体、换行）
                CellStyle dataStyle = setCellStyle(excelCellDTO, workbook);
                // 创建单元格，填入数据，设置格式
                SXSSFCell cell = dataRow.createCell(excelCellDTO.getColumn());
                cellMapOfRow.put(excelCellDTO.getColumn(), cell);
                // 设置单元格内容（替换自动换行标识符）
                String cellValue = excelCellDTO.getValue() == null ? "" : excelCellDTO.getValue();
                if (wrapSpecial != null) {
                    cell.setCellValue(cellValue.replace(wrapSpecial, "\n"));
                }
                cell.setCellStyle(dataStyle);
                // 每个单元格都要计算每列的最大宽度
                ExcelUtil.computeMaxColumnWith(maxWidthMap, cell, nowRow, excelCellDTO.getColumn(), null, cellRangeList);
            }
            // 每处理一行都要判断是否有合并单元格，有的话就合并（此时所在行为nowRow）
            List<ExcelCellRangeDTO> dataRowMergeList = lastRowToMergeMap.get(nowRow);
            if (dataRowMergeList != null && dataRowMergeList.size() > 0) {
                for (ExcelCellRangeDTO cellRangeDTO : dataRowMergeList) {
                    // 合并单元格
                    ExcelUtil.doMergeCell(sheet, cellRangeDTO);
                }
            }
        }
    }

    /**
     * 设置单元格样式
     * @param excelCellDTO 单元格及样式要求
     * @param workbook Excel工作簿
     */
    public static CellStyle setCellStyle(ExcelCellDTO excelCellDTO, SXSSFWorkbook workbook) {
        // 单元格格式设定（背景色、位置、边框、字体、换行）
        CellStyle dataStyle = workbook.createCellStyle();
        setAlignment(dataStyle, excelCellDTO.getHorizontal(), excelCellDTO.getVertical());
        setBackgroundColor(dataStyle, excelCellDTO.getBackgroundColor());
        setBorder(dataStyle, excelCellDTO.getBorderTop(), excelCellDTO.getBorderBottom(),
                excelCellDTO.getBorderLeft(), excelCellDTO.getBorderRight());
        setFont(dataStyle, workbook, excelCellDTO.getFontName(), excelCellDTO.getFontSize(),
                excelCellDTO.getFontItalic(), excelCellDTO.getFontBold(), excelCellDTO.getFontColor());
        setWrapText(dataStyle, excelCellDTO.getWrapText());
        return dataStyle;
    }

    /**
     * 计算非跨列的合并单元格的最大列宽
     * @param maxWidthMap 存储的最大列宽（Key：列序号  Value：列宽）
     * @param cell 单元格
     * @param row 当前行序号
     * @param column 当前列序号
     * @param columnLengthLimit 限制的最大列宽
     * @param cellRangeList 合并单元格列表
     */
    public static void computeMaxColumnWith(Map<Integer, Integer> maxWidthMap, SXSSFCell cell, int row,
                                            int column, Integer columnLengthLimit, List<ExcelCellRangeDTO> cellRangeList) {
        if (cell == null) {
            return;
        }
        // 是否在跨列合并单元格范围内
        if (cellRangeList != null && cellRangeList.size() > 0) {
            for (ExcelCellRangeDTO cellRangeDTO : cellRangeList) {
                if (cellRangeDTO.getFromColumn() != cellRangeDTO.getToColumn()
                        && row >= cellRangeDTO.getFromRow() && row <= cellRangeDTO.getToRow()
                        && column >= cellRangeDTO.getFromColumn() && column <= cellRangeDTO.getToColumn()) {
                    // 在跨列的合并单元格范围内不计算最大列宽
                    return;
                }
            }
        }
        // 剔除合并单元格
        if (columnLengthLimit == null) {
            // 这里把宽度最大限制到15000
            columnLengthLimit = 15000;
        }
        int length = cell.getStringCellValue().getBytes().length  * 256 + 200;
        // 这里把宽度最大限制到 columnLengthLimit
        if (length > columnLengthLimit) {
            length = columnLengthLimit;
        }
        Integer currentMaxLength = maxWidthMap.get(column);
        if (currentMaxLength == null) {
            maxWidthMap.put(column, length);
        } else {
            maxWidthMap.put(column, Math.max(length, maxWidthMap.get(column)));
        }
    }

    /**
     * 预处理合并单元格（以防出现Attempting to write ... that is already written to disk.）
     * @param cellRangeList
     * @return Map（Key：要合并的单元格的最后一行  Value：最后一行是该数值的所有合并单元格）
     */
    public static Map<Integer, List<ExcelCellRangeDTO>> mergeCellGroup(List<ExcelCellRangeDTO> cellRangeList) {
        Map<Integer, List<ExcelCellRangeDTO>> lastRowToMergeMap = new HashMap<>();
        if (cellRangeList != null) {
            for (ExcelCellRangeDTO cellRangeDTO : cellRangeList) {
                int toRow = cellRangeDTO.getToRow();
                List<ExcelCellRangeDTO> lastRowCellRangeList = lastRowToMergeMap.get(toRow);
                if (lastRowCellRangeList == null) {
                    lastRowCellRangeList = new ArrayList<>();
                    lastRowToMergeMap.put(toRow, lastRowCellRangeList);
                }
                lastRowCellRangeList.add(cellRangeDTO);
            }
        }
        return lastRowToMergeMap;
    }

    /**
     * 合并单元格
     * @param sheet Excel工作表
     * @param cellRangeDTO Excel单元格范围DTO（要合并的单元格范围）
     */
    public static void doMergeCell(SXSSFSheet sheet, ExcelCellRangeDTO cellRangeDTO) {
        // 合并单元格参数：起始行、终止行、起始列、终止列
        CellRangeAddress totalTitleRegion = new CellRangeAddress(cellRangeDTO.getFromRow(),
                cellRangeDTO.getToRow(), cellRangeDTO.getFromColumn(), cellRangeDTO.getToColumn());
        sheet.addMergedRegion(totalTitleRegion);
        // 合并单元格加边框
        ExcelUtil.setRegionBorder(sheet, totalTitleRegion, cellRangeDTO.getBorderTop(),
                cellRangeDTO.getBorderBottom(), cellRangeDTO.getBorderLeft(), cellRangeDTO.getBorderRight());
    }

    /**
     * 修改指定字段的ExcelProperty注解的optionList值
     * @param objClass 指定类
     * @param fieldName 指定字段名
     * @param optionList 可选范围列表
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void changeOptionListOfExcelProperty(Class<?> objClass, String fieldName,
                                                       List<String> optionList) throws NoSuchFieldException, IllegalAccessException {
        String[] optionArray = new String[optionList.size()];
        optionList.toArray(optionArray);
        MyBeanUtil.changeStaticAnnotationOfField(objClass, fieldName, ExcelProperty.class, "optionList", optionArray);
    }

    /**
     * 生成Excel单元格对象
     * @param excelCellBuilder 单元格构造器
     * @return 生成的单元格
     */
    public static ExcelCellDTO buildCell(ExcelCellBuilder excelCellBuilder) {
        return excelCellBuilder.buildCell();
    }

    // =============== Excel列号转字母 start ==============================
    /**
     * 列号数据源
     */
    private static String[] columnNameSources = new String[] { "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * (256 for *.xls, 16384 for *.xlsx)
     *
     * @param columnNum
     *            列的个数，从1开始
     * @throws IllegalArgumentException
     *             如果 columnNum 超出该范围 [1,16384]
     * @return 返回[1,columnNum]共columnNum个对应xls列字母的数组
     */
    public static String[] getColumnLabels(int columnNum) {
        if (columnNum < 1 || columnNum > 16384) {
            throw new IllegalArgumentException();
        }
        String[] columns = new String[columnNum];
        if (columnNum < 27) {
            System.arraycopy(columnNameSources, 0, columns, 0, columnNum);
            return columns;
        }
        int multiple = -1;
        int remainder;
        System.arraycopy(columnNameSources, 0, columns, 0, 26);
        int currentLoopIdx = 0;
        if (columnNum < 703) {
            for (int i = 26; i < columnNum; i++) {
                remainder = currentLoopIdx % 26;
                if (remainder == 0) {
                    multiple++;
                }
                columns[i] = columnNameSources[multiple] + columns[remainder];
                currentLoopIdx++;
            }
        } else {
            int currentLen = 26;
            int totalLen = 26;
            int lastLen = 0;
            for (int i = 26; i < columnNum; i++) {
                remainder = currentLoopIdx % currentLen;
                if (remainder == 0) {
                    multiple++;
                    int j = multiple % 26;
                    if (j == 0 && multiple != 0) {
                        lastLen = totalLen;
                        currentLen = 26 * currentLen;
                        totalLen = currentLen + lastLen;
                        currentLoopIdx = 0;
                    }
                }
                columns[i] = columnNameSources[multiple % 26]
                        + columns[remainder + lastLen];
                currentLoopIdx++;
            }
        }

        return columns;
    }

    /**
     * 返回该列号对应的字母
     *
     * @param columnNo
     *            (xls的)第几列（从1开始）
     */
    public static String getColumnCorrespondingLabel(int columnNo) {
        if (columnNo < 1/** ||columnNo>16384 **/) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(5);
        int remainder = columnNo % 26;
        if (remainder == 0) {
            sb.append("Z");
            remainder = 26;
        } else {
            sb.append(columnNameSources[remainder - 1]);
        }

        while ((columnNo = (columnNo - remainder) / 26 - 1) > -1) {
            remainder = columnNo % 26;
            sb.append(columnNameSources[remainder]);
        }

        return sb.reverse().toString();
    }

    /**
     * 列号转字母
     *
     * @param columnIndex
     *            poi里xls的列号（从0开始）
     * @throws IllegalArgumentException
     *             if columnIndex less than 0
     * @return 该列对应的字母
     */
    public static String getColumnIndexLabel(int columnIndex) {
        return getColumnCorrespondingLabel(columnIndex + 1);
    }
    // =============== Excel列号转字母 end ==============================

}
