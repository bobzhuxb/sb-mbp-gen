package ${packageName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.criteria.filter.*;
import ${packageName}.dto.help.MbpPage;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.*;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.*;
import ${packageName}.util.MbpUtil;
import ${packageName}.util.MyBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy
@Transactional
public class ${eentityName}ServiceImpl extends ServiceImpl<${eentityName}Mapper, ${eentityName}> implements ${eentityName}Service {

    private final Logger log = LoggerFactory.getLogger(${eentityName}ServiceImpl.class);
	<#list fromToList as fromTo>

    @Autowired
    private ${fromTo.fromToEntityType}Mapper ${fromTo.fromToEntityName}Mapper;
	</#list>
	<#list toFromList as toFrom>

    @Autowired
    private ${toFrom.toFromEntityType}Service ${toFrom.toFromEntityName}Service;
	</#list>
    <#list fromToList as fromTo>

    @Autowired
    private ${fromTo.fromToEntityType}Service ${fromTo.fromToEntityName}Service;
	</#list>

    /**
     * 新增或更新
     * @param ${entityName}DTO 新增或更新的内容
	 * @return 结果返回码和消息
     */
    public ReturnCommonDTO save(${eentityName}DTO ${entityName}DTO) {
        log.debug("Service ==> 新增或修改${eentityName} {}", ${entityName}DTO);
		if (${entityName}DTO.getId() != null) {
			// 修改
			long ${entityName}Id = ${entityName}DTO.getId();
			<#list fromToList as fromTo>
			<#if fromTo.relationType == "OneToMany">
			if (${entityName}DTO.get${fromTo.fromToEntityUName}List() != null) {
				// 清空${fromTo.fromToComment}列表
				${fromTo.fromToEntityName}Mapper.deleteByMap(new HashMap<String, Object>() {{
					put("${fromTo.fromColumnName}", ${entityName}Id);
				}});
			}
			</#if>
			<#if fromTo.relationType == "OneToOne">
			if (${entityName}DTO.get${fromTo.fromToEntityUName}() != null) {
			    if (${entityName}DTO.get${fromTo.fromToEntityUName}().getId() == null) {
				    // 删除${fromTo.fromToComment}
				    ${fromTo.fromToEntityName}Mapper.deleteByMap(new HashMap<String, Object>() {{
					    put("${fromTo.fromColumnName}", ${entityName}Id);
				    }});
                }
			}
			</#if>
			</#list>
		}
        ${eentityName} ${entityName} = new ${eentityName}();
        MyBeanUtil.copyNonNullProperties(${entityName}DTO, ${entityName});
        boolean result = saveOrUpdate(${entityName});
        long ${entityName}Id = ${entityName}.getId();
		<#if fromToList?? && (fromToList?size > 0) >
        // 需要级联保存的属性
		</#if>
		<#list fromToList as fromTo>
		<#if fromTo.relationType == "OneToMany">
        if (${entityName}DTO.get${fromTo.fromToEntityUName}List() != null) {
            // 新增${fromTo.fromToComment}
            for (${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO : ${entityName}DTO.get${fromTo.fromToEntityUName}List()) {
                ${fromTo.fromToEntityType} ${fromTo.fromToEntityName} = new ${fromTo.fromToEntityType}();
                MyBeanUtil.copyNonNullProperties(${fromTo.fromToEntityName}DTO, ${fromTo.fromToEntityName});
                ${fromTo.fromToEntityName}.set${fromTo.toFromEntityUName}Id(${entityName}Id);
                ${fromTo.fromToEntityName}.setInsertTime(${entityName}DTO.getInsertTime());
                ${fromTo.fromToEntityName}.setUpdateTime(${entityName}DTO.getUpdateTime());
                ${fromTo.fromToEntityName}Mapper.insert(${fromTo.fromToEntityName});
			}
        }
		</#if>
		<#if fromTo.relationType == "OneToOne">
        if (${entityName}DTO.get${fromTo.fromToEntityUName}() != null) {
            // 新增或更新${fromTo.fromToComment}
            ${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO = ${entityName}DTO.get${fromTo.fromToEntityUName}();
            ${fromTo.fromToEntityType} ${fromTo.fromToEntityName} = new ${fromTo.fromToEntityType}();
            MyBeanUtil.copyNonNullProperties(${fromTo.fromToEntityName}DTO, ${fromTo.fromToEntityName});
            ${fromTo.fromToEntityName}.set${fromTo.toFromEntityUName}Id(${entityName}Id);
            ${fromTo.fromToEntityName}.setInsertTime(${entityName}DTO.getInsertTime());
            ${fromTo.fromToEntityName}.setUpdateTime(${entityName}DTO.getUpdateTime());
			if (${entityName}DTO.get${fromTo.fromToEntityUName}().getId() == null) {
                // 新增
                ${fromTo.fromToEntityName}Mapper.insert(${fromTo.fromToEntityName});
            } else {
                // 更新
                ${fromTo.fromToEntityName}Mapper.updateById(${fromTo.fromToEntityName});
            }
        }
		</#if>
		</#list>
        ${entityName}DTO.setId(${entityName}.getId());
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 删除${eentityName}DTO {}", id);
		<#list fromToList as fromTo>
        // 删除${fromTo.fromToComment}
        ${fromTo.fromToEntityName}Mapper.deleteByMap(new HashMap<String, Object>() {{
            put("${fromTo.fromColumnName}", id);
        }});
		</#list>
        boolean result = removeById(id);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "删除失败");
    }

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public Optional<${eentityName}DTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询${eentityName}DTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<${eentityName}> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(${entityName} -> {return doConvert(${entityName}, criteria);});
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<${eentityName}DTO> findAll(${eentityName}Criteria criteria) {
        log.debug("Service ==> 查询所有${eentityName}DTO {}", criteria);
        Wrapper<${eentityName}> wrapper = new MbpUtil().getWrapper(null, criteria, ${eentityName}.class);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(getDataQuerySql(criteria), wrapper).stream()
                .map(${eentityName} -> {return doConvert(${eentityName}, criteria);}).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<${eentityName}DTO> findPage(${eentityName}Criteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询${eentityName}DTO {}, {}", criteria, pageable);
        Page<${eentityName}> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        Wrapper<${eentityName}> wrapper = MbpUtil.getWrapper(null, criteria, ${eentityName}.class);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<${eentityName}DTO> pageResult = baseMapper.joinSelectPage(pageQuery, getDataQuerySql(criteria), wrapper)
                    .convert(${entityName} -> {return doConvert(${entityName}, criteria);});
        int totalCount = baseMapper.joinSelectCount(getCountQuerySql(criteria), wrapper);
        pageResult.setTotal((long)totalCount);
        return pageResult;
    }

    /**
     * 查询个数
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(readOnly = true)
    public int findCount(${eentityName}Criteria criteria) {
        log.debug("Service ==> 查询个数${eentityName}DTO {}", criteria);
        Wrapper<${eentityName}> wrapper = MbpUtil.getWrapper(null, criteria, ${eentityName}.class);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return 0;
        }
        return baseMapper.joinSelectCount(getCountQuerySql(criteria), wrapper);
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param criteria 附加条件
     * @return 查询通用Wrapper
     */
    private Wrapper<${eentityName}> idEqualsPrepare(Long id, BaseCriteria criteria) {
        ${eentityName}Criteria ${entityName}Criteria = new ${eentityName}Criteria();
        MyBeanUtil.copyNonNullProperties(criteria, ${entityName}Criteria);
        Wrapper<${eentityName}> wrapper = MbpUtil.getWrapper(null, ${entityName}Criteria, ${eentityName}.class);
        ((QueryWrapper<${eentityName}>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<${eentityName}> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(${eentityName}Criteria criteria) {
        String joinDataSql = "select " + ${eentityName}.getTableName() + ".*" + getFromAndJoinSql(criteria);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @return
     */
    private String getCountQuerySql(${eentityName}Criteria criteria) {
        String joinCountSql = "select count(0)" + getFromAndJoinSql(criteria);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(${eentityName}Criteria criteria) {
        String joinSubSql = " from " + ${eentityName}.getTableName();
        joinSubSql += getJoinSql(criteria);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(${eentityName}Criteria criteria) {
        String joinSubSql = "";
		<#list toFromList as toFrom>
        if (criteria.get${toFrom.toFromEntityUName}() != null) {
            joinSubSql += " left join " + ${toFrom.toFromEntityType}.getTableName() + " on "
                    + ${toFrom.toFromEntityType}.getTableName() + ".id = " + ${eentityName}.getTableName()
                    + ".${toFrom.fromColumnName}";
            joinSubSql += ${toFrom.toFromEntityName}Service.getJoinSql(criteria.get${toFrom.toFromEntityUName}());
        }
		</#list>
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param ${entityName} 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private ${eentityName}DTO doConvert(${eentityName} ${entityName}, BaseCriteria criteria) {
        ${eentityName}DTO ${entityName}DTO = new ${eentityName}DTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(${entityName}, ${entityName}DTO);
        getAssociations(${entityName}DTO, criteria);
        return ${entityName}DTO;
    }

    /**
     * 获取关联属性
     * @param ${entityName}DTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public ${eentityName}DTO getAssociations(${eentityName}DTO ${entityName}DTO, BaseCriteria criteria) {
        if (${entityName}DTO.getId() == null) {
            return ${entityName}DTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
			<#list fromToList as fromTo>
			<#if fromTo.relationType == "OneToMany">
            if (associationNameList.contains("${fromTo.fromToEntityName}List")) {
                // 获取${fromTo.fromToComment}列表
                List<${fromTo.fromToEntityType}DTO> ${fromTo.fromToEntityName}List = ${fromTo.fromToEntityName}Mapper.selectList(
                        new QueryWrapper<${fromTo.fromToEntityType}>().eq("${fromTo.fromColumnName}", ${entityName}DTO.getId())
                ).stream().map(${fromTo.fromToEntityName} -> {
                    ${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO = new ${fromTo.fromToEntityType}DTO();
                    MyBeanUtil.copyNonNullProperties(${fromTo.fromToEntityName}, ${fromTo.fromToEntityName}DTO);
                    return ${fromTo.fromToEntityName}DTO;
                }).collect(Collectors.toList());
                ${entityName}DTO.set${fromTo.fromToEntityUName}List(${fromTo.fromToEntityName}List);
                // 继续追查
                if (${fromTo.fromToEntityName}List != null && ${fromTo.fromToEntityName}List.size() > 0) {
			</#if>
			<#if fromTo.relationType == "OneToOne">
            if (associationNameList.contains("${fromTo.fromToEntityName}")) {
                // 获取${fromTo.fromToComment}
				${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO = null;
                ${fromTo.fromToEntityType} ${fromTo.fromToEntityName} = ${fromTo.fromToEntityName}Mapper.selectOne(
                        new QueryWrapper<${fromTo.fromToEntityType}>().eq("${fromTo.fromColumnName}", ${entityName}DTO.getId())
                );
                if (${fromTo.fromToEntityName} != null) {
                    ${fromTo.fromToEntityName}DTO = new ${fromTo.fromToEntityType}DTO();
                    MyBeanUtil.copyNonNullProperties(${fromTo.fromToEntityName}, ${fromTo.fromToEntityName}DTO);
                }
                ${entityName}DTO.set${fromTo.fromToEntityUName}(${fromTo.fromToEntityName}DTO);
                // 继续追查
                if (${fromTo.fromToEntityName}DTO != null) {
			</#if>
                    List<String> associationName2List = new ArrayList<>();
                    for (String associationName : associationNameList) {
						<#if fromTo.relationType == "OneToMany">
                        if (associationName.startsWith("${fromTo.fromToEntityName}List.")) {
                            String associationName2 = associationName.substring("${fromTo.fromToEntityName}List.".length());
						</#if>
						<#if fromTo.relationType == "OneToOne">
                        if (associationName.startsWith("${fromTo.fromToEntityName}.")) {
                            String associationName2 = associationName.substring("${fromTo.fromToEntityName}.".length());
						</#if>
                            associationName2List.add(associationName2);
                        }
                    }
                    BaseCriteria ${fromTo.fromToEntityName}Criteria = new BaseCriteria();
                    ${fromTo.fromToEntityName}Criteria.setAssociationNameList(associationName2List);
					<#if fromTo.relationType == "OneToMany">
                    for (${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO : ${fromTo.fromToEntityName}List) {
                        ${fromTo.fromToEntityName}Service.getAssociations(${fromTo.fromToEntityName}DTO, ${fromTo.fromToEntityName}Criteria);
                    }
					</#if>
					<#if fromTo.relationType == "OneToOne">
                    ${fromTo.fromToEntityName}Service.getAssociations(${fromTo.fromToEntityName}DTO, ${fromTo.fromToEntityName}Criteria);
					</#if>
                }
            }
			</#list>
			<#list toFromList as toFrom>
            if (associationNameList.contains("${toFrom.toFromEntityName}")) {
			    // 获取${toFrom.toFromComment}
                Long ${toFrom.toFromEntityName}Id = ${entityName}DTO.get${toFrom.toFromEntityUName}Id();
                if (${toFrom.toFromEntityName}Id == null) {
                    return ${entityName}DTO;
                }
                List<String> associationName2List = new ArrayList<>();
                for (String associationName : associationNameList) {
                    if (associationName.startsWith("${toFrom.toFromEntityName}.")) {
                        String associationName2 = associationName.substring("${toFrom.toFromEntityName}.".length());
                        associationName2List.add(associationName2);
                    }
                }
                BaseCriteria ${toFrom.toFromEntityName}Criteria = new BaseCriteria();
                ${toFrom.toFromEntityName}Criteria.setAssociationNameList(associationName2List);
                Optional<${toFrom.toFromEntityType}DTO> ${toFrom.toFromEntityName}Optional = ${toFrom.toFromEntityName}Service.findOne(${toFrom.toFromEntityName}Id, ${toFrom.toFromEntityName}Criteria);
                ${entityName}DTO.set${toFrom.toFromEntityUName}(${toFrom.toFromEntityName}Optional.isPresent() ? ${toFrom.toFromEntityName}Optional.get() : null);
            }
			</#list>
        }
        return ${entityName}DTO;
    }

}
