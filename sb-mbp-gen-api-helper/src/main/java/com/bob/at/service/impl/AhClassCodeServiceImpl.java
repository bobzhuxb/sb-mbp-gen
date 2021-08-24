package com.bob.at.service.impl;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhClassCode;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.service.AhClassCodeService;
import com.bob.at.util.DynamicLoader;
import com.bob.at.util.MemoryClassLoader;
import com.bob.at.util.MyBeanUtil;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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
        Map<String, Class> classMap = new HashMap<>();
        for (MultipartFile file : files) {
            // 获取文件名和文件内容
            String fileName = file.getOriginalFilename();
            FileReader fileReader = new FileReader(file.getName());
            // 文件的字节码Map（一个文件可能有多个类）
            Map<String, byte[]> bytecode = null;
            // java文件先编译
            if (fileName.endsWith(".java")) {
                String javaSrc = fileReader.readString();
                bytecode = DynamicLoader.compile(fileName, javaSrc);
            } else if (fileName.endsWith(".class")) {
                // TODO
            } else {
                throw new CommonException("文件类别错误");
            }
            for (String keyName : bytecode.keySet()) {
                try {
                    Class<?> clazz = new MemoryClassLoader(bytecode).loadClass(keyName);
                    classMap.put(keyName, clazz);
                } catch (Exception e) {
                    throw new CommonException("类加载异常", e);
                }
            }
        }
        for (Map.Entry<String, Class> classEntry : classMap.entrySet()) {
            Class<?> clazz = classEntry.getValue();
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                System.out.println(field.getName());
            }
            System.out.println("=================================================");
        }
        return new ReturnCommonDTO();
    }
}