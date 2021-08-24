package com.bob.at.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhClassCode;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.service.AhClassCodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 可用实体类
 * @author Bob
 */
@Service
public class AhClassCodeServiceImpl extends ServiceImpl<AhClassCodeMapper, AhClassCode>
        implements AhClassCodeService {

    @Override
    public ReturnCommonDTO createAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO updateAhClassCode(AhClassCodeDTO ahClassCodeDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhClassCode(String id) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhClassCodes(List<String> idList) {
        return null;
    }

    @Override
    public ReturnCommonDTO<AhClassCodeDTO> getAhClassCode(String id) {
        return null;
    }

    @Override
    public ReturnCommonDTO<List<AhClassCodeDTO>> getAllAhClassCodes(AhClassCodeCriteria criteria) {
        return null;
    }
}