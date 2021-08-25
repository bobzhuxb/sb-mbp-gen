package com.bob.at.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.at.domain.AhField;
import com.bob.at.dto.AhFieldDTO;
import com.bob.at.dto.criteria.AhFieldCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;

import java.util.List;

/**
 * 接口
 * @author Bob
 */
public interface AhFieldService extends IService<AhField> {

    ReturnCommonDTO createAhField(AhFieldDTO ahFieldDTO);

    ReturnCommonDTO updateAhField(AhFieldDTO ahFieldDTO);

    ReturnCommonDTO deleteAhField(String id);

    ReturnCommonDTO deleteAhFields(List<String> idList);

    ReturnCommonDTO<AhFieldDTO> getAhField(String id);

    ReturnCommonDTO<List<AhFieldDTO>> getAllAhFields(AhFieldCriteria criteria);

}
