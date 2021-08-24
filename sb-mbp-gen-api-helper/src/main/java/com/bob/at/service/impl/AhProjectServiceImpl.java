package com.bob.at.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhProjectDTO;
import com.bob.at.dto.criteria.AhProjectCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhProjectMapper;
import com.bob.at.service.AhProjectService;
import com.bob.at.util.MyBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目
 * @author Bob
 */
@Service
public class AhProjectServiceImpl extends ServiceImpl<AhProjectMapper, AhProject>
        implements AhProjectService {

    @Override
    public ReturnCommonDTO createAhProject(AhProjectDTO ahProjectDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO updateAhProject(AhProjectDTO ahProjectDTO) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhProject(String id) {
        return null;
    }

    @Override
    public ReturnCommonDTO deleteAhProjects(List<String> idList) {
        return null;
    }

    @Override
    public ReturnCommonDTO<AhProjectDTO> getAhProject(String id) {
        AhProject project = baseMapper.selectOne(new QueryWrapper<AhProject>().last("limit 1"));
        AhProjectDTO projectDTO = new AhProjectDTO();
        MyBeanUtil.copyNonNullProperties(project, projectDTO);
        return new ReturnCommonDTO<>(projectDTO);
    }

    @Override
    public ReturnCommonDTO<List<AhProjectDTO>> getAllAhProjects(AhProjectCriteria criteria) {
        return null;
    }
}