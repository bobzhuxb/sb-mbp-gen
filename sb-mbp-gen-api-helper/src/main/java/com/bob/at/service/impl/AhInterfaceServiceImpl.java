package com.bob.at.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.config.Constants;
import com.bob.at.config.YmlConfig;
import com.bob.at.domain.AhInterface;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.adapter.ApiAdapterConfigDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.CompressChangeFileDTO;
import com.bob.at.dto.help.FileExportDTO;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.ReturnFileUploadDTO;
import com.bob.at.mapper.AhInterfaceMapper;
import com.bob.at.mapper.AhProjectMapper;
import com.bob.at.service.AhInterfaceService;
import com.bob.at.util.MyBeanUtil;
import com.bob.at.util.MyFileUtil;
import com.bob.at.web.rest.errors.CommonAlertException;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口
 * @author Bob
 */
@Service
public class AhInterfaceServiceImpl extends ServiceImpl<AhInterfaceMapper, AhInterface>
        implements AhInterfaceService {

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private AhProjectMapper ahProjectMapper;

    @Autowired
    private CommonService commonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO createAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        AhInterface ahInterface = new AhInterface();
        if (!ahInterfaceDTO.getHttpUrl().startsWith("/")) {
            throw new CommonAlertException("URL格式错误，必须以/开头");
        }
        AhInterface existInterface = baseMapper.selectOne(new QueryWrapper<AhInterface>()
                .eq(AhInterface._httpUrl, ahInterfaceDTO.getHttpUrl())
                .eq(AhInterface._httpMethod, ahInterfaceDTO.getHttpMethod())
                .eq(AhInterface._interNo, ahInterfaceDTO.getInterNo()));
        if (existInterface != null) {
            throw new CommonException("该接口已存在");
        }
        MyBeanUtil.copyNonNullProperties(ahInterfaceDTO, ahInterface);
        baseMapper.insert(ahInterface);
        return new ReturnCommonDTO(Constants.commonReturnStatus.SUCCESS.getValue(), null, ahInterface.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO updateAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        AhInterface ahInterface = new AhInterface();
        if (!ahInterfaceDTO.getHttpUrl().startsWith("/")) {
            throw new CommonAlertException("URL格式错误，必须以/开头");
        }
        AhInterface existInterface = baseMapper.selectOne(new QueryWrapper<AhInterface>()
                .eq(AhInterface._httpUrl, ahInterfaceDTO.getHttpUrl())
                .eq(AhInterface._httpMethod, ahInterfaceDTO.getHttpMethod())
                .eq(AhInterface._interNo, ahInterfaceDTO.getInterNo())
                .eq(AhInterface._ahProjectId, ahInterfaceDTO.getAhProjectId())
                .last("limit 1"));
        if (existInterface != null && !existInterface.getId().equals(ahInterfaceDTO.getId())) {
            throw new CommonException("该接口已存在");
        }
        MyBeanUtil.copyNonNullProperties(ahInterfaceDTO, ahInterface);
        baseMapper.updateById(ahInterface);
        return new ReturnCommonDTO(Constants.commonReturnStatus.SUCCESS.getValue(), null, ahInterface.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhInterface(String id) {
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhInterfaces(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<AhInterfaceDTO> getAhInterface(String id) {
        AhInterface inter = baseMapper.selectById(id);
        AhInterfaceDTO interfaceDTO = new AhInterfaceDTO();
        MyBeanUtil.copyNonNullProperties(inter, interfaceDTO);
        return new ReturnCommonDTO<>(interfaceDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<List<AhInterfaceDTO>> getAllAhInterfaces(AhInterfaceCriteria criteria) {
        List<AhInterface> interfaceList = baseMapper.selectList(new QueryWrapper<AhInterface>()
                .eq(StrUtil.isNotBlank(criteria.getProjectIdEq()), AhInterface._ahProjectId, criteria.getProjectIdEq()));
        if (interfaceList == null || interfaceList.size() == 0) {
            return new ReturnCommonDTO<>(new ArrayList<>());
        }
        List<AhInterfaceDTO> interfaceDTOList = interfaceList.stream().map(inter -> {
            AhInterfaceDTO interfaceDTO = new AhInterfaceDTO();
            MyBeanUtil.copyNonNullProperties(inter, interfaceDTO);
            return interfaceDTO;
        }).collect(Collectors.toList());
        return new ReturnCommonDTO<>(interfaceDTOList);
    }

    @Override
    public void exportInterJson(String interfaceId, HttpServletResponse response) {
        AhInterface ahInterface = baseMapper.selectById(interfaceId);
        // 生成JSON文件
        FileExportDTO fileExportDTO = generateJsonFile(ahInterface);
        // 下载文件
        doExportFile(fileExportDTO, response);
    }

    @Override
    public void exportProjectInterJson(String projectId, HttpServletResponse response) {
        Date nowDate = new Date();
        // 获取工程
        AhProject ahProject = ahProjectMapper.selectById(projectId);
        // 获取工程对应的接口列表
        List<AhInterface> ahInterfaceList = baseMapper.selectList(new QueryWrapper<AhInterface>()
                .eq(AhInterface._ahProjectId, projectId));
        if (ahInterfaceList == null || ahInterfaceList.size() == 0) {
            throw new CommonException("该工程没有接口");
        }
        // 压缩文件
        String relativePath = new SimpleDateFormat("yyyyMMdd").format(nowDate);
        int fileRandomInt = new Random().nextInt(1000);
        // 新文件名
        String newZipFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + ".zip";
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + File.separator + relativePath;
        String localZipFileName = localPath + File.separator + newZipFileName;
        // 压缩文件
        List<CompressChangeFileDTO> compressChangeFileList = new ArrayList<>();
        for (AhInterface ahInterface : ahInterfaceList) {
            // 生成JSON文件
            FileExportDTO fileExportDTO = generateJsonFile(ahInterface);
            compressChangeFileList.add(new CompressChangeFileDTO(fileExportDTO.getRealFileName(),
                    fileExportDTO.getLocalFullFileName()));
        }
        try {
            MyFileUtil.zipFile(compressChangeFileList, localZipFileName);
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
        // 下载文件
        doExportFile(new FileExportDTO(localZipFileName, ahProject.getName() + ".zip"), response);
    }

    private void doExportFile(FileExportDTO fileExportDTO, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileExportDTO.getLocalFullFileName());
            // 文件下载的ContentType
            response.setHeader("content-type", "application/json");
            response.setContentType("application/json");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(fileExportDTO.getRealFileName().replace("+", " "), "UTF-8"));
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
                throw new CommonException(e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {}
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {}
                }
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    /**
     * 生成接口的JSON文件
     * @param ahInterface 接口内容
     * @return 格式化之后的JSON字符串
     */
    private FileExportDTO generateJsonFile(AhInterface ahInterface) {
        Date nowDate = new Date();
        // 获取接口数据，并生成JSON文件名
        String httpUrl = ahInterface.getHttpUrl();
        String httpMethod = ahInterface.getHttpMethod();
        String interNo = ahInterface.getInterNo();
        String fileName = httpUrl.substring(1).replace("/", "_") + "_" + httpMethod.toUpperCase()
                + "_" + interNo + ".json";
        // 获取扩展名（注意扩展名可能不存在的情况）
        int lastPointPosition = fileName.lastIndexOf(".");
        String extension = lastPointPosition < 0 ? "" : fileName.substring(lastPointPosition);
        // 相对路径
        String relativePath = new SimpleDateFormat("yyyyMMdd").format(nowDate);
        int fileRandomInt = new Random().nextInt(1000);
        // 新文件名
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate) + "_"
                + fileRandomInt + extension;
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + File.separator + relativePath;
        String localFileName = localPath + File.separator + newFileName;
        // 获取要输出的JSON信息，并格式化
        ApiAdapterConfigDTO configDTO = JSONObject.parseObject(ahInterface.getDataJson(), ApiAdapterConfigDTO.class);
        String prettyJson = JSON.toJSONString(configDTO, SerializerFeature.PrettyFormat);
        FileUtil.writeString(prettyJson, localFileName, "UTF-8");
        return new FileExportDTO(localFileName, fileName);
    }

    @Override
    @Transactional
    public ReturnCommonDTO importInterfaceJson(String projectId, MultipartFile[] files) {
        List<ApiAdapterConfigDTO> configList = new ArrayList<>();
        for (MultipartFile file : files) {
            // 获取文件名和文件信息
            ReturnFileUploadDTO fileUploadDTO = commonService.uploadFileToLocal(file, true, new Date());
            String fullFileName = fileUploadDTO.getAbsolutePath();
            String jsonData = FileUtil.readString(new File(fullFileName), "UTF-8");
            String originalFileName = fileUploadDTO.getOriginalFileName();
            ApiAdapterConfigDTO configDTO;
            try {
                configDTO = JSON.parseObject(jsonData, ApiAdapterConfigDTO.class);
                if (!configDTO.getHttpUrl().startsWith("/")) {
                    throw new CommonException("URL格式错误，必须以/开头");
                }
                String correctFileName = configDTO.getHttpUrl().substring(1).replace("/", "_") + "_"
                        + configDTO.getHttpMethod() + "_" + configDTO.getInterNo() + ".json";
                if (!correctFileName.equals(originalFileName)) {
                    throw new CommonException("文件名与内容不匹配");
                }
            } catch (Exception e) {
                throw new CommonException("文件" + originalFileName + "错误：" + e.getMessage());
            }
            configList.add(configDTO);
        }
        // 存入数据库
        List<AhInterface> interfaceList = baseMapper.selectList(new QueryWrapper<AhInterface>().eq(AhInterface._ahProjectId, projectId));
        Map<String, AhInterface> interfaceKeyMap = new HashMap<>();
        if (interfaceList != null && interfaceList.size() > 0) {
            for (AhInterface ahInterface : interfaceList) {
                String key = ahInterface.getHttpUrl().substring(1).replace("/", "_")
                        + ahInterface.getHttpMethod() + "_" + ahInterface.getInterNo();
                interfaceKeyMap.put(key, ahInterface);
            }
        }
        for (ApiAdapterConfigDTO configDTO : configList) {
            String key = configDTO.getHttpUrl().substring(1).replace("/", "_")
                    + configDTO.getHttpMethod() + "_" + configDTO.getInterNo();
            AhInterface existInterface = interfaceKeyMap.get(key);
            AhInterface savingInterface = new AhInterface();
            if (existInterface != null) {
                savingInterface.setId(existInterface.getId());
            }
            savingInterface.setInterNo(configDTO.getInterNo());
            savingInterface.setHttpUrl(configDTO.getHttpUrl());
            savingInterface.setHttpMethod(configDTO.getHttpMethod());
            savingInterface.setAddDefaultPrefix(configDTO.getAddDefaultPrefix());
            savingInterface.setInterDescr(configDTO.getInterDescr());
            savingInterface.setReturnType(configDTO.getReturnType());
            savingInterface.setDataJson(JSON.toJSONString(configDTO));
            savingInterface.setAhProjectId(projectId);
            if (existInterface != null) {
                // 修改
                baseMapper.updateById(savingInterface);
            } else {
                // 新增
                baseMapper.insert(savingInterface);
            }
        }
        return new ReturnCommonDTO();
    }
}