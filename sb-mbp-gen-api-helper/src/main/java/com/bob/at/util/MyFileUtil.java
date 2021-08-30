package com.bob.at.util;

import com.bob.at.dto.help.CompressChangeFileDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * @author Bob
 */
@Service
public class MyFileUtil {

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

}
