package com.bob.at.service.impl;

import com.bob.at.config.YmlConfig;
import com.bob.at.dto.help.ReturnFileUploadDTO;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 共通
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    public ReturnFileUploadDTO uploadFileToLocal(MultipartFile file, boolean changeFileName, Date nowDate) {
        // 获取文件名和文件内容
        String fileName = file.getOriginalFilename();
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        // 后缀（注意扩展名可能不存在的情况）
        int lastPointPosition = fileName.lastIndexOf(".");
        String extension = lastPointPosition < 0 ? "" : fileName.substring(lastPointPosition);
        String relativePath = null;
        String newFileName = null;
        if (changeFileName) {
            // 相对路径
            relativePath = new SimpleDateFormat("yyyyMMdd").format(nowDate);
            // 新文件名
            int fileRandomInt = new Random().nextInt(1000);
            newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                    + fileRandomInt + extension;
        } else {
            relativePath = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate);
            newFileName = fileName;
        }
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + File.separator + relativePath;
        // 生成文件
        File targetPath = new File(localPath);
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
        String localFileName = localPath + File.separator + newFileName;
        File targetFile = new File(localFileName);
        // 文件保存到服务器
        try {
            file.transferTo(targetFile);
            ReturnFileUploadDTO fileUploadDTO = new ReturnFileUploadDTO();
            fileUploadDTO.setOriginalFileName(fileName);
            fileUploadDTO.setRelativePath(relativePath + File.separator + newFileName);
            fileUploadDTO.setAbsolutePath(localFileName);
            fileUploadDTO.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
            return fileUploadDTO;
        } catch (Exception e) {
            throw CommonException.errWithDetail("文件上传失败：" + fileName, e.getMessage(), e);
        }
    }

    @Override
    public String getUploadFullFilePath(Date nowDate) {
        String relativePath = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate);
        String localPath = ymlConfig.getLocation() + File.separator + relativePath + File.separator;
        return localPath;
    }

}
