package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.*;
import ${packageName}.service.CommonService;
import ${packageName}.util.ExcelUtil;
import ${packageName}.util.FileUtil;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonServiceImpl implements CommonService {

    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private YmlConfig ymlConfig;

    /**
     * 上传文件到服务器
     * @param file 待上传的文件
     * @return 上传结果（路径、上传时间）
     */
    public ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file) {
        log.debug("上传文件 : {}", file.getOriginalFilename());
        Date nowDate = new Date();
        // 获取上传文件名
        String fileName = file.getOriginalFilename();
        // 获取扩展名
        String extension = fileName.substring(fileName.lastIndexOf("."));
        // 相对路径
        String relativePath = new SimpleDateFormat("yyyyMMdd").format(nowDate);
        int fileRandomInt = new Random().nextInt(1000);
        // 新文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + extension;
        // 压缩文件名
        String compressFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + "_small" + extension;
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + "/" + relativePath;

        File targetPath = new File(localPath);
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
        String localFileName = localPath + "/" + newFileName;
        String localCompressFileName = localPath + "/" + compressFileName;
        File targetFile = new File(localFileName);

        // 文件保存到服务器
        try {
            file.transferTo(targetFile);
            ReturnFileUploadDTO fileUploadDTO = new ReturnFileUploadDTO();
            if ("open".equals(ymlConfig.getPicCompressSwitch())) {
                // 启动压缩
                int compressCount = FileUtil.compressPic(ymlConfig, localFileName, localCompressFileName);
                fileUploadDTO.setCompressedRelativePath(relativePath + "/" + localCompressFileName);
            }
            fileUploadDTO.setRelativePath(relativePath + "/" + newFileName);
            fileUploadDTO.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
            return new ReturnUploadCommonDTO<>(fileUploadDTO);
        } catch (Exception e) {
            log.error("文件上传失败：" + fileName, e);
            throw new CommonException("文件上传失败：" + fileName + " -> " + e.getMessage());
        }
    }

    /**
     * 从服务器端下载文件
     * @param response
     * @param fullFileName 包含全路径的文件名
     * @param changeFileName 修改后的文件名（不包含路径）
     * @return
     * @throws UnsupportedEncodingException
     */
    public ReturnCommonDTO downloadFile(HttpServletResponse response, String fullFileName, String changeFileName) {
        File file = new File(fullFileName);
        return downloadFile(response, file, changeFileName);
    }

    /**
     * 从服务器端下载文件
     * @param response
     * @param file 待下载的文件
     * @param changeFileName 修改后的文件名（不包含路径）
     * @return
     * @throws UnsupportedEncodingException
     */
    public ReturnCommonDTO downloadFile(HttpServletResponse response, File file, String changeFileName) {
        log.debug("下载文件 : {}", file.getAbsolutePath());
        String fullFileName = file.getAbsolutePath();
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件不存在");
            }
            fis = new FileInputStream(file);
            return downloadFileBase(response, fis, fullFileName, changeFileName);
        } catch (Exception e) {
            log.error("文件下载失败：" + fullFileName, e);
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件下载失败");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("文件下载失败：" + fullFileName, e);
                }
            }
        }
    }

    /**
     * 从服务器端下载文件
     * @param response
     * @param inputStream 输入流
     * @param changeFileName 修改后的文件名（不包含路径）
     * @return
     * @throws UnsupportedEncodingException
     */
    public ReturnCommonDTO downloadFile(HttpServletResponse response, InputStream inputStream, String changeFileName) {
        return downloadFileBase(response, inputStream, null, changeFileName);
    }

    /**
     * 从服务器端下载文件
     * @param response
     * @param inputStream 输入流
     * @param oriFileName 原始文件名
     * @param changeFileName 修改后的文件名（不包含路径）
     * @return
     * @throws UnsupportedEncodingException
     */
    public ReturnCommonDTO downloadFileBase(HttpServletResponse response, InputStream inputStream, String oriFileName,
                                            String changeFileName) {
        try {
            // 配置文件下载
            response.setHeader("content-type", "application/json");
            response.setContentType("application/json");
            if (changeFileName == null || "".equals(changeFileName.trim())) {
                changeFileName = oriFileName.substring(oriFileName.lastIndexOf(File.separator) + 1);
            }
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(changeFileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(inputStream);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                log.error("文件下载失败：" + (oriFileName == null ? "" : oriFileName), e);
                return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件下载失败");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("文件关闭失败：" + (oriFileName == null ? "" : oriFileName), e);
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        log.error("文件关闭失败：" + (oriFileName == null ? "" : oriFileName), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件下载失败：" + (oriFileName == null ? "" : oriFileName), e);
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件下载失败");
        }
        return new ReturnCommonDTO();
    }

    /**
     * 导出Excel
     * @param response HTTP Response
     * @param fileName Excel文件名（不包含后缀）
     * @param sheetName sheet名
     * @param maxColumn 最大列数（用于自适应列宽）
     * @param tableStartRow 实际表格（包括标题行）的开始行
     * @param beforeDataCellList 在实际表格前面部分的单元格
     * @param afterDataCellList 在实际表格后面部分的单元格
     * @param titleList 标题行
     * @param dataList 数据
     * @param cellRangeList 要合并的单元格
     * @return 导出结果
     * @throws Exception
     */
    public ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName,
                                       int maxColumn, int tableStartRow, List<ExcelCellDTO> beforeDataCellList,
                                       List<ExcelCellDTO> afterDataCellList, List<ExcelTitleDTO> titleList,
                                       List<?> dataList, List<ExcelCellRangeDTO> cellRangeList) {
        try {
            // 创建Excel工作簿
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // 设置头信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            // 一定要设置成xlsx格式
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'"
                    + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            // 输出流
            ServletOutputStream outputStream = null;
            workbook.createCellStyle();
            try {
                // 创建一个输出流
                outputStream = response.getOutputStream();
                // 创建sheet页
                SXSSFSheet sheet = workbook.createSheet(sheetName);
                // 存储最大列宽，用于处理中文不能自动调整列宽的问题
                Map<Integer, Integer> maxWidthMap = new HashMap<>();
                // 在实际表格上方的单元格
                if (beforeDataCellList != null) {
                    // key：relativeRow相对行数，value：该行的单元格数据
                    ExcelUtil.addDataToExcel(beforeDataCellList, 0, maxWidthMap, workbook, sheet);
                }
                // 创建表头
                SXSSFRow headRow = sheet.createRow(tableStartRow);
                // 表头统一居中，背景为灰色，加边框
                CellStyle titleNameStyle = workbook.createCellStyle();
                ExcelUtil.setAlignment(titleNameStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                ExcelUtil.setBackgroundColor(titleNameStyle, IndexedColors.GREY_25_PERCENT.getIndex());
                ExcelUtil.setBorder(titleNameStyle, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                // 设置表头信息
                Map<String, Integer> titleNameMap = new HashMap<>();
                for (int column = 0; column < titleList.size(); column++) {
                    ExcelTitleDTO titleDTO = titleList.get(column);
                    titleNameMap.put(titleDTO.getTitleName(), column);
                    SXSSFCell cell = headRow.createCell(column);
                    cell.setCellValue(titleDTO.getTitleContent());
                    cell.setCellStyle(titleNameStyle);
                }
                // 表数据统一居中，加边框
                CellStyle dataStyle = workbook.createCellStyle();
                ExcelUtil.setAlignment(dataStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                ExcelUtil.setBorder(dataStyle, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                // 填入表数据
                for (int dataRowCount = 0; dataRowCount < dataList.size(); dataRowCount++) {
                    SXSSFRow dataRow = sheet.createRow(tableStartRow + dataRowCount + 1);
                    Object dataObject = dataList.get(dataRowCount);
                    if (dataObject instanceof Map) {
                        // Map获取数据
                        for (int column = 0; column < titleList.size(); column++) {
                            String titleName = titleList.get(column).getTitleName();
                            Object data = ((Map) dataObject).get(titleName);
                            SXSSFCell cell = dataRow.createCell(column);
                            cell.setCellValue(data == null ? "" : data.toString());
                            cell.setCellStyle(dataStyle);
                            // 设置该列的最大宽度
                            ExcelUtil.computeMaxColumnWith(maxWidthMap, cell, column, null);
                        }
                    } else {
                        // 反射获取对象属性
                        for (int column = 0; column < titleList.size(); column++) {
                            String titleName = titleList.get(column).getTitleName();
                            // 获取属性（使用apache的包可以获取包括父类的属性）
                            Field field = FieldUtils.getField(dataObject.getClass(), titleName, true);
                            // 设置对象的访问权限，保证对private的属性的访问
                            if (field != null) {
                                field.setAccessible(true);
                                Object data = field.get(dataObject);
                                SXSSFCell cell = dataRow.createCell(column);
                                cell.setCellValue(data == null ? "" : data.toString());
                                cell.setCellStyle(dataStyle);
                                // 设置该列的最大宽度
                                ExcelUtil.computeMaxColumnWith(maxWidthMap, cell, column, null);
                            }
                        }
                    }
                }
                // 在实际表格下方的单元格
                if (afterDataCellList != null) {
                    // key：relativeRow相对行数，value：该行的单元格数据
                    ExcelUtil.addDataToExcel(afterDataCellList, tableStartRow + dataList.size() + 1, maxWidthMap, workbook, sheet);
                }
                // 合并单元格
                if (cellRangeList != null) {
                    for (ExcelCellRangeDTO cellRangeDTO : cellRangeList) {
                        // 合并单元格参数：起始行、终止行、起始列、终止列
                        CellRangeAddress totalTitleRegion = new CellRangeAddress(cellRangeDTO.getFromRow(),
                                cellRangeDTO.getToRow(), cellRangeDTO.getFromColumn(), cellRangeDTO.getToColumn());
                        sheet.addMergedRegion(totalTitleRegion);
                    }
                }
                // 设置为根据内容自动调整列宽，必须在单元格设值以后进行
                sheet.trackAllColumnsForAutoSizing();
                for (int column = 0; column < maxColumn; column++) {
                    sheet.autoSizeColumn(column);
                    // 处理中文不能自动调整列宽的问题
                    if (maxWidthMap.get(column) != null) {
                        sheet.setColumnWidth(column, maxWidthMap.get(column));
                    }
                }
                // 写入数据
                workbook.write(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭
                if (outputStream != null) {
                    outputStream.close();
                }
                workbook.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("导出失败");
        }
        return new ReturnCommonDTO();
    }

    /**
     * 通用解析Excel文件
     * @param fullFileName 本地全路径的文件名
     * @param columnCount 指定的列数
     * @param columnNameList Excel列名列表
     * @param columnKeyList Excel列的Key列表（与返回的数据中Map的key一致）
     * @param regexList 数据的正则验证
     * @param allowNullList 每一列是否允许为空
     * @return 解析后的数据列表
     */
    public ReturnCommonDTO<List<Map<String, String>>> importParseExcel(String fullFileName, int columnCount,
                List<String> columnNameList, List<String> columnKeyList, List<String> regexList, List<Boolean> allowNullList) {
        if (fullFileName == null || "".equals(fullFileName)) {
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "导入文件名为空");
        }
        if (!fullFileName.endsWith(".xlsx")) {
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "导入文件名后缀不正确，必须为.xlsx");
        }
        try {
            List<Map<String, String>> dataList = new ArrayList<>();
            InputStream is = new FileInputStream(fullFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 第一行（列名）
            XSSFRow firstRow = sheet.getRow(0);
            int firstMinCell = firstRow.getFirstCellNum();
            int firstMaxCell = firstRow.getLastCellNum();
            if (firstMaxCell != columnCount) {
                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第1行列数不是" + columnCount);
            }
            // 验证第一行是否正确
            for (int j = 0; j < firstMaxCell; j++) {
                String cellValue = ExcelUtil.getCellValueOfExcel(firstRow.getCell(j));
                if (cellValue == null || !cellValue.trim().equals(columnNameList.get(j))) {
                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第1行第" + (j + 1)
                        + "列的列名不正确，请参照模板修改（注意列顺序不能错乱）");
                }
            }
            // 数据验证的正则初始化
            List<Pattern> patternList = new ArrayList<>();
            for (int j = 0; j < firstMaxCell; j++) {
                Pattern pattern = Pattern.compile(regexList.get(j));
                patternList.add(pattern);
            }
            // 读取数据行的每一行内容进行解析
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // 初始化该行的返回数据
                Map<String, String> data = new HashMap<>();
                // 获取这一行
                XSSFRow dataRow = sheet.getRow(i);
                int dataMinCell = dataRow.getFirstCellNum();
                int dataMaxCell = dataRow.getLastCellNum();
                if (dataMaxCell != columnCount) {
                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第" + (i + 1) +
                            "行列数不是" + columnCount);
                }
                // 遍历这一行的每一列
                for (int j = 0; j < dataMaxCell; j++) {
                    String cellValue = ExcelUtil.getCellValueOfExcel(dataRow.getCell(j));
                    if (cellValue == null || "".equals(cellValue.trim())) {
                        if (!allowNullList.get(j)) {
                            return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第" + (i + 1) +
                                    "行第" + (j + 1) + "列数据为空");
                        } else {
                            // 空数据直接设置空字符串
                            data.put(columnKeyList.get(j), "");
                        }
                    } else {
                        if (regexList.get(j) != null) {
                            // 有校验的列
                            Matcher matcher = patternList.get(j).matcher(cellValue.trim());
                            if (!matcher.matches()) {
                                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第" + (i + 1) +
                                        "行第" + (j + 1) + "列数据格式不正确");
                            }
                            data.put(columnKeyList.get(j), cellValue.trim());
                        } else {
                            // 没有校验的列
                            data.put(columnKeyList.get(j), cellValue.trim());
                        }
                    }
                }
                // 将解析出的这一行的数据添加到列表中
                dataList.add(data);
            }
            // 返回解析后的全部数据
            return new ReturnUploadCommonDTO(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "导入文件解析失败");
        }
    }

}
