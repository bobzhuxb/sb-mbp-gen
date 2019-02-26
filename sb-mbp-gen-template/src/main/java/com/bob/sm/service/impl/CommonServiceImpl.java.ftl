package ${packageName}.service.impl;

import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.*;
import ${packageName}.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
@Transactional
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
            return new ReturnCommonDTO<>(fileUploadDTO);
        } catch (Exception e) {
            log.error("文件上传失败：" + fileName, e);
            throw new CommonException("文件上传失败：" + fileName + " -> " + e.getMessage());
        }

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
                                       List<ExcelTitleDTO> titleList, List<Object> dataList) {
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
                    CellRangeAddress totalTitleRegion = new CellRangeAddress(0, 1, 0, titleList.size() - 1);
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
                    Object data = null;
                    if (dataObject instanceof Map) {
                        // Map获取数据
                        for (int column = 0; column < titleList.size(); column++) {
                            String titleName = titleList.get(column).getTitleName();
                            data = ((Map) dataObject).get(titleName);
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
                                data = field.get(dataObject);
                            }
                        }
                    }
                    dataRow.createCell(0).setCellValue(data == null ? "" : data.toString());
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

}
