package com.bob.at.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.domain.AhClassCode;
import com.bob.at.domain.AhInterface;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.AhProjectDTO;
import com.bob.at.dto.criteria.AhProjectCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.mapper.AhFieldMapper;
import com.bob.at.mapper.AhInterfaceMapper;
import com.bob.at.mapper.AhProjectMapper;
import com.bob.at.service.AhProjectService;
import com.bob.at.util.MyBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 项目
 * @author Bob
 */
@Service
public class AhProjectServiceImpl extends ServiceImpl<AhProjectMapper, AhProject>
        implements AhProjectService {

    @Autowired
    private AhClassCodeMapper ahClassCodeMapper;

    @Autowired
    private AhFieldMapper ahFieldMapper;

    @Autowired
    private AhInterfaceMapper ahInterfaceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO createAhProject(AhProjectDTO ahProjectDTO) {
        AhProject ahProject = new AhProject();
        MyBeanUtil.copyNonNullProperties(ahProjectDTO, ahProject);
        baseMapper.insert(ahProject);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO updateAhProject(AhProjectDTO ahProjectDTO) {
        AhProject ahProject = new AhProject();
        MyBeanUtil.copyNonNullProperties(ahProjectDTO, ahProject);
        baseMapper.updateById(ahProject);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhProject(String id) {
        ahFieldMapper.deleteByProjectId(id);
        ahClassCodeMapper.delete(new QueryWrapper<AhClassCode>().eq(AhClassCode._ahProjectId, id));
        ahInterfaceMapper.delete(new QueryWrapper<AhInterface>().eq(AhInterface._ahProjectId, id));
        baseMapper.deleteById(id);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO deleteAhProjects(List<String> idList) {
        for (String id : idList) {
            ahFieldMapper.deleteByProjectId(id);
        }
        ahClassCodeMapper.delete(new QueryWrapper<AhClassCode>().in(AhClassCode._ahProjectId, idList));
        ahInterfaceMapper.delete(new QueryWrapper<AhInterface>().in(AhInterface._ahProjectId, idList));
        baseMapper.deleteBatchIds(idList);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<AhProjectDTO> getAhProject(String id) {
        AhProject project = baseMapper.selectById(id);
        AhProjectDTO projectDTO = new AhProjectDTO();
        MyBeanUtil.copyNonNullProperties(project, projectDTO);
        getAssociation(projectDTO);
        return new ReturnCommonDTO<>(projectDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO<List<AhProjectDTO>> getAllAhProjects(AhProjectCriteria criteria) {
        List<AhProject> projectList = baseMapper.selectList(new QueryWrapper<AhProject>()
            .like(StrUtil.isNotBlank(criteria.getNameLike()), AhProject._name, criteria.getNameLike().trim()));
        if (projectList == null || projectList.size() == 0) {
            return new ReturnCommonDTO<>(new ArrayList<>());
        }
        List<AhProjectDTO> projectDTOList = projectList.stream().map(project -> {
            AhProjectDTO projectDTO = new AhProjectDTO();
            MyBeanUtil.copyNonNullProperties(project, projectDTO);
            getAssociation(projectDTO);
            return projectDTO;
        }).collect(Collectors.toList());
        return new ReturnCommonDTO<>(projectDTOList);
    }

    private void getAssociation(AhProjectDTO projectDTO) {
        List<AhClassCodeDTO> ahClassCodeList = Optional.ofNullable(
                ahClassCodeMapper.selectList(new QueryWrapper<AhClassCode>().eq(AhClassCode._ahProjectId, projectDTO.getId())))
                .orElse(new ArrayList<>()).stream().map(ahClassCode -> {
                    AhClassCodeDTO ahClassCodeDTO = new AhClassCodeDTO();
                    MyBeanUtil.copyNonNullProperties(ahClassCode, ahClassCodeDTO);
                    return ahClassCodeDTO;
                }).collect(Collectors.toList());
        List<AhInterfaceDTO> ahInterfaceList = Optional.ofNullable(
                ahInterfaceMapper.selectList(new QueryWrapper<AhInterface>().eq(AhInterface._ahProjectId, projectDTO.getId())))
                .orElse(new ArrayList<>()).stream().map(ahInterface -> {
                    AhInterfaceDTO ahInterfaceDTO = new AhInterfaceDTO();
                    MyBeanUtil.copyNonNullProperties(ahInterface, ahInterfaceDTO);
                    return ahInterfaceDTO;
                }).collect(Collectors.toList());
        projectDTO.setAhClassCodeList(ahClassCodeList);
        projectDTO.setAhInterfaceList(ahInterfaceList);
    }
}