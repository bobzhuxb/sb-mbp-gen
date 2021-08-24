package com.bob.at.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhInterface;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhInterfaceMapper;
import com.bob.at.service.AhInterfaceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 接口
 * @author Bob
 */
@Service
public class AhInterfaceServiceImpl extends ServiceImpl<AhInterfaceMapper, AhInterface>
        implements AhInterfaceService {

    @Override
    public ReturnCommonDTO createAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO updateAhInterface(AhInterfaceDTO ahInterfaceDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhInterface(String id) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhInterfaces(List<String> idList) {
        return null;
    }

    @Override
    public ReturnCommonDTO<AhInterfaceDTO> getAhInterface(String id) {
        return null;
    }

    @Override
    public ReturnCommonDTO<List<AhInterfaceDTO>> getAllAhInterfaces(AhInterfaceCriteria criteria) {
        return null;
    }
}