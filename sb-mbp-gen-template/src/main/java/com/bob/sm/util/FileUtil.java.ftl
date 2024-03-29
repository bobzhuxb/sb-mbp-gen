package ${packageName}.util;

import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.CompressChangeFileDTO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * @author Bob
 */
@Service
public class FileUtil {

    /**
     * 图片压缩
     * @param srcFileName 原图片路径
     * @param destFileName 目标图片路径
     * @return 共压缩的次数
     */
    public static int compressPic(YmlConfig ymlConfig, String srcFileName, String destFileName) throws Exception {
        int compressCount = 0;
        // 压缩后文件的限制大小
        long compressMaxSize = Long.parseLong(ymlConfig.getPicCompressMaxSize());
        // 压缩文件的限制次数
        int compressMaxTimes = Integer.parseInt(ymlConfig.getPicCompressMaxTimes());
        // 每次图片压缩图片的大小（长宽），范围0到1，1f就是原图大小，0.5就是原图的一半大小
        float compressScale = Float.parseFloat(ymlConfig.getPicCompressScale());
        // 次图片压缩的质量，范围0到1，越接近于1质量越好，越接近于0质量越差
        float compressQuality = Float.parseFloat(ymlConfig.getPicCompressQuality());
        // 文件后缀名（包括.）
        String extension = srcFileName.substring(srcFileName.lastIndexOf("."));
        // 压缩后的临时文件
        String currentTempFileName = srcFileName;
        // 要删除的临时文件
        String lastTempFileName = currentTempFileName;
        // 初始化压缩后文件大小
        long tempFileLength = new File(currentTempFileName).length();
        // 循环压缩直到文件大小达标（小于compressMaxSize）或已到达指定压缩次数compressMaxTimes，跳出循环，压缩结束
        while (compressCount <= compressMaxTimes && tempFileLength > compressMaxSize) {
            currentTempFileName = srcFileName.substring(0, srcFileName.lastIndexOf("."))
                    + "-compress-" + compressCount + extension;
            // 图片格式，启动图片压缩
            Thumbnails
                    // 原图文件路径
                    .of(lastTempFileName)
                    // scale是可以指定图片的大小，值在0到1之间，1f就是原图大小，0.5就是原图的一半大小，
                    // 这里的大小是指图片的长宽
                    .scale(compressScale)
                    // outputQuality是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差
                    .outputQuality(compressQuality)
                    // 压缩后文件的路径
                    .toFile(currentTempFileName);

            // 计算压缩后的文件大小
            tempFileLength = new File(currentTempFileName).length();
            // 先删除压缩前的原文件，注意，初始文件不要删除
            if (!lastTempFileName.equals(srcFileName)) {
                new File(lastTempFileName).delete();
            }
            // 把目标文件作为原文件，以便下次继续压缩
            lastTempFileName = currentTempFileName;
            // 压缩次数增加1
            compressCount++;
        }
        if (currentTempFileName.equals(srcFileName)) {
            // 完全没有压缩过，直接拷贝一份源文件作为目标文件即可
            FileUtils.copyFile(new File(srcFileName), new File(destFileName));
        }
        // 返回压缩次数
        return compressCount;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 打包下载文件
     * @param compressChangeFileList  待打包的文件
     * @param zipPath 打包完成后的文件全路径
     */
    public static void zipFile(List<CompressChangeFileDTO> compressChangeFileList, String zipPath) throws IOException {
        FileInputStream fis = null;

        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (CompressChangeFileDTO compressChangeFile : compressChangeFileList) {
                File inputFile = new File(compressChangeFile.getFullFileName());
                if (inputFile.exists()) {
                    if (inputFile.isFile()) {
                        fis = new FileInputStream(inputFile);
                        String singleFileNameChange = compressChangeFile.getFullFileName();
                        if (compressChangeFile.getChangeFileName() != null) {
                            singleFileNameChange = compressChangeFile.getChangeFileName();
                        }
                        ZipEntry zipEntry = new ZipEntry(singleFileNameChange);
                        zos.putNextEntry(zipEntry);
                        int len = 0;
                        byte[] bytes = new byte[1024 * 5];
                        while ((len = fis.read(bytes)) > 0) {
                            zos.write(bytes, 0, len);
                        }
                        zos.closeEntry();
                        fis.close();
                    }
                }
            }
        }
    }

    /**
     * 图片文件转化成Base64字符串
     * @param fullFileName 全路径的图片文件名
     * @return
     */
    public static String getBase64ImgStr(String fullFileName) throws IOException {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try (InputStream in = new FileInputStream(fullFileName)) {
            data = new byte[in.available()];
            in.read(data);
        }
        // 返回Base64编码过的字节数组字符串
        return new String(Base64.getEncoder().encode(data));
    }

    /**
     * Base64字符串转化成图片文件
     * @param imgStr Base64字符串
     * @param fullFileName 输出的图片全路径文件名
     * @return
     */
    public static void generateImageFromBase64(String imgStr, String fullFileName) throws IOException {
        try (OutputStream out = new FileOutputStream(fullFileName)) {
            // Base64解码
            byte[] b = Base64.getDecoder().decode(imgStr);
            for (int i = 0;i < b.length; ++i) {
                if (b[i] < 0) {
                    // 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成图片
            out.write(b);
            out.flush();
        }
    }

}
