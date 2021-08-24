package com.bob.at.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhInterface;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhInterfaceMapper;
import com.bob.at.service.AhInterfaceService;
import com.bob.at.util.MyBeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口
 * @author Bob
 */
@Service
public class AhInterfaceServiceImpl extends ServiceImpl<AhInterfaceMapper, AhInterface>
        implements AhInterfaceService {

    @Override
    public ReturnCommonDTO createAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        AhInterface ahInterface = new AhInterface();
        MyBeanUtil.copyNonNullProperties(ahInterfaceDTO, ahInterface);
        baseMapper.insert(ahInterface);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO updateAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        AhInterface ahInterface = new AhInterface();
        MyBeanUtil.copyNonNullProperties(ahInterfaceDTO, ahInterface);
        baseMapper.updateById(ahInterface);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO deleteAhInterface(String id) {
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO deleteAhInterfaces(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    public ReturnCommonDTO<AhInterfaceDTO> getAhInterface(String id) {
        AhInterface inter = baseMapper.selectById(id);
        AhInterfaceDTO interfaceDTO = new AhInterfaceDTO();
        MyBeanUtil.copyNonNullProperties(inter, interfaceDTO);
        return new ReturnCommonDTO<>(interfaceDTO);
    }

    @Override
    public ReturnCommonDTO<List<AhInterfaceDTO>> getAllAhInterfaces(AhInterfaceCriteria criteria) {
        List<AhInterface> interfaceList = baseMapper.selectList(new QueryWrapper<AhInterface>()
                .eq(StrUtil.isNotBlank(criteria.getProjectIdEq()), AhInterface._ahProjectId, criteria.getProjectIdEq().trim()));
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
}