package com.bob.at.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.config.YmlConfig;
import com.bob.at.domain.AhField;
import com.bob.at.dto.AhFieldDTO;
import com.bob.at.dto.criteria.AhFieldCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhFieldMapper;
import com.bob.at.service.AhFieldService;
import com.bob.at.util.MyBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可用实体类
 * @author Bob
 */
@Service
public class AhFieldServiceImpl extends ServiceImpl<AhFieldMapper, AhField>
        implements AhFieldService {

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO createAhField(AhFieldDTO ahFieldDTO) {
        AhField ahField = new AhField();
        MyBeanUtil.copyNonNullProperties(ahFieldDTO, ahField);
        baseMapper.insert(ahField);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO updateAhField(AhFieldDTO ahFieldDTO) {
        AhField ahField = new AhField();
        MyBeanUtil.copyNonNullProperties(ahFieldDTO, ahField);
        baseMapper.updateById(ahField);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhField(String id) {
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhFields(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<AhFieldDTO> getAhField(String id) {
        AhField inter = baseMapper.selectById(id);
        AhFieldDTO fieldDTO = new AhFieldDTO();
        MyBeanUtil.copyNonNullProperties(inter, fieldDTO);
        return new ReturnCommonDTO<>(fieldDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<List<AhFieldDTO>> getAllAhFields(AhFieldCriteria criteria) {
        List<AhField> fieldList = baseMapper.selectList(new QueryWrapper<AhField>()
                .eq(StrUtil.isNotBlank(criteria.getClassCodeIdEq()), AhField._ahClassCodeId, criteria.getClassCodeIdEq().trim()));
        if (fieldList == null || fieldList.size() == 0) {
            return new ReturnCommonDTO<>(new ArrayList<>());
        }
        List<AhFieldDTO> fieldDTOList = fieldList.stream().map(field -> {
            AhFieldDTO fieldDTO = new AhFieldDTO();
            MyBeanUtil.copyNonNullProperties(field, fieldDTO);
            return fieldDTO;
        }).collect(Collectors.toList());
        return new ReturnCommonDTO<>(fieldDTOList);
    }
}