package ${packageName}.service.impl;

import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.ExcelTitleDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import ${packageName}.dto.help.ReturnUploadCommonDTO;
import ${packageName}.service.CommonService;
import ${packageName}.util.FileUtil;
import ${packageName}.web.rest.errors.CommonException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
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
        try {
            if (!file.exists()) {
                return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件不存在");
            }
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(changeFileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                log.error("文件下载失败：" + fullFileName, e);
                return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件下载失败");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        log.error("文件关闭失败：" + fullFileName, e);
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        log.error("文件关闭失败：" + fullFileName, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("文件下载失败：" + fullFileName, e);
            return new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "文件下载失败");
        }
        return new ReturnCommonDTO();
    }

    /**
     * 导出Excel
     * @param response HTTP Response
     * @param fileName Excel文件名（不包含后缀）
     * @param sheetName sheet名
     * @param headTitle 总标题
     * @param titleList 标题行
     * @param dataList 数据
     * @return 导出结果
     * @throws Exception
     */
    public ReturnCommonDTO exportExcel(HttpServletResponse response, String fileName, String sheetName, String headTitle,
                                       List<ExcelTitleDTO> titleList, List<?> dataList) {
        try {
            // 创建poi导出数据对象
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
            // 设置头信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            // 一定要设置成xlsx格式
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'"
                    + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            // 输出流
            ServletOutputStream outputStream = null;
            try {
                // 创建一个输出流
                outputStream = response.getOutputStream();
                // 创建sheet页
                SXSSFSheet sheet = sxssfWorkbook.createSheet(sheetName);
                // 当前操作的行
                int currentRow = 0;
                // 创建标题行
                if (headTitle != null) {
                    // 参数：起始行、终止行、起始列、终止列
                    CellRangeAddress totalTitleRegion = new CellRangeAddress(0, 0, 0, titleList.size() - 1);
                    sheet.addMergedRegion(totalTitleRegion);
                    SXSSFRow headTitleRow = sheet.createRow(0);
                    currentRow++;
                    headTitleRow.createCell(0).setCellValue(headTitle);
                }
                // 创建表头
                SXSSFRow headRow = sheet.createRow(currentRow);
                currentRow++;
                // 设置表头信息
                Map<String, Integer> titleNameMap = new HashMap<>();
                for (int column = 0; column < titleList.size(); column++) {
                    ExcelTitleDTO titleDTO = titleList.get(column);
                    titleNameMap.put(titleDTO.getTitleName(), column);
                    headRow.createCell(column).setCellValue(titleDTO.getTitleContent());
                }
                // 填入表数据
                for (int dataRowCount = 0; dataRowCount < dataList.size(); dataRowCount++) {
                    SXSSFRow dataRow = sheet.createRow(currentRow);
                    currentRow++;
                    Object dataObject = dataList.get(dataRowCount);
                    if (dataObject instanceof Map) {
                        // Map获取数据
                        for (int column = 0; column < titleList.size(); column++) {
                            String titleName = titleList.get(column).getTitleName();
                            Object data = ((Map) dataObject).get(titleName);
                            dataRow.createCell(column).setCellValue(data == null ? "" : data.toString());
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
                                dataRow.createCell(column).setCellValue(data == null ? "" : data.toString());
                            }
                        }
                    }
                }
                // 写入数据
                sxssfWorkbook.write(outputStream);
            } finally {
                // 关闭
                if (outputStream != null) {
                    outputStream.close();
                }
                sxssfWorkbook.close();
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
        if (".xlsx".equals(fullFileName)) {
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
                String cellValue = getCellValueOfExcel(firstRow.getCell(j));
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
                    String cellValue = getCellValueOfExcel(dataRow.getCell(j));
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

    /**
     * 获取Excel的XSSFCell的值
     * @param cell 传入的cell
     * @return cell的值
     */
    public String getCellValueOfExcel(XSSFCell cell) {
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

}
