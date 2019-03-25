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
import ${packageName}.util.MyBeanUtil;
import ${packageName}.web.rest.errors.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
@Transactional
public class ${eentityName}ServiceImpl extends ServiceImpl<${eentityName}Mapper, ${eentityName}>
        implements ${eentityName}Service {

    private final Logger log = LoggerFactory.getLogger(${eentityName}ServiceImpl.class);

    private final String DOMAIN_NAME = "${eentityName}";
	<#list fromToList as fromTo>

    @Autowired
    private ${fromTo.fromToEntityType}Mapper ${fromTo.fromToEntityName}Mapper;
	</#list>
	<#assign systemUserServiceName='' />
	<#list toFromList as toFrom>
	<#if toFrom.toFromEntityType != eentityName>

    @Autowired
    private ${toFrom.toFromEntityType}Service ${toFrom.toFromEntityName}Service;
	<#if toFrom.toFromEntityType == 'SystemUser'><#assign systemUserServiceName='${toFrom.toFromEntityName}Service'/></#if>
	</#if>
	</#list>
    <#list fromToList as fromTo>
	<#if fromTo.fromToEntityType != eentityName>

    @Autowired
    private ${fromTo.fromToEntityType}Service ${fromTo.fromToEntityName}Service;
	<#if fromTo.fromToEntityType == 'SystemUser'><#assign systemUserServiceName='${fromTo.fromToEntityName}Service' /></#if>
	</#if>
	</#list>
	<#if systemUserServiceName == '' && eentityName != 'SystemUser'>

    @Autowired
    private SystemUserService systemUserService;
	</#if>
	<#if eentityName == 'BaseDictionary' && (useDictionaryList)?? && (useDictionaryList?size > 0) >
	<#list useDictionaryList as useDictionary>

    @Autowired
	private ${useDictionary.eentityName}Mapper ${useDictionary.entityName}Mapper;
	</#list>
	</#if>
	<#if eentityName == 'SystemUser'>

    @Autowired
    private PasswordEncoder passwordEncoder;
	</#if>

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @param appendParamMap 附加参数
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public ReturnCommonDTO<${eentityName}DTO> baseFindOne(Long id, BaseCriteria criteria,
            Map<String, Object> appendParamMap) {
        log.debug("Service ==> 根据ID查询${eentityName}DTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<${eentityName}> wrapper = baseIdEqualsPrepare(DOMAIN_NAME, id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(criteria);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该条件的查询权限");
        }
        // 执行查询并返回结果
        return Optional.ofNullable(getOne(wrapper)).map(${entityName} ->
                new ReturnCommonDTO(doConvert(${entityName}, criteria,
                        appendParamMap == null ? new HashMap<>() : appendParamMap)))
                .orElse(new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "没有该数据"));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @param appendParamMap 附加参数
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public ReturnCommonDTO<List<${eentityName}DTO>> baseFindAll(${eentityName}Criteria criteria,
            Map<String, Object> appendParamMap) {
        log.debug("Service ==> 查询所有${eentityName}DTO {}", criteria);
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(criteria);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该条件的查询权限");
        }
        // 预处理orderBy的内容
        basePreOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = baseGetDataQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        Wrapper<${eentityName}> wrapper = baseGetWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
        // 执行查询并返回结果
        return new ReturnCommonDTO(baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(${entityName} -> doConvert(${entityName}, criteria,
                        appendParamMap == null ? new HashMap<>() : appendParamMap)).collect(Collectors.toList()));
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @param appendParamMap 附加参数
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public ReturnCommonDTO<IPage<${eentityName}DTO>> baseFindPage(${eentityName}Criteria criteria, MbpPage pageable,
            Map<String, Object> appendParamMap) {
        log.debug("Service ==> 分页查询${eentityName}DTO {}, {}", criteria, pageable);
        Page<${eentityName}> pageQuery = new Page<>(pageable.getPage(), pageable.getSize());
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(criteria);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该条件的查询权限");
        }
        // 预处理orderBy的内容
        basePreOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = baseGetDataQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        String countQuerySql = baseGetCountQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        Wrapper<${eentityName}> wrapper = baseGetWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
        // 执行查询并返回结果
        IPage<${eentityName}DTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(${entityName} -> doConvert(${entityName}, criteria,
                        appendParamMap == null ? new HashMap<>() : appendParamMap));
        int totalCount = baseMapper.joinSelectCount(countQuerySql, wrapper);
        pageResult.setTotal((long)totalCount);
        return new ReturnCommonDTO(pageResult);
    }

    /**
     * 查询个数
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(readOnly = true)
    public ReturnCommonDTO<Integer> baseFindCount(${eentityName}Criteria criteria) {
        log.debug("Service ==> 查询个数${eentityName}DTO {}", criteria);
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(criteria);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该条件的查询权限");
        }
        // 获取查询SQL（select和join）
        String countQuerySql = baseGetCountQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        Wrapper<${eentityName}> wrapper = baseGetWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
        // 执行查询并返回结果
        return new ReturnCommonDTO(baseMapper.joinSelectCount(countQuerySql, wrapper));
    }

    /**
     * 数据权限过滤器
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }

    /**
     * 处理Domain到DTO的转换
     * @param ${entityName} 原始Domain
     * @param criteria 查询条件
     * @param appendParamMap 附加的查询参数条件
     * @return 转换后的DTO
     */
    private ${eentityName}DTO doConvert(${eentityName} ${entityName}, BaseCriteria criteria,
                        Map<String, Object> appendParamMap) {
        ${eentityName}DTO ${entityName}DTO = new ${eentityName}DTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(${entityName}, ${entityName}DTO);
        getAssociationsAll(${entityName}DTO, criteria, appendParamMap);
        return ${entityName}DTO;
    }

    /**
     * 获取关联属性
     * @param ${entityName}DTO 主实体
     * @param criteria 关联属性的条件
     * @param appendParamMap 附加的查询参数条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public ${eentityName}DTO getAssociationsAll(${eentityName}DTO ${entityName}DTO, BaseCriteria criteria,
                        Map<String, Object> appendParamMap) {
        if (${entityName}DTO.getId() == null) {
            return ${entityName}DTO;
        }
        // 处理关联属性（共通）
		baseGetAssociations(DOMAIN_NAME, ${entityName}DTO, criteria, appendParamMap);
        // TODO: 处理关联属性（自定义）
        
        return ${entityName}DTO;
    }

}
