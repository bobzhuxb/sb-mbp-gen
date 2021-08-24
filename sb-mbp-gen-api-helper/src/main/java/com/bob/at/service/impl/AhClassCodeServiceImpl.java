package com.bob.at.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhClassCode;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.service.AhClassCodeService;
import com.bob.at.util.MyBeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可用实体类
 * @author Bob
 */
@Service
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
}