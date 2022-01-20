package com.bob.at.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.at.config.Constants;
import com.bob.at.config.YmlConfig;
import com.bob.at.domain.AhClassCode;
import com.bob.at.domain.AhInterface;
import com.bob.at.domain.AhProject;
import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.AhProjectDTO;
import com.bob.at.dto.adapter.ApiAdapterConfigDTO;
import com.bob.at.dto.criteria.AhProjectCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.dto.help.YapiSendDTO;
import com.bob.at.mapper.AhClassCodeMapper;
import com.bob.at.mapper.AhFieldMapper;
import com.bob.at.mapper.AhInterfaceMapper;
import com.bob.at.mapper.AhProjectMapper;
import com.bob.at.service.AhProjectService;
import com.bob.at.util.CommandUtil;
import com.bob.at.util.MyBeanUtil;
import com.bob.at.web.rest.errors.CommonAlertException;
import com.bob.at.web.rest.errors.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO createAhProject(AhProjectDTO ahProjectDTO) {
        AhProject projectExist = baseMapper.selectOne(new QueryWrapper<AhProject>()
                .eq(AhProject._name, ahProjectDTO.getName()).last("limit 1"));
        if (projectExist != null) {
            throw new CommonAlertException("该工程已存在");
        }
        AhProject ahProject = new AhProject();
        MyBeanUtil.copyNonNullProperties(ahProjectDTO, ahProject);
        baseMapper.insert(ahProject);
        return new ReturnCommonDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCommonDTO updateAhProject(AhProjectDTO ahProjectDTO) {
        AhProject projectExist = baseMapper.selectOne(new QueryWrapper<AhProject>()
                .eq(AhProject._name, ahProjectDTO.getName()).last("limit 1"));
        if (projectExist != null && !projectExist.getId().equals(ahProjectDTO.getId())) {
            throw new CommonAlertException("该工程已存在");
        }
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
                .like(StrUtil.isNotBlank(criteria.getNameLike()), AhProject._name, criteria.getNameLike())
                .orderByAsc(AhProject._name));
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

    @Override
    public ReturnCommonDTO sendInterToYapi(YapiSendDTO yapiSendDTO) {
        AhProject project = baseMapper.selectById(yapiSendDTO.getAhProjectId());
        String yapiToken = project.getYapiToken();
        String yapiUrl = project.getYapiUrl();
        if (StrUtil.isBlank(yapiToken)) {
            throw new CommonAlertException("工程没有设置yapiToken");
        }
        if (StrUtil.isBlank(yapiUrl)) {
            throw new CommonAlertException("工程没有设置yapiUrl");
        }
        Date nowDate = new Date();
        // 获取接口数据，并生成JSON文件名
        String sendFileName = "yapi.json";
        String bootFileName = "yapi-import.json";
        // 相对路径
        String relativePath = Constants.YAPI_JSON_FILE_UPLOAD_RELATIVE_PATH + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(nowDate);
        // 本地（服务器）绝对路径
        String localPath = ymlConfig.getLocation() + File.separator + relativePath;
        String localSendFileName = localPath + File.separator + sendFileName;
        String localBootFileName = localPath + File.separator + bootFileName;
        // 创建启动的json文件脚本
        String bootContent = "{\"type\":\"swagger\",\"token\":\"" + yapiToken + "\",\"file\":\"" + sendFileName
                + "\",\"merge\":\"good\",\"server\":\"" + yapiUrl + "\"}";
        // 写入启动json和要传输的json文件
        FileUtil.writeString(bootContent, localBootFileName, "UTF-8");
        FileUtil.writeString(yapiSendDTO.getDataJson(), localSendFileName, "UTF-8");
        // 执行
        try {
            CommandUtil.exeCmd("cd " + localPath + " && yapi import");
        } catch (Exception e) {
            throw new CommonException("命令执行失败", e);
        }
        return new ReturnCommonDTO();
    }
}