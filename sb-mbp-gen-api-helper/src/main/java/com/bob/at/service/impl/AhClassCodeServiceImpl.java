package com.bob.at.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhClassCode;
import com.bob.at.domain.AhField;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.AhFieldDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.ReturnFileUploadDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.mapper.AhFieldMapper;
import com.bob.at.mapper.AhProjectMapper;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private AhProjectMapper ahProjectMapper;

    @Autowired
    private AhClassCodeService ahClassCodeService;

    @Autowired
    private CommonService commonService;

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
        AhClassCode classCode = baseMapper.selectById(id);
        AhClassCodeDTO classCodeDTO = new AhClassCodeDTO();
        MyBeanUtil.copyNonNullProperties(classCode, classCodeDTO);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<AhClassCodeDTO> getAhClassCodeOne(AhClassCodeCriteria criteria) {
        String fullNameEq = criteria.getFullNameEq();
        String projectIdEq = criteria.getProjectIdEq();
        if (StrUtil.isNotBlank(fullNameEq) && StrUtil.isNotBlank(projectIdEq)) {
            String packageName = fullNameEq.substring(0, fullNameEq.lastIndexOf("."));
            String className = fullNameEq.substring(fullNameEq.lastIndexOf(".") + 1);
            AhClassCode classCode = baseMapper.selectOne(new QueryWrapper<AhClassCode>()
                    .eq(AhClassCode._packageName, packageName)
                    .eq(AhClassCode._className, className)
                    .eq(AhClassCode._ahProjectId, projectIdEq)
                    .last("limit 1"));
            if (classCode != null) {
                AhClassCodeDTO classCodeDTO = new AhClassCodeDTO();
                MyBeanUtil.copyNonNullProperties(classCode, classCodeDTO);
                getAssociation(classCodeDTO);
                return new ReturnCommonDTO<>(classCodeDTO);
            }
        }
        return new ReturnCommonDTO<>();
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
    public ReturnCommonDTO uploadClassFiles(String projectId, String fileType, String overwrite, String exceptClassNames,  MultipartFile[] files) {
        // 更新exceptClassNames
        AhProject ahProjectUpdate = new AhProject();
        ahProjectUpdate.setId(projectId);
        ahProjectUpdate.setExceptClassNames(exceptClassNames);
        ahProjectMapper.updateById(ahProjectUpdate);
        // 解析手动排除的类
        List<String> exceptClassNameList = new ArrayList<>();
        if (StrUtil.isNotBlank(exceptClassNames)) {
            exceptClassNameList = Arrays.asList(exceptClassNames.split("\r\n"));
        }
        // 预置的Java文件
        List<String> reservedFileNameList = Arrays.asList("IPage.java", "GenComment.java", "MbpPage.java");
        // 获取basePackage信息
        AhProject project = ahProjectMapper.selectById(projectId);
        String basePackage = project.getBasePackage();
        // 上传文件
        Set<Class> classSet = new HashSet<>();
        List<String> fullFileNameList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        Date nowDate = new Date();
        // 特殊类固定生成
        String fullFilePath = commonService.getUploadFullFilePath(nowDate);
        // IPage.java
        String iPageFullFileName = fullFilePath + "IPage.java";
        FileUtil.writeLines(genJavaSrcIPage(basePackage), iPageFullFileName, "UTF-8");
        fullFileNameList.add(iPageFullFileName);
        fileNameList.add("IPage.java");
        // MbpPage.java
        String mbpPageFullFileName = fullFilePath + "MbpPage.java";
        FileUtil.writeLines(genJavaSrcMbpPage(basePackage), mbpPageFullFileName, "UTF-8");
        fullFileNameList.add(mbpPageFullFileName);
        fileNameList.add("MbpPage.java");
        // GenComment.java
        String genCommentFullFileName = fullFilePath + "GenComment.java";
        FileUtil.writeLines(genJavaSrcGenComment(basePackage), genCommentFullFileName, "UTF-8");
        fullFileNameList.add(genCommentFullFileName);
        fileNameList.add("GenComment.java");
        // 其他Java文件的导入
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            // 预置的文件，忽略从外部导入的文件
            String fileNameTmp = file.getOriginalFilename();
            if (fileNameTmp.contains("/")) {
                fileNameTmp = fileNameTmp.substring(fileNameTmp.lastIndexOf("/") + 1);
            }
            if (reservedFileNameList.contains(fileNameTmp)) {
                continue;
            }
            // 获取文件名和文件信息
            ReturnFileUploadDTO fileUploadDTO = commonService.uploadFileToLocal(file, false, nowDate);
            String fullFileName = fileUploadDTO.getAbsolutePath();
            String fileName = fileUploadDTO.getOriginalFileName();
            if (!fullFileName.endsWith(fileType)) {
                // 过滤掉后缀不对的文件
                continue;
            }
            // 其他类添加
            if (fullFileName.endsWith(fileType)) {
                fullFileNameList.add(fullFileName);
                fileNameList.add(fileName);
            }
        }
        if (".java".equals(fileType)) {
            List<String> fullFileNameListNew = new ArrayList<>();
            // 过滤掉所有不必要的行
            for (String fullFileName : fullFileNameList) {
                List<String> lines = FileUtil.readLines(fullFileName, "UTF-8");
                String fileName = fullFileName.substring(fullFileName.lastIndexOf(File.separator) + 1);
                // 预置的类不处理
                if (reservedFileNameList.contains(fileName)) {
                    fullFileNameListNew.add(fullFileName);
                    continue;
                }
                // 是否手动排除的类
                boolean isExceptFile = false;
                List<String> newLines = new ArrayList<>();
                for (String line : lines) {
                    line = line.trim();
                    // 该行是否留下
                    boolean needLine = false;
                    // package行留下
                    if (line.startsWith("package ")) {
                        // 过滤掉手动排除的类
                        String nowPackageName = line.substring(8, line.length() - 1);
                        String nowClassName = fileName.substring(0, fileName.indexOf("."));
                        String nowFullClassName = nowPackageName + "." + nowClassName;
                        if (exceptClassNameList.contains(nowFullClassName)) {
                            isExceptFile = true;
                            break;
                        }
                        // package行留下
                        needLine = true;
                    }
                    // 成员变量留下
                    boolean isFieldLine = (line.startsWith("private ") || line.startsWith("public ")
                            || line.startsWith("protected ")) && !line.contains("(");
                    if (isFieldLine) {
                        needLine = true;
                    }
                    // import行的去留
                    if (line.startsWith("import ")) {
                        if (line.endsWith(".IPage;") || line.endsWith(".MbpPage;") || line.endsWith(".GenComment;")) {
                            needLine = false;
                        } else if (line.contains(".annotation.")) {
                            needLine = false;
                        } else if (line.contains(".Constants;")) {
                            needLine = false;
                        } else if (line.startsWith("import java.")) {
                            needLine = true;
                        } else if (line.startsWith("import " + basePackage + ".")) {
                            if (line.startsWith("import " + basePackage + ".domain.")
                                || line.startsWith("import " + basePackage + ".dto.")) {
                                needLine = true;
                            } else {
                                needLine = false;
                            }
                        }
                    }
                    // 唯一留下的特殊注解
                    if (line.startsWith("@GenComment(")) {
                        needLine = true;
                    }
                    // 需要追加的行写入
                    if (needLine) {
                        newLines.add(line);
                    }
                    // 所有的类先把IPage和GenComment加上
                    if (line.startsWith("package ")) {
                        if (!reservedFileNameList.contains(fileName)) {
                            newLines.add("import " + basePackage + ".IPage;");
                            newLines.add("import " + basePackage + ".MbpPage;");
                            newLines.add("import " + basePackage + ".GenComment;");
                        }
                    }
                }
                // 过滤掉手动排除的类
                if (!isExceptFile) {
                    fullFileNameListNew.add(fullFileName);
                }
                // 类结束的大括号
                newLines.add("}");
                // 重新写入文件
                FileUtil.writeLines(newLines, fullFileName, "UTF-8");
            }
            // 编译Java类
            try {
                Map<String, byte[]> byteCodeMap = DynamicLoader.compile(fullFileNameListNew);
                for (String keyName : byteCodeMap.keySet()) {
                    Class<?> clazz = new MemoryClassLoader(byteCodeMap).loadClass(keyName);
                    classSet.add(clazz);
                }
            } catch (Exception e) {
                throw new CommonException("java文件动态编译加载异常，请查看日志", e);
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
        ahClassCodeService.reloadClassCodeAndField(projectId, overwrite, basePackage, classSet);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reloadClassCodeAndField(String projectId, String overwrite, String basePackage, Set<Class> classSet) {
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
            List<Field> fieldList = MyBeanUtil.getAllFieldsOfClass(clazz);
            for (Field field : fieldList) {
                String fieldFullTypeName = ClassUtil.getClassName(field.getType(), false);
                String fieldName = field.getName();
                Annotation[] annotations = field.getAnnotations();
                String descr = null;
                if (annotations != null && annotations.length == 1) {
                    try {
                        Class<?> annotationClass = annotations[0].getClass();
                        Method valueMethod = annotationClass.getMethod("value");
                        Object descrObj = valueMethod.invoke(annotations[0]);
                        if (descrObj != null) {
                            descr = descrObj.toString().trim();
                        }
                    } catch (Exception e) {
                        throw new CommonException(e.getMessage(), e);
                    }
                }
                String genericTypeName = null;
                if ("java.util.List".equals(fieldFullTypeName)
                        || (basePackage + ".IPage").equals(fieldFullTypeName)
                        || (basePackage + ".MbpPage").equals(fieldFullTypeName)) {
                    String genericTypeStr = field.getGenericType().getTypeName();
                    Pattern pattern = Pattern.compile("^" + fieldFullTypeName.replace(".", "\\.") + "\\<(.*)\\>$");
                    Matcher matcher = pattern.matcher(genericTypeStr);
                    if (matcher.find()) {
                        genericTypeName = matcher.group(1);
                    }
                }
                // 新增Field
                AhField ahFieldAdd = new AhField();
                ahFieldAdd.setTypeName(fieldFullTypeName);
                ahFieldAdd.setFieldName(fieldName);
                ahFieldAdd.setGenericTypeName(genericTypeName);
                ahFieldAdd.setDescr(descr);
                ahFieldAdd.setAhClassCodeId(classCodeIdRelate);
                ahFieldAdd.setInsertTime(nowTimeStr);
                ahFieldMapper.insert(ahFieldAdd);
            }
        }
    }

    private List<String> genJavaSrcMbpPage(String basePackage) {
        return Arrays.asList("package " + basePackage + ";",
                "public class MbpPage<T> {",
                "}");
    }

    private List<String> genJavaSrcIPage(String basePackage) {
        return Arrays.asList("package " + basePackage + ";",
                "import java.util.List;",
                "public class IPage<T> {",
                "private long pages;",
                "private List<T> records;",
                "private long total;",
                "private long size;",
                "private long current;",
                "}\n");
    }

    private List<String> genJavaSrcGenComment(String basePackage) {
        return Arrays.asList("package " + basePackage + ";",
                "import java.lang.annotation.*;",
                "@Documented",
                "@Target({ElementType.FIELD})",
                "@Retention(RetentionPolicy.RUNTIME)",
                "public @interface GenComment {",
                "String value();",
                "}");
    }
}