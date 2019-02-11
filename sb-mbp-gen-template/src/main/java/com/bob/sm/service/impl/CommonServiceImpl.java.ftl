package ${packageName}.service.impl;

import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import ${packageName}.service.CommonService;
import ${packageName}.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class CommonServiceImpl implements CommonService {

    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private YmlConfig ymlConfig;

    /**
     * 上传文件到服务器
     * @param file 待上传的文件
     * @return 上传结果（路径、上传时间）
     * @throws Exception 上传失败
     */
    public ReturnFileUploadDTO uploadFile(MultipartFile file) throws Exception {
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
            return fileUploadDTO;
        } catch (Exception e) {
            log.error("文件上传失败：" + fileName, e);
            throw new Exception("文件上传失败：" + fileName + " -> " + e.getMessage());
        }

    }

}
