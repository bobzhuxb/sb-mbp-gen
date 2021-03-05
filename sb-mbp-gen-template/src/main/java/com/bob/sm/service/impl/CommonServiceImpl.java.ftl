package ${packageName}.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.annotation.ExcelProperty;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.*;
import ${packageName}.service.CommonService;
import ${packageName}.util.ExcelUtil;
import ${packageName}.util.FileUtil;
import ${packageName}.util.LocalCache;
import ${packageName}.util.LocalCacheEntity;
import ${packageName}.web.rest.errors.CommonAlertException;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 共通方法
 * @author Bob
 */
@Service
public class CommonServiceImpl implements CommonService {

    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 上传文件到服务器
     * @param file 待上传的文件
     * @return 上传结果（路径、上传时间）
     */
    @Override
    public ReturnCommonDTO<ReturnFileUploadDTO> uploadFile(MultipartFile file) {
        Date nowDate = new Date();
        // 获取上传文件名
        String fileName = file.getOriginalFilename();
        // 获取扩展名（注意扩展名可能不存在的情况）
        int lastPointPosition = fileName.lastIndexOf(".");
        String extension = lastPointPosition < 0 ? "" : fileName.substring(lastPointPosition);
        // 相对路径
        String relativePath = Constants.FILE_UPLOAD_RELATIVE_PATH + File.separator
                + new SimpleDateFormat("yyyyMMdd").format(nowDate);
        int fileRandomInt = new Random().nextInt(1000);
        // 新文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + extension;
        // 压缩文件名
        String compressFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + "_small" + extension;
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + File.separator + relativePath;

        File targetPath = new File(localPath);
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
        String localFileName = localPath + File.separator + newFileName;
        String localCompressFileName = localPath + File.separator + compressFileName;
        File targetFile = new File(localFileName);

        // 文件保存到服务器
        try {
            file.transferTo(targetFile);
            ReturnFileUploadDTO fileUploadDTO = new ReturnFileUploadDTO();
            fileUploadDTO.setOriginalFileName(fileName);
            if ("open".equals(ymlConfig.getPicCompressSwitch())) {
                // 启动压缩
                int compressCount = FileUtil.compressPic(ymlConfig, localFileName, localCompressFileName);
                fileUploadDTO.setCompressedRelativePath(relativePath + File.separator + localCompressFileName);
            }
            fileUploadDTO.setRelativePath(relativePath + File.separator + newFileName);
            fileUploadDTO.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
            return new ReturnUploadCommonDTO<>(fileUploadDTO);
        } catch (Exception e) {
            throw CommonException.errWithDetail("文件上传失败：" + fileName, e.getMessage());
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
    @Override
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
    @Override
    public ReturnCommonDTO downloadFile(HttpServletResponse response, File file, String changeFileName) {
        String fullFileName = file.getAbsolutePath();
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件不存在");
            }
            fis = new FileInputStream(file);
            return downloadFileBase(response, fis, fullFileName, changeFileName);
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            throw CommonException.errWithDetail("文件下载失败：" + fullFileName, e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.warn("文件下载，流关闭失败：" + fullFileName, e.getMessage());
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
    @Override
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
    @Override
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
                throw CommonException.errWithDetail("文件下载失败：" + (oriFileName == null ? "" : oriFileName),
                        e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.warn("文件下载，流关闭失败：" + (oriFileName == null ? "" : oriFileName), e.getMessage());
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        log.warn("文件下载，流关闭失败：" + (oriFileName == null ? "" : oriFileName), e.getMessage());
                    }
                }
            }
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            throw CommonException.errWithDetail("文件下载失败：" + (oriFileName == null ? "" : oriFileName),
                    e.getMessage());
        }
        return new ReturnCommonDTO();
    }

    /**
     * 导出单Sheet的Excel
     * 注意：1、Spring框架中，不要对HttpServletResponse对象的ServletOutputStream做流关闭处理
     * 2、对于下载文件的方法请不要有任何返回值，因为我们写完业务逻辑后Spring框架层还会做一些额外的工作（可能会用到ServletOutputStream对象）
     * @param response HTTP Response
     * @param excelExportDTO 导出的Excel相关数据
     * @return 导出结果
     * @throws Exception
     */
    @Override
    public void exportExcel(HttpServletResponse response, ExcelExportDTO excelExportDTO) {
        SXSSFWorkbook workbook = null;
        OutputStream outputStream = null;
        try {
            // 生成Excel信息
            ExcelExportBaseInfoDTO excelExportBaseInfoDTO = formExcelForExport(response, excelExportDTO);
            // 获取解析参数
            workbook = excelExportBaseInfoDTO.getWorkbook();
            outputStream = excelExportBaseInfoDTO.getOutputStream();
            String fileName = excelExportBaseInfoDTO.getFileName();
            // 生成Sheet信息
            // sheet名
            String sheetName = excelExportDTO.getSheetName();
            if (sheetName == null || "".equals(sheetName)) {
                sheetName = fileName;
            }
            formExcelSheetForExport(excelExportDTO, sheetName, workbook);
            // 写入数据
            workbook = doWriteExcelStream(workbook, outputStream, excelExportBaseInfoDTO.getSuffix(),
                    excelExportDTO.getOpenPassword());
        } catch (Exception e) {
            throw CommonException.errWithDetail("导出失败", e.getMessage());
        } finally {
            // 关闭
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (response == null && outputStream != null) {
                    // 纯写文件需要关闭流，导出时不需要
                    outputStream.close();
                }
            } catch (Exception e) {
                log.warn("Excel导出，流关闭失败", e.getMessage());
            }
        }
    }

    /**
     * 导出多Sheet的Excel
     * 注意：1、Spring框架中，不要对HttpServletResponse对象的ServletOutputStream做流关闭处理
     * 2、对于下载文件的方法请不要有任何返回值，因为我们写完业务逻辑后Spring框架层还会做一些额外的工作（可能会用到ServletOutputStream对象）
     * @param response HTTP Response
     * @param excelExportDTOList 导出的Excel相关数据（每个元素表示一个Sheet的描述）
     * @return 导出结果
     * @throws Exception
     */
    @Override
    public void exportExcel(HttpServletResponse response, List<ExcelExportDTO> excelExportDTOList) {
        // 验证参数
        if (excelExportDTOList == null && excelExportDTOList.size() == 0) {
            throw new CommonAlertException("Excel多sheet导出参数异常");
        }
        if (excelExportDTOList.size() == 1) {
            // 单sheet的Excel转入对应的方法进行处理
            exportExcel(response, excelExportDTOList.get(0));
            return;
        }
        SXSSFWorkbook workbook = null;
        OutputStream outputStream = null;
        try {
            // 与Excel文件本身相关的属性（非sheet的属性）写在第一个元素中
            ExcelExportBaseInfoDTO excelExportBaseInfoDTO = formExcelForExport(response, excelExportDTOList.get(0));
            // 获取解析参数
            workbook = excelExportBaseInfoDTO.getWorkbook();
            outputStream = excelExportBaseInfoDTO.getOutputStream();
            // 生成每一个Sheet信息
            for (ExcelExportDTO excelExportDTO : excelExportDTOList) {
                // sheet名
                String sheetName = excelExportDTO.getSheetName();
                if (sheetName == null || "".equals(sheetName)) {
                    throw new CommonAlertException("参数错误，部分sheet名未配置");
                }
                formExcelSheetForExport(excelExportDTO, sheetName, workbook);
            }
            // 写入数据
            workbook = doWriteExcelStream(workbook, outputStream, excelExportBaseInfoDTO.getSuffix(),
                    excelExportDTOList.get(0).getOpenPassword());
        } catch (CommonAlertException e) {
            throw e;
        } catch (Exception e) {
            throw CommonException.errWithDetail("导出失败", e.getMessage());
        } finally {
            // 关闭
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (response == null && outputStream != null) {
                    // 纯写文件需要关闭流，导出时不需要
                    outputStream.close();
                }
            } catch (Exception e) {
                log.warn("Excel导出，流关闭失败", e.getMessage());
            }
        }
    }

    /**
     * 为Excel导出解析Excel的基本信息
     * @param response HTTP Response
     * @param excelExportDTO 导出的Excel相关数据
     * @return Excel的基本信息
     * @throws Exception
     */
    private ExcelExportBaseInfoDTO formExcelForExport(HttpServletResponse response, ExcelExportDTO excelExportDTO) throws Exception {
        // Excel文件名（包含相对路径，但不包含后缀）
        // 如果response不为空，则fileName没有相对路径；如果response为空，则fileName有相对路径
        // response不为空表示导出文件到客户端，response为空表示导出文件到服务器本地
        // 最终文件名
        String fileName = null;
        // 相对路径
        String relativePath = null;
        // 包含相对路径，但不包含后缀的文件名
        String relativeFileName = excelExportDTO.getFileName();
        if (response != null) {
            fileName = relativeFileName;
        } else {
            fileName = relativeFileName.substring(relativeFileName.lastIndexOf(File.separator) + 1);
            relativePath = relativeFileName.substring(0, relativeFileName.lastIndexOf(File.separator));
        }
        // 后缀名
        String suffix = Constants.excelType.XLSX.getValue();
        if (excelExportDTO.getExcelType() != null) {
            suffix = excelExportDTO.getExcelType();
        }
        // 创建Excel工作簿
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        if (response != null) {
            // 设置头信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            // 文件名进行编码
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'"
                    + URLEncoder.encode(fileName.replace("+", " ") + "." + suffix, "UTF-8"));
        }
        // 输出流
        OutputStream outputStream = null;
        if (response != null) {
            // 创建一个输出流
            outputStream = response.getOutputStream();
        } else {
            // excelExportDTO.getFileName()是相对路径
            outputStream = new FileOutputStream(ymlConfig.getLocation() + File.separator + relativePath
                    + File.separator + fileName + "." + suffix);
        }
        return new ExcelExportBaseInfoDTO(fileName, workbook, outputStream, suffix);
    }

    /**
     * 为Excel导出解析Excel中某个sheet的基本信息
     * @param excelExportDTO 导出的Excel相关数据
     * @param sheetName sheet名
     * @param workbook Excel工作簿
     * @throws Exception
     */
    private void formExcelSheetForExport(ExcelExportDTO excelExportDTO, String sheetName, SXSSFWorkbook workbook) throws Exception {
        // 最大列数（用于自适应列宽）
        int maxColumn = excelExportDTO.getMaxColumn();
        // 实际表格（包括标题行）的开始行
        int tableStartRow = excelExportDTO.getTableStartRow();
        // 在实际表格前面部分的单元格
        List<ExcelCellDTO> beforeDataCellList = excelExportDTO.getBeforeDataCellList();
        // 在实际表格后面部分的单元格
        List<ExcelCellDTO> afterDataCellList = excelExportDTO.getAfterDataCellList();
        // 标题行
        List<ExcelTitleDTO> titleList = excelExportDTO.getTitleList();
        // 数据
        List<?> dataList = excelExportDTO.getDataList();
        // 要合并的单元格
        List<ExcelCellRangeDTO> cellRangeList = excelExportDTO.getCellRangeList();

        // ==================== 开始生成Excel ==========================
        // 创建sheet页
        SXSSFSheet sheet = null;
        try {
            sheet = workbook.createSheet(sheetName);
        } catch (Exception e) {
            // 出现了重名现象，则创建非指定的名称Sheet
            sheet = workbook.createSheet();
        }
        // 如果设置了工作表保护，则设定密码
        if (StrUtil.isNotBlank(excelExportDTO.getSheetProtectPassword())) {
            sheet.protectSheet(excelExportDTO.getSheetProtectPassword());
        }
        // 存储最大列宽，用于处理中文不能自动调整列宽的问题
        Map<Integer, Integer> maxWidthMap = new HashMap<>();
        // 预处理合并单元格（以防出现Attempting to write ... that is already written to disk.）
        // Key：要合并的单元格的最后一行  Value：最后一行是该数值的所有合并单元格
        Map<Integer, List<ExcelCellRangeDTO>> lastRowToMergeMap = ExcelUtil.mergeCellGroup(cellRangeList);
        // 在实际表格上方的单元格
        if (beforeDataCellList != null) {
            // key：relativeRow相对行数，value：该行的单元格数据
            ExcelUtil.addDataToExcel(beforeDataCellList, 0, maxWidthMap, lastRowToMergeMap,
                    cellRangeList, excelExportDTO.getWrapSpecial(), workbook, sheet);
        }
        if (tableStartRow >= 0 && titleList != null && titleList.size() > 0
                && dataList != null && dataList.size() > 0) {
            // 存在有数据列表的情况下，生成数据列表
            // 创建表头
            SXSSFRow headRow = sheet.createRow(tableStartRow);
            // 表头统一居中，背景为灰色，加边框，可换行
            CellStyle titleNameStyle = workbook.createCellStyle();
            ExcelUtil.setAlignment(titleNameStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            ExcelUtil.setBackgroundColor(titleNameStyle, Constants.EXCEL_THEME_COLOR);
            ExcelUtil.setBorder(titleNameStyle, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
            // 可自动换行
            ExcelUtil.setWrapText(titleNameStyle, true);
            // 设置表头信息
            Map<String, Integer> titleNameMap = new HashMap<>();
            for (int column = 0; column < titleList.size(); column++) {
                ExcelTitleDTO titleDTO = titleList.get(column);
                titleNameMap.put(titleDTO.getTitleName(), column);
                SXSSFCell cell = headRow.createCell(column);
                // 设置表头内容（替换自动换行标识符）
                String titleContent = titleDTO.getTitleContent() == null ? "" : titleDTO.getTitleContent();
                if (excelExportDTO.getWrapSpecial() != null) {
                    cell.setCellValue(titleContent.replace(excelExportDTO.getWrapSpecial(), "\n"));
                }
                cell.setCellStyle(titleNameStyle);
                // 每个单元格都要计算该列的最大宽度
                ExcelUtil.computeMaxColumnWith(maxWidthMap, cell, tableStartRow, column, null, cellRangeList);
            }
            // 默认表数据统一居中，加边框，可换行
            CellStyle defaultDataStyle = workbook.createCellStyle();
            ExcelUtil.setAlignment(defaultDataStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            ExcelUtil.setBorder(defaultDataStyle, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
            // 可自动换行
            ExcelUtil.setWrapText(defaultDataStyle, true);
            // 判断是否有合并单元格，有的话就合并（此时所在行为tableStartRow）
            List<ExcelCellRangeDTO> tableStartMergeList = lastRowToMergeMap.get(tableStartRow);
            if (tableStartMergeList != null && tableStartMergeList.size() > 0) {
                for (ExcelCellRangeDTO cellRangeDTO : tableStartMergeList) {
                    // 合并单元格
                    ExcelUtil.doMergeCell(sheet, cellRangeDTO);
                }
            }
            // 特殊格式的单元格List
            List<ExcelCellDTO> specialStyleCellList = excelExportDTO.getSpecialStyleCellList();
            // 特殊格式的单元格Map（Key：相对行序号，从数据的第一行开始记为0  Value：特殊格式的单元格列表）
            Map<Integer, List<ExcelCellDTO>> specialStyleCellMap = new HashMap<>();
            if (specialStyleCellList != null && specialStyleCellList.size() > 0) {
                for (ExcelCellDTO specialStyleCell : specialStyleCellList) {
                    int relativeRow = specialStyleCell.getRelativeRow();
                    List<ExcelCellDTO> rowSpecialStyleCellList = specialStyleCellMap.get(relativeRow);
                    if (rowSpecialStyleCellList == null) {
                        rowSpecialStyleCellList = new ArrayList<>();
                        specialStyleCellMap.put(relativeRow, rowSpecialStyleCellList);
                    }
                    rowSpecialStyleCellList.add(specialStyleCell);
                }
            }
            // 填入表数据
            for (int dataRowCount = 0; dataRowCount < dataList.size(); dataRowCount++) {
                // 当前行
                int nowRow = tableStartRow + dataRowCount + 1;
                SXSSFRow dataRow = sheet.createRow(nowRow);
                Object dataObject = dataList.get(dataRowCount);
                // 特殊格式的单元格列表
                List<ExcelCellDTO> rowSpecialStyleCellList = specialStyleCellMap.get(dataRowCount);
                if (dataObject instanceof Map) {
                    // Map获取数据
                    for (int column = 0; column < titleList.size(); column++) {
                        String titleName = titleList.get(column).getTitleName();
                        Object data = ((Map) dataObject).get(titleName);
                        // 设置数据列的数据和格式
                        setDataAndStyle(excelExportDTO, data, dataRow, nowRow, column, defaultDataStyle,
                                rowSpecialStyleCellList, maxWidthMap, cellRangeList, workbook);
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
                            // 设置数据列的数据和格式
                            setDataAndStyle(excelExportDTO, data, dataRow, nowRow, column, defaultDataStyle,
                                    rowSpecialStyleCellList, maxWidthMap, cellRangeList, workbook);
                        }
                    }
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
        // 在实际表格下方的单元格
        if (afterDataCellList != null) {
            // key：relativeRow相对行数，value：该行的单元格数据
            ExcelUtil.addDataToExcel(afterDataCellList, tableStartRow + dataList.size() + 1, maxWidthMap,
                    lastRowToMergeMap, cellRangeList, excelExportDTO.getWrapSpecial(), workbook, sheet);
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
    }

    /**
     * 设置数据列的数据和格式
     * @param excelExportDTO 导出的Excel相关数据
     * @param data 数据值
     * @param dataRow 数据行
     * @param nowRow 当前行
     * @param column 当前列
     * @param defaultDataStyle 默认数据格式
     * @param rowSpecialStyleCellList 当前行特殊格式的单元格列表
     * @param maxWidthMap 存储的最大列宽Map
     * @param cellRangeList 要合并的单元格
     * @param workbook Excel工作簿
     */
    private static void setDataAndStyle(ExcelExportDTO excelExportDTO, Object data, SXSSFRow dataRow, int nowRow,
                                        int column, CellStyle defaultDataStyle, List<ExcelCellDTO> rowSpecialStyleCellList,
                                        Map<Integer, Integer> maxWidthMap, List<ExcelCellRangeDTO> cellRangeList,
                                        SXSSFWorkbook workbook) {
        SXSSFCell cell = dataRow.createCell(column);
        // 设置数据（替换自动换行标识符）
        if (excelExportDTO.getWrapSpecial() != null) {
            cell.setCellValue(data == null ? "" : data.toString().replace(excelExportDTO.getWrapSpecial(), "\n"));
        }
        // 指定列格式
        ExcelCellDTO dataCellStyleFromColumn = excelExportDTO.getDataSpecialStyleMap() == null ? null
                : excelExportDTO.getDataSpecialStyleMap().get(column);
        CellStyle dataStyle = defaultDataStyle;
        // 如果指定过某列的格式，则用该格式
        if (dataCellStyleFromColumn != null) {
            dataStyle = ExcelUtil.setCellStyle(dataCellStyleFromColumn, workbook);
        }
        // 如果指定过具体单元格格式，则用该格式
        if (rowSpecialStyleCellList != null && rowSpecialStyleCellList.size() > 0) {
            for (ExcelCellDTO rowSpecialStyleCell : rowSpecialStyleCellList) {
                if (rowSpecialStyleCell.getColumn() == column) {
                    dataStyle = ExcelUtil.setCellStyle(rowSpecialStyleCell, workbook);
                    break;
                }
            }
        }
        cell.setCellStyle(dataStyle);
        // 每个单元格都要计算该列的最大宽度
        ExcelUtil.computeMaxColumnWith(maxWidthMap, cell, nowRow, column, null, cellRangeList);
    }

    /**
     * 将workbook写入流，如果需要加密，则加密后写入流
     * @param workbook Excel工作簿
     * @param outputStream 输出流
     * @param suffix 后缀
     * @param encryptPassword 加密密码（若为null，则表示加密）
     */
    private SXSSFWorkbook doWriteExcelStream(SXSSFWorkbook workbook, OutputStream outputStream, String suffix,
                                             String encryptPassword) throws Exception {
        if (StrUtil.isBlank(encryptPassword)) {
            // 不加密，直接写入流
            workbook.write(outputStream);
            return workbook;
        }
        // 先把流写入临时文件，然后加密
        String tempFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "." + suffix;
        OutputStream tempOutputStream = null;
        try {
            tempOutputStream = new FileOutputStream(tempFileName);
            workbook.write(tempOutputStream);
        } finally {
            if (tempOutputStream != null) {
                tempOutputStream.close();
            }
        }
        encryptExcel(tempFileName, encryptPassword);
        // 将加密后的文件写回流
        InputStream encryptFileInputStream = null;
        try {
            encryptFileInputStream = new FileInputStream(tempFileName);
            byte[] read = new byte[10240];
            int length;
            // 开始读取
            while ((length = encryptFileInputStream.read(read)) != -1) {
                // 开始写入
                outputStream.write(read, 0, length);
            }
        } finally {
            if (null != encryptFileInputStream) {
                encryptFileInputStream.close();
            }
            // 删除临时的加密文件
            new File(tempFileName).delete();
        }
        return workbook;
    }

    /**
     * 对Excel进行加密（读写均加密）
     * @param fullFileName 带全路径的文件名
     * @param password 密码
     * @throws Exception
     */
    @Override
    public void encryptExcel(String fullFileName, String password) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem();
        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        // EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile, CipherAlgorithm.aes192, HashAlgorithm.sha384, -1, -1, null);
        Encryptor enc = info.getEncryptor();
        // 设置密码
        enc.confirmPassword(password);
        OPCPackage opc = null;
        try {
            opc = OPCPackage.open(new File(fullFileName), PackageAccess.READ_WRITE);
            OutputStream os = enc.getDataStream(fs);
            opc.save(os);
        } finally {
            if (opc != null) {
                opc.close();
            }
        }
        // 保存加密后的文件
        try (FileOutputStream fos = new FileOutputStream(fullFileName)) {
            fs.writeFilesystem(fos);
        }
    }

    /**
     * 通用解析Excel文件
     * @param fullFileName 本地全路径的文件名
     * @param excelParseClass 解析用Class
     * @param maxErrHintCount 最大错误提示数
     * @return 解析后的数据列表
     */
    @Override
    public <T> ReturnCommonDTO<List<T>> importParseExcel(String fullFileName,
                                                         Class<T> excelParseClass, Integer maxErrHintCount) {
        if (fullFileName == null || "".equals(fullFileName)) {
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "导入文件名为空");
        }
        try {
            InputStream fileInputStream = new FileInputStream(fullFileName);
            String excelType = fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
            return importParseExcel(excelType, fileInputStream, excelParseClass, maxErrHintCount);
        } catch (IOException e) {
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件读取失败");
        }
    }

    /**
     * 通用解析Excel文件
     * @param excelType Excel类型（xls或xlsx）
     * @param fileInputStream 文件输入流
     * @param excelParseClass 解析用Class
     * @param maxErrHintCount 最大错误提示数
     * @return 解析后的数据列表
     */
    @Override
    public <T> ReturnCommonDTO<List<T>> importParseExcel(String excelType, InputStream fileInputStream,
                                                         Class<T> excelParseClass, Integer maxErrHintCount) {
        try {
            if (!Constants.excelType.XLS.getValue().equals(excelType)
                    && !Constants.excelType.XLSX.getValue().equals(excelType)) {
                throw new CommonAlertException("Excel文件格式错误，后缀必须为.xls或.xlsx");
            }
            // 解析传入的DTO，分别获取每个属性
            Field[] excelParseFields = excelParseClass.getDeclaredFields();
            Field[] excelPropertyFields = ExcelPropertyDataDTO.class.getDeclaredFields();
            // Excel列属性列表
            List<ExcelPropertyDataDTO> excelPropertyDataList = new ArrayList<>();
            for (Field excelParseField : excelParseFields) {
                // 根据注解ExcelProperty的属性（一一映射）反射设置ExcelPropertyDataDTO的属性
                ExcelPropertyDataDTO excelPropertyDataDTO = new ExcelPropertyDataDTO();
                String excelParseFieldName = excelParseField.getName();
                // 设置属性名
                excelPropertyDataDTO.setPropertyName(excelParseFieldName);
                // 解析注解内容
                ExcelProperty excelPropertyAnnotation = excelParseField.getAnnotation(ExcelProperty.class);
                // 只记录有ExcelProperty注解的字段
                if (excelPropertyAnnotation != null) {
                    for (Field excelPropertyField : excelPropertyFields) {
                        String excelPropertyFieldName = excelPropertyField.getName();
                        Method annotationMethod = null;
                        try {
                            annotationMethod = excelPropertyAnnotation.annotationType().getDeclaredMethod(excelPropertyFieldName);
                        } catch (Exception e) {
                            // 说明是ExcelPropertyDataDTO多出来的属性，不匹配，直接跳过
                            continue;
                        }
                        Field excelPropertyDataField = excelPropertyDataDTO.getClass().getDeclaredField(excelPropertyFieldName);
                        if (annotationMethod == null || excelPropertyDataField == null) {
                            // 非共通属性，跳过
                            continue;
                        }
                        Object annotationValue = annotationMethod.invoke(excelPropertyAnnotation);
                        if (annotationValue != null) {
                            // 设置对象的访问权限，保证对private的属性的访问
                            excelPropertyDataField.setAccessible(true);
                            excelPropertyDataField.set(excelPropertyDataDTO, annotationValue);
                        }
                    }
                    // 追加到Excel列属性列表中
                    excelPropertyDataList.add(excelPropertyDataDTO);
                }
            }
            // 转换成Map（Key：列序号，从1开始   Value：ExcelPropertyDataDTO值）
            Map<Integer, ExcelPropertyDataDTO> excelPropertyDataMap = new HashMap<>();
            int maxColumn = 0;
            for (int j = 0; j < excelPropertyDataList.size(); j++) {
                ExcelPropertyDataDTO excelPropertyDataDTO = excelPropertyDataList.get(j);
                int index = excelPropertyDataDTO.getIndex();
                ExcelPropertyDataDTO existProperty = excelPropertyDataMap.get(index);
                // 验证列序号重复
                if (existProperty != null) {
                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第" + (j + 1)
                            + "列序号重复");
                }
                // 设置最大列
                if (index > maxColumn) {
                    maxColumn = index;
                }
                // 初始化验证正则
                if (excelPropertyDataDTO.getRegex() != null) {
                    Pattern pattern = Pattern.compile(excelPropertyDataDTO.getRegex());
                    excelPropertyDataDTO.setPattern(pattern);
                }
                excelPropertyDataMap.put(excelPropertyDataDTO.getIndex(), excelPropertyDataDTO);
            }
            if (maxColumn != excelPropertyDataList.size()) {
                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "列序号必须从1开始且递增幅度为1");
            }

            List<T> dataList = new ArrayList<>();
            Workbook workbook = null;
            Sheet sheet = null;
            Row firstRow = null;
            if (Constants.excelType.XLS.getValue().equals(excelType)) {
                workbook = new HSSFWorkbook(fileInputStream);
                sheet = workbook.getSheetAt(0);
                // 第一行（列名）
                firstRow = sheet.getRow(0);
            } else {
                workbook = new XSSFWorkbook(fileInputStream);
                sheet = workbook.getSheetAt(0);
                // 第一行（列名）
                firstRow = sheet.getRow(0);
            }
            int firstMaxCell = firstRow.getLastCellNum();
            if (firstMaxCell != maxColumn) {
                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "第1行列数不是" + maxColumn);
            }

            // 验证第一行是否正确
            for (int col = 1; col <= firstMaxCell; col++) {
                String cellValue = ExcelUtil.getCellValueOfExcel(firstRow.getCell(col - 1), null);
                if (cellValue == null || !cellValue.trim().equals(excelPropertyDataMap.get(col).getValue())) {
                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "【"
                            + ExcelUtil.getColumnCorrespondingLabel(col) + "1】的列名不正确，请参照模板修改（注意列顺序不能错乱）");
                }
            }
            // 统计错误提示数，不超过最大提示数
            int errHintCount = 0;
            StringJoiner errInfoSj = new StringJoiner("、");
            // 读取数据行的每一行内容进行解析
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // 初始化该行的返回数据
                Map<String, String> data = new HashMap<>();
                // 获取这一行
                Row dataRow = sheet.getRow(i);
                int dataMaxCell = dataRow.getLastCellNum();
                if (dataMaxCell > maxColumn) {
                    errInfoSj.add("第" + (i + 1) + "行列数超过" + maxColumn);
                    errHintCount++;
                    if (maxErrHintCount != null && errHintCount >= maxErrHintCount) {
                        return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), errInfoSj.toString());
                    }
                    continue;
                } else if (dataMaxCell < maxColumn) {
                    // 数据行列数小于标题行列数，则后面几列都是空值
                }
                // 遍历这一行的每一列
                for (int col = 1; col < maxColumn + 1; col++) {
                    // 属性数据
                    ExcelPropertyDataDTO excelPropertyDataDTO = excelPropertyDataMap.get(col);
                    // 属性名
                    String propertyName = excelPropertyDataDTO.getPropertyName();
                    String cellValue = "";
                    if (col <= dataMaxCell) {
                        // 获取单元格中填入的值
                        cellValue = ExcelUtil.getCellValueOfExcel(dataRow.getCell(col - 1), excelPropertyDataDTO.getDateFormat());
                    }
                    if (cellValue == null || "".equals(cellValue.trim())) {
                        // 单元格中的数据为空
                        if (!"".equals(excelPropertyDataDTO.getEmptyToDefault())) {
                            // 有默认值，则设置默认值
                            cellValue = excelPropertyDataDTO.getEmptyToDefault();
                        } else if (!excelPropertyDataDTO.getNullable()) {
                            // 限制不允许为空，则报错
                            errInfoSj.add("【" + ExcelUtil.getColumnCorrespondingLabel(col) + (i + 1) + "】数据为空");
                            errHintCount++;
                            if (maxErrHintCount != null && errHintCount >= maxErrHintCount) {
                                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), errInfoSj.toString());
                            }
                            continue;
                        } else {
                            // 空数据直接设置空字符串
                            cellValue = "";
                        }
                        data.put(propertyName, cellValue);
                    } else {
                        // 去掉前后空白符
                        cellValue = cellValue.trim();
                        // 指定选项范围
                        String[] optionList = excelPropertyDataDTO.getOptionList();
                        if (optionList != null && optionList.length > 0) {
                            // 验证指定选项范围
                            boolean inOption = false;
                            for (String option : optionList) {
                                if (option.equals(cellValue)) {
                                    inOption = true;
                                    break;
                                }
                            }
                            if (!inOption) {
                                errInfoSj.add("【" + ExcelUtil.getColumnCorrespondingLabel(col) + (i + 1) + "】数据范围不正确");
                                errHintCount++;
                                if (maxErrHintCount != null && errHintCount >= maxErrHintCount) {
                                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), errInfoSj.toString());
                                }
                                continue;
                            }
                        } else if (excelPropertyDataDTO.getPattern() != null) {
                            // 有校验的列
                            Matcher matcher = excelPropertyDataDTO.getPattern().matcher(cellValue);
                            if (!matcher.matches()) {
                                errInfoSj.add("【" + ExcelUtil.getColumnCorrespondingLabel(col) + (i + 1) + "】数据格式不正确");
                                errHintCount++;
                                if (maxErrHintCount != null && errHintCount >= maxErrHintCount) {
                                    return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), errInfoSj.toString());
                                }
                                continue;
                            }
                        } else {
                            // 没有校验的列
                        }
                        data.put(propertyName, cellValue);
                    }
                }
                // 将Map转为Bean
                T dataT = BeanUtil.mapToBean(data, excelParseClass, true);
                // 将解析出的这一行的数据添加到列表中
                dataList.add(dataT);
            }
            // 错误信息整体返回
            if (errInfoSj.length() > 0) {
                return new ReturnUploadCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), errInfoSj.toString());
            }
            // 返回解析后的全部数据
            return new ReturnUploadCommonDTO(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ReturnUploadCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "导入文件解析失败");
        }
    }

    /**
     * Controller中获取单条结果
     * @param resultOfList 列表结果
     * @return
     */
    @Override
    public <O> ReturnCommonDTO<O> doGetSingleResult(ReturnCommonDTO<List<O>> resultOfList) {
        if (Constants.commonReturnStatus.SUCCESS.getValue().equals(resultOfList.getResultCode())) {
            if (resultOfList.getData() != null && resultOfList.getData().size() != 0) {
                // 有数据
                if (resultOfList.getData().size() == 1) {
                    // 刚好一条数据
                    return new ReturnCommonDTO<>(resultOfList.getData().get(0));
                } else {
                    // 多余一条数据
                    throw new CommonAlertException("数据异常，超过一条");
                }
            } else {
                // 没有数据
                return new ReturnCommonDTO<>();
            }
        } else {
            // 查询操作异常
            return new ReturnCommonDTO<>(resultOfList.getResultCode(), resultOfList.getErrMsg());
        }
    }

    /**
     * 从缓存获取数据
     * @param cacheName
     * @param key
     * @return
     */
    @Override
    public ReturnCommonDTO<String> getDataFromCache(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "cacheName不存在");
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "key不存在");
        }
        Object value = valueWrapper.get();
        if (value == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "value为空");
        }
        return new ReturnCommonDTO<>(Constants.commonReturnStatus.SUCCESS.getValue(), null, JSON.toJSONString(value));
    }

    /**
     * 从LocalCache获取数据
     * @param key
     * @return
     */
    @Override
    public ReturnCommonDTO<String> getDataFromLocalCache(String key) {
        LocalCacheEntity cacheEntity = LocalCache.getValue(key);
        if (cacheEntity == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "key不存在");
        }
        LocalCacheEntity cacheEntityClone = LocalCache.clone(cacheEntity);
        Object value = cacheEntityClone.getObj();
        if (value == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "value为空");
        }
        return new ReturnCommonDTO<>(Constants.commonReturnStatus.SUCCESS.getValue(), null, JSON.toJSONString(value));
    }

    /**
     * 从LocalCache获取数据
     * @param key
     * @return
     */
    @Override
    public ReturnCommonDTO<Integer> getTtlFromLocalCache(String key) {
        LocalCacheEntity cacheEntity = LocalCache.getValue(key);
        if (cacheEntity == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "key不存在");
        }
        LocalCacheEntity cacheEntityClone = LocalCache.clone(cacheEntity);
        Object value = cacheEntityClone.getObj();
        if (value == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "value为空");
        }
        return new ReturnCommonDTO<>(Constants.commonReturnStatus.SUCCESS.getValue(), null, cacheEntityClone.getExpireTime());
    }

}
