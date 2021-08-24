package com.bob.at.service.impl;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.config.YmlConfig;
import com.bob.at.domain.AhClassCode;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.ReturnFileUploadDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.service.AhClassCodeService;
import com.bob.at.util.DynamicLoader;
import com.bob.at.util.MemoryClassLoader;
import com.bob.at.util.MyBeanUtil;
import com.bob.at.util.MyClassLoader;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 可用实体类
 * @author Bob
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AhClassCodeServiceImpl extends ServiceImpl<AhClassCodeMapper, AhClassCode>
        implements AhClassCodeService {

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    public ReturnCommonDTO createAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        AhClassCode ahClassCode = new AhClassCode();
        MyBeanUtil.copyNonNullProperties(ahClassCodeDTO, ahClassCode);
        baseMapper.insert(ahClassCode);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO updateAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        AhClassCode ahClassCode = new AhClassCode();
        MyBeanUtil.copyNonNullProperties(ahClassCodeDTO, ahClassCode);
        baseMapper.updateById(ahClassCode);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO deleteAhClassCode(String id) {
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO deleteAhClassCodes(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO<AhClassCodeDTO> getAhClassCode(String id) {
        AhClassCode inter = baseMapper.selectById(id);
        AhClassCodeDTO classCodeDTO = new AhClassCodeDTO();
        MyBeanUtil.copyNonNullProperties(inter, classCodeDTO);
        return new ReturnCommonDTO<>(classCodeDTO);
    }

    @Override
    public ReturnCommonDTO<List<AhClassCodeDTO>> getAllAhClassCodes(AhClassCodeCriteria criteria) {
        List<AhClassCode> classCodeList = baseMapper.selectList(new QueryWrapper<AhClassCode>()
                .eq(StrUtil.isNotBlank(criteria.getProjectIdEq()), AhClassCode._ahProjectId, criteria.getProjectIdEq().trim()));
        if (classCodeList == null || classCodeList.size() == 0) {
            return new ReturnCommonDTO<>(new ArrayList<>());
        }
        List<AhClassCodeDTO> classCodeDTOList = classCodeList.stream().map(classCode -> {
            AhClassCodeDTO classCodeDTO = new AhClassCodeDTO();
            MyBeanUtil.copyNonNullProperties(classCode, classCodeDTO);
            return classCodeDTO;
        }).collect(Collectors.toList());
        return new ReturnCommonDTO<>(classCodeDTOList);
    }

    @Override
    public ReturnCommonDTO uploadClassFiles(String projectId, MultipartFile[] files) {
        Set<Class> classSet = new HashSet<>();
        for (MultipartFile file : files) {
            // 获取文件名和文件内容
            ReturnFileUploadDTO fileUploadDTO = uploadFileToLocal(file);
            String fileName = fileUploadDTO.getOriginalFileName();
            FileReader fileReader = new FileReader(fileUploadDTO.getAbsolutePath());
            // 文件的字节码Map（一个文件可能有多个类）
            Map<String, byte[]> bytecode = null;
            if (fileName.endsWith(".java")) {
                // java文件
                String javaSrc = fileReader.readString();
                bytecode = DynamicLoader.compile(javaSrc);
                for (String keyName : bytecode.keySet()) {
                    try {
                        Class<?> clazz = new MemoryClassLoader(bytecode).loadClass(keyName);
                        classSet.add(clazz);
                    } catch (Exception e) {
                        throw new CommonException("类加载异常", e);
                    }
                }
            } else if (fileName.endsWith(".class")) {
                // class文件
                try {
                    byte[] classBytes = fileReader.readBytes();
                    Class<?> clazz = new MyClassLoader(classBytes).loadClass("");
                    classSet.add(clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CommonException("文件解析错误");
                }
            } else {
                throw new CommonException("文件类别错误");
            }
        }
        for (Class<?> clazz : classSet) {
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                System.out.println(field.getName());
            }
            System.out.println("=================================================");
        }
        return new ReturnCommonDTO();
    }

    private ReturnFileUploadDTO uploadFileToLocal(MultipartFile file) {
        Date nowDate = new Date();
        // 获取文件名和文件内容
        String fileName = file.getOriginalFilename();
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
}