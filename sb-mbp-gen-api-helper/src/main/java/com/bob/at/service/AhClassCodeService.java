package com.bob.at.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.at.domain.AhClassCode;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 可用实体类
 * @author Bob
 */
public interface AhClassCodeService extends IService<AhClassCode> {

    ReturnCommonDTO createAhClassCode(AhClassCodeDTO ahClassCodeDTO);

    ReturnCommonDTO updateAhClassCode(AhClassCodeDTO ahClassCodeDTO);

    ReturnCommonDTO deleteAhClassCode(String id);

    ReturnCommonDTO deleteAhClassCodes(List<String> idList);

    ReturnCommonDTO<AhClassCodeDTO> getAhClassCode(String id);

    ReturnCommonDTO<List<AhClassCodeDTO>> getAllAhClassCodes(AhClassCodeCriteria criteria);

    ReturnCommonDTO uploadClassFiles(String projectId, MultipartFile[] files);

}
