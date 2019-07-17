package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.config.Constants;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.*;
import ${packageName}.mapper.*;
import ${packageName}.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * ${entityComment}
 */
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
@Transactional
public class ${eentityName}ServiceImpl extends ServiceImpl<${eentityName}Mapper, ${eentityName}>
        implements ${eentityName}Service {

    private final Logger log = LoggerFactory.getLogger(${eentityName}ServiceImpl.class);
	
	<#if eentityName == 'SystemPermission'>
    @Autowired
    private SystemRoleMapper systemRoleMapper;

    @Autowired
    private SystemRolePageElementMapper systemRolePageElementMapper;

    @Autowired
    private SystemUserService systemUserService;

    /**
     * Get page elements of authority by name
     * @param roleId id of role
     * @return result page elements
     */
    public List<SystemRolePageElement> getPageElementsByRole(long roleId) {
        List<SystemRolePageElement> rolePageElementList = systemRolePageElementMapper.selectByMap(
                new HashMap<String, Object>(){{
                    put("role_id", roleId);
                }});
        return rolePageElementList;
    }

    /**
     * 保存角色的页面和元素权限
     * @param rolePageElementsDTO
     */
    public void saveRolePermissions(RolePageElementsDTO rolePageElementsDTO) {
        if (rolePageElementsDTO.getRoleId() == null
                || rolePageElementsDTO.getPageElementCodeList() == null) {
            return;
        }
        SystemRole role = systemRoleMapper.selectById(rolePageElementsDTO.getRoleId());
        if (role == null) {
            role = new SystemRole();
        }
        role.setName(rolePageElementsDTO.getName());
        role.setChineseName(rolePageElementsDTO.getChineseName());
        role.setDescription(rolePageElementsDTO.getDescription());
        if (role == null) {
            // 如果角色不存在，同时新增角色
            systemRoleMapper.insert(role);
        } else {
            systemRoleMapper.updateById(role);
        }
        // 页面元素集合
        Set<String> elementIdentifySet = new HashSet<>();
        for (PageElementDTO pageElementDTO1 : Constants.pageElementList) {
            for (PageElementDTO pageElementDTO2 : pageElementDTO1.getChildList()) {
                if ("ELEMENT".equals(pageElementDTO2.getType())) {
                    elementIdentifySet.add(pageElementDTO2.getCode());
                } else {
                    for (PageElementDTO pageElementDTO3 : pageElementDTO2.getChildList()) {
                        if ("ELEMENT".equals(pageElementDTO3.getType())) {
                            elementIdentifySet.add(pageElementDTO3.getCode());
                        }
                    }
                }
            }
        }
        // 根据角色删除相关的页面权限
        systemRolePageElementMapper.deleteByMap(new HashMap<String, Object>() {{
            put("role_id", rolePageElementsDTO.getRoleId());
        }});
        // 重新添加角色的页面权限
        for (String pageElementCode : rolePageElementsDTO.getPageElementCodeList()) {
            if (!elementIdentifySet.contains(pageElementCode)) {
                // 只保存页面元素
                continue;
            }
            SystemRolePageElement systemRolePageElement = new SystemRolePageElement();
            systemRolePageElement.setRoleId(rolePageElementsDTO.getRoleId());
            systemRolePageElement.setPageElementCode(pageElementCode);
            systemRolePageElementMapper.insert(systemRolePageElement);
        }
    }

    /**
     * 生成角色的页面元素树
     * @param roleId id of role
     * @return
     */
    public List<PageElementDTO> getPageElementTree(Long roleId) {
        if (roleId == null || roleId == 0) {
            return Constants.pageElementList;
        }
        List<SystemRolePageElement> authorityPageElementList = getPageElementsByRole(roleId);
        // 角色拥有的页面元素列表
        List<String> hasPageElementList = new ArrayList<>();
        if (authorityPageElementList != null) {
            authorityPageElementList.forEach(
                    authorityPageElement -> hasPageElementList.add(authorityPageElement.getPageElementCode()));
        }
        List<PageElementDTO> pageElementList = new ArrayList<>();
        formPageElement(Constants.pageElementList, pageElementList, hasPageElementList);
        return pageElementList;
    }

    /**
     * 拷贝一份页面元素树状结构，并标记元素是否选中
     * @param originList 原页面元素树
     * @param destList 生成的页面元素树
     * @param hasPageElementList 已选中的元素
     * @return 0：未选  1：全选  2：部分选择
     */
    private int formPageElement(List<PageElementDTO> originList, List<PageElementDTO> destList,
                                List<String> hasPageElementList) {
        double childTotalCount = (double)originList.size();
        double selectedCount = 0.0;
        for (PageElementDTO pageElementDTO : originList) {
            String code = pageElementDTO.getCode();
            PageElementDTO pageElementDTONew = (PageElementDTO)pageElementDTO.clone();
            destList.add(pageElementDTONew);
            if ("ELEMENT".equals(pageElementDTO.getType())) {
                // 只有页面元素（例如按钮）才需要标注是否选中
                if (hasPageElementList.contains(code)) {
                    pageElementDTONew.setSelected(1);
                    selectedCount++;
                } else {
                    pageElementDTONew.setSelected(0);
                }
            }
            List<PageElementDTO> oriChildList = pageElementDTO.getChildList();
            if (oriChildList != null && oriChildList.size() > 0) {
                List<PageElementDTO> destChildList = new ArrayList<>();
                pageElementDTONew.setChildList(destChildList);
                // 递归拷贝
                int selected = formPageElement(pageElementDTO.getChildList(), destChildList, hasPageElementList);
                if (selected == 1) {
                    // 全选
                    selectedCount += 1.0;
                    pageElementDTONew.setSelected(1);
                } else if (selected == 2) {
                    // 部分选择
                    selectedCount += 0.5;
                    pageElementDTONew.setSelected(2);
                } else {
                    // 未选
                    pageElementDTONew.setSelected(0);
                }
            }
        }
        // 返回未选（0）、全选（1）、部分选择（2）
        return selectedCount - 0.0 <= 0.001 ? 0 : (Math.abs(childTotalCount - selectedCount) <= 0.001 ? 1 : 2);
    }

    /**
     * 获取当前用户的页面元素树
     * @return
     */
    public List<PageElementDTO> getPageElementOfCurrentUser() {
        // 用户当前的页面元素（第三层级）
        List<String> hasPageElementList = new ArrayList<>();
        // 获取用户当前的页面元素
        SecurityUtils.getCurrentUserLogin().ifPresent(login -> {
            SystemUserCriteria criteria = new SystemUserCriteria();
            StringFilter loginFilter = new StringFilter();
            loginFilter.setEquals(login);
            criteria.setLogin(loginFilter);
            criteria.setAssociationNameList(Arrays.asList("systemUserRoleList"));
            List<SystemUserDTO> systemUserList = systemUserService.baseFindAll("SystemUser", criteria, null).getData();
            if (systemUserList == null || systemUserList.size() == 0) {
                return;
            }
            SystemUserDTO systemUser = systemUserList.get(0);
            if (systemUser == null || systemUser.getSystemUserRoleList() == null
                    || systemUser.getSystemUserRoleList().size() == 0) {
                return;
            }
            for (SystemUserRoleDTO systemUserRoleDTO : systemUser.getSystemUserRoleList()) {
                List<SystemRolePageElement> authorityPageElementList = getPageElementsByRole(systemUserRoleDTO.getSystemRoleId());
                if (authorityPageElementList != null) {
                    authorityPageElementList.forEach(authorityPageElement -> {
                        String pageElementCode = authorityPageElement.getPageElementCode();
                        if (!hasPageElementList.contains(pageElementCode)) {
                            hasPageElementList.add(pageElementCode);
                        }
                    });
                }
            }
        });
        // 获取该用户的权限所使用到的页面
        List<PageElementDTO> pageElementList = new ArrayList<>();
        for (PageElementDTO pageElementDTO : Constants.pageElementList) {
            // 获取一级页面的子元素
            List<PageElementDTO> childList = pageElementDTO.getChildList();
            if (childList == null || childList.size() == 0) {
                continue;
            }
            if ("ELEMENT".equals(childList.get(0).getType())) {
                // 第二层是页面元素（例如按钮）
                List<PageElementDTO> destChildList = new ArrayList<>();
                for (PageElementDTO childElementDTO : childList) {
                    if (hasPageElementList.contains(childElementDTO.getCode())) {
                        // 当前用户有该页面元素（例如按钮）的权限
                        destChildList.add(childElementDTO);
                    }
                }
                if (destChildList.size() > 0) {
                    // 当前一级页面有子元素（按钮）的权限
                    PageElementDTO descPageElementDTO = (PageElementDTO)pageElementDTO.clone();
                    descPageElementDTO.setChildList(destChildList);
                    pageElementList.add(descPageElementDTO);
                }
            } else {
                // 原第二层是页面
                List<PageElementDTO> pageElement2List = new ArrayList<>();
                for (PageElementDTO childPage : childList) {
                    // 第三层是页面元素（例如按钮）
                    List<PageElementDTO> oriChildElementList = childPage.getChildList();
                    List<PageElementDTO> destChildElementList = new ArrayList<>();
                    for (PageElementDTO elementDTO : oriChildElementList) {
                        if (hasPageElementList.contains(elementDTO.getCode())) {
                            // 当前用户有该页面元素（例如按钮）的权限
                            destChildElementList.add(elementDTO);
                        }
                    }
                    if (destChildElementList.size() > 0) {
                        // 当前二级页面有子元素（按钮）的权限
                        PageElementDTO descPageElementDTO = (PageElementDTO)childPage.clone();
                        descPageElementDTO.setChildList(destChildElementList);
                        pageElement2List.add(descPageElementDTO);
                    }
                }
                if (pageElement2List.size() > 0) {
                    // 当前一级页面有子元素（二级页面）的权限
                    PageElementDTO descPageElementDTO = (PageElementDTO)pageElementDTO.clone();
                    descPageElementDTO.setChildList(pageElement2List);
                    pageElementList.add(descPageElementDTO);
                }
            }
        }
        return pageElementList;
    }
	</#if>

}