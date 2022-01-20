package com.bob.at.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhProjectDTO;
import com.bob.at.dto.criteria.AhProjectCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.YapiSendDTO;

import java.util.List;

/**
 * 项目
 * @author Bob
 */
public interface AhProjectService extends IService<AhProject> {

    ReturnCommonDTO createAhProject(AhProjectDTO ahProjectDTO);

    ReturnCommonDTO updateAhProject(AhProjectDTO ahProjectDTO);

    ReturnCommonDTO deleteAhProject(String id);

    ReturnCommonDTO deleteAhProjects(List<String> idList);

    ReturnCommonDTO<AhProjectDTO> getAhProject(String id);

    ReturnCommonDTO<List<AhProjectDTO>> getAllAhProjects(AhProjectCriteria criteria);

    ReturnCommonDTO sendInterToYapi(YapiSendDTO yapiSendDTO);

}
