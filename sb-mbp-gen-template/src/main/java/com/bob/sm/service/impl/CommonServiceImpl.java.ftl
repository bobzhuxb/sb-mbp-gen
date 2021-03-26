package ${packageName}.service.impl;

import com.alibaba.fastjson.JSON;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnFileUploadDTO;
import ${packageName}.dto.help.ReturnUploadCommonDTO;
import ${packageName}.service.CommonService;
import ${packageName}.util.FileUtil;
import ${packageName}.util.LocalCache;
import ${packageName}.util.LocalCacheEntity;
import ${packageName}.web.rest.errors.CommonAlertException;
import ${packageName}.web.rest.errors.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
            // 文件下载的ContentType
            response.setHeader("content-type", "application/json");
            response.setContentType("application/json");
            if (changeFileName == null || "".equals(changeFileName.trim())) {
                changeFileName = oriFileName.substring(oriFileName.lastIndexOf(File.separator) + 1);
            }
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(changeFileName.replace("+", " "), "UTF-8"));
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
