package com.bob.at.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.at.domain.AhInterface;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;

import java.util.List;

/**
 * 接口
 * @author Bob
 */
public interface AhInterfaceService extends IService<AhInterface> {

    ReturnCommonDTO createAhInterface(AhInterfaceDTO ahInterfaceDTO);

    ReturnCommonDTO updateAhInterface(AhInterfaceDTO ahInterfaceDTO);

    ReturnCommonDTO deleteAhInterface(String id);

    ReturnCommonDTO deleteAhInterfaces(List<String> idList);

    ReturnCommonDTO<AhInterfaceDTO> getAhInterface(String id);

    ReturnCommonDTO<List<AhInterfaceDTO>> getAllAhInterfaces(AhInterfaceCriteria criteria);

}
