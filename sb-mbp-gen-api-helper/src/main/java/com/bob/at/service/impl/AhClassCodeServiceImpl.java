package com.bob.at.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.config.YmlConfig;
import com.bob.at.domain.AhClassCode;
import com.bob.at.domain.AhField;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.AhFieldDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.ReturnFileUploadDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.mapper.AhFieldMapper;
import com.bob.at.service.AhClassCodeService;
import com.bob.at.util.DynamicLoader;
import com.bob.at.util.MemoryClassLoader;
import com.bob.at.util.MyBeanUtil;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 可用实体类
 * @author Bob
 */
@Service
public class AhClassCodeServiceImpl extends ServiceImpl<AhClassCodeMapper, AhClassCode>
        implements AhClassCodeService {

    private static final Pattern LIST_TYPE_PATTERN = Pattern.compile("^java\\.util\\.List\\<(.*)\\>$");

    @Autowired
    private AhFieldMapper ahFieldMapper;

    @Autowired
    private AhClassCodeService ahClassCodeService;

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO createAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        AhClassCode ahClassCode = new AhClassCode();
        MyBeanUtil.copyNonNullProperties(ahClassCodeDTO, ahClassCode);
        baseMapper.insert(ahClassCode);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO updateAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        AhClassCode ahClassCode = new AhClassCode();
        MyBeanUtil.copyNonNullProperties(ahClassCodeDTO, ahClassCode);
        baseMapper.updateById(ahClassCode);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhClassCode(String id) {
        ahFieldMapper.delete(new QueryWrapper<AhField>().eq(AhField._ahClassCodeId, id));
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhClassCodes(List<String> idList) {
        ahFieldMapper.delete(new QueryWrapper<AhField>().in(AhField._ahClassCodeId, idList));
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhClassCodesOfProject(String projectId) {
        ahFieldMapper.deleteByProjectId(projectId);
        baseMapper.delete(new QueryWrapper<AhClassCode>().eq(AhClassCode._ahProjectId, projectId));
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<AhClassCodeDTO> getAhClassCode(String id) {
        AhClassCode inter = baseMapper.selectById(id);
        AhClassCodeDTO classCodeDTO = new AhClassCodeDTO();
        MyBeanUtil.copyNonNullProperties(inter, classCodeDTO);
        getAssociation(classCodeDTO);
        return new ReturnCommonDTO<>(classCodeDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    private void getAssociation(AhClassCodeDTO classCodeDTO) {
        List<AhFieldDTO> ahFieldList = Optional.ofNullable(
                ahFieldMapper.selectList(new QueryWrapper<AhField>().eq(AhField._ahClassCodeId, classCodeDTO.getId())))
                .orElse(new ArrayList<>()).stream().map(ahField -> {
                    AhFieldDTO ahFieldDTO = new AhFieldDTO();
                    MyBeanUtil.copyNonNullProperties(ahField, ahFieldDTO);
                    return ahFieldDTO;
                }).collect(Collectors.toList());
        classCodeDTO.setAhFieldList(ahFieldList);
    }

    @Override
    public ReturnCommonDTO uploadClassFiles(String projectId, String fileType, String overwrite, MultipartFile[] files) {
        Set<Class> classSet = new HashSet<>();
        List<String> fullFileNameList = new ArrayList<>();
        for (MultipartFile file : files) {
            // 获取文件名和文件内容
            ReturnFileUploadDTO fileUploadDTO = uploadFileToLocal(file);
            String fullFileName = fileUploadDTO.getAbsolutePath();
            if (fullFileName.endsWith(fileType)) {
                fullFileNameList.add(fileUploadDTO.getAbsolutePath());
            }
        }
        if (".java".equals(fileType)) {
            try {
                Map<String, byte[]> byteCodeMap = DynamicLoader.compile(fullFileNameList);
                for (String keyName : byteCodeMap.keySet()) {
                    Class<?> clazz = new MemoryClassLoader(byteCodeMap).loadClass(keyName);
                    classSet.add(clazz);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonException("java文件动态编译加载异常", e);
            }
        } else if (".class".equals(fileType)) {
            // class文件
            throw new CommonException("暂不支持");
//            try {
//                for (String fullFileName : fullFileNameList) {
//                    FileReader fileReader = new FileReader(fullFileName);
//                    byte[] classBytes = fileReader.readBytes();
//                    Class<?> clazz = new MyClassLoader(classBytes).loadClass("");
//                    classSet.add(clazz);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new CommonException("类动态加载异常");
//            }
        } else {
            throw new CommonException("fileType类别错误");
        }
        ahClassCodeService.reloadClassCodeAndField(projectId, overwrite, classSet);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reloadClassCodeAndField(String projectId, String overwrite, Set<Class> classSet) {
        // 获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        String nowTimeStr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(nowTime);
        // 验证
        if ("yes".equals(overwrite)) {
            // 全覆盖，则清空该项目下的所有类
            ahClassCodeService.deleteAhClassCodesOfProject(projectId);
        }
        for (Class<?> clazz : classSet) {
            String packageName = ClassUtil.getPackage(clazz);
            String className = ClassUtil.getClassName(clazz, true);
            // 已存在的classCodeId
            String classCodeIdExist = null;
            if (!"yes".equals(overwrite)) {
                // 非全覆盖，要验证是新增还是修改
                AhClassCode classCodeExist = baseMapper.selectOne(new QueryWrapper<AhClassCode>()
                        .eq(AhClassCode._ahProjectId, projectId)
                        .eq(AhClassCode._packageName, packageName)
                        .eq(AhClassCode._className, className));
                if (classCodeExist != null) {
                    classCodeIdExist = classCodeExist.getId();
                }
            }
            // 后续级联用的classCodeId
            String classCodeIdRelate = null;
            // AhClassCode表
            if (classCodeIdExist == null) {
                AhClassCode ahClassCodeAdd = new AhClassCode();
                ahClassCodeAdd.setPackageName(packageName);
                ahClassCodeAdd.setClassName(className);
                ahClassCodeAdd.setAhProjectId(projectId);
                ahClassCodeAdd.setInsertTime(nowTimeStr);
                baseMapper.insert(ahClassCodeAdd);
                classCodeIdRelate = ahClassCodeAdd.getId();
            } else {
                AhClassCode ahClassCodeUpdate = new AhClassCode();
                ahClassCodeUpdate.setId(classCodeIdExist);
                ahClassCodeUpdate.setUpdateTime(nowTimeStr);
                baseMapper.updateById(ahClassCodeUpdate);
                classCodeIdRelate = classCodeIdExist;
            }
            // AhField表
            if (classCodeIdExist != null) {
                ahFieldMapper.delete(new QueryWrapper<AhField>().eq(AhField._ahClassCodeId, classCodeIdExist));
            }
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                String fieldFullTypeName = ClassUtil.getClassName(field.getType(), false);
                String fieldName = field.getName();
                String genericTypeName = null;
                if ("java.util.List".equals(fieldFullTypeName)) {
                    // 继续获取泛型名
                    String genericTypeStr = field.getGenericType().getTypeName();
                    Matcher matcher = LIST_TYPE_PATTERN.matcher(genericTypeStr);
                    if (matcher.find()) {
                        genericTypeName = matcher.group(1);
                    }
                }
                // 新增Field
                AhField ahFieldAdd = new AhField();
                ahFieldAdd.setTypeName(fieldFullTypeName);
                ahFieldAdd.setFieldName(fieldName);
                ahFieldAdd.setGenericTypeName(genericTypeName);
                ahFieldAdd.setAhClassCodeId(classCodeIdRelate);
                ahFieldAdd.setInsertTime(nowTimeStr);
                ahFieldMapper.insert(ahFieldAdd);
            }
        }

    }

    private ReturnFileUploadDTO uploadFileToLocal(MultipartFile file) {
        Date nowDate = new Date();
        // 获取文件名和文件内容
        String fileName = file.getOriginalFilename();
        // 相对路径
        String relativePath = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate);
        // 新文件名
        String newFileName = fileName;
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