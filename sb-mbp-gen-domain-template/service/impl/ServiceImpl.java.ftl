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
     * 新增或更新
     * @param ${entityName}DTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事务的一致性
     */
    public ReturnCommonDTO save(${eentityName}DTO ${entityName}DTO) {
        log.debug("Service ==> 新增或修改${eentityName} {}", ${entityName}DTO);
        String nowTime = ${entityName}DTO.getId() == null ? ${entityName}DTO.getInsertTime() : ${entityName}DTO.getUpdateTime();
        Long nowUserId = ${entityName}DTO.getId() == null ? ${entityName}DTO.getInsertUserId() : ${entityName}DTO.getOperateUserId();
        // 新增修改验证
        saveValidator(${entityName}DTO);
		if (${entityName}DTO.getId() != null) {
			// 修改
			long ${entityName}Id = ${entityName}DTO.getId();
			<#list fromToList as fromTo>
			<#if fromTo.relationType == "OneToMany">
			if (${entityName}DTO.get${fromTo.fromToEntityUName}List() != null) {
                // 获取传入的${fromTo.fromToComment}ID列表（去掉null的）
                List<Long> ${fromTo.fromToEntityName}IdList = ${entityName}DTO.get${fromTo.fromToEntityUName}List().stream()
                        .filter(${fromTo.fromToEntityName}DTO -> ${entityName}DTO.getId() != null)
                        .map(${fromTo.fromToEntityName}DTO -> ${entityName}DTO.getId()).collect(Collectors.toList());
                if (${fromTo.fromToEntityName}IdList == null) {
                    // 如果${fromTo.fromToComment}都没有填写ID，则认为是全刷新，清空${fromTo.fromToComment}列表
                    <#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>deleteByMapCascade(new HashMap<String, Object>() {{
                        put("${fromTo.fromColumnName}", ${entityName}Id);
                    }});
                } else {
                    // 如果${fromTo.fromToComment}部分填写了ID，则删除未填写ID的数据
                    <#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>deleteByIdList(${fromTo.fromToEntityName}IdList);
                }
			}
			</#if>
			<#if fromTo.relationType == "OneToOne">
			if (${entityName}DTO.get${fromTo.fromToEntityUName}() != null) {
			    if (${entityName}DTO.get${fromTo.fromToEntityUName}().getId() == null) {
				    // 删除${fromTo.fromToComment}
				    <#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>deleteByMapCascade(new HashMap<String, Object>() {{
					    put("${fromTo.fromColumnName}", ${entityName}Id);
				    }});
                }
			}
			</#if>
			</#list>
		}
        // 新增或更新${entityComment}（当前实体）
        ${eentityName} ${entityName} = new ${eentityName}();
        MyBeanUtil.copyNonNullProperties(${entityName}DTO, ${entityName});
        if (${entityName}.getId() == null) {
            // 新增
            ${entityName}.setInsertUserId(${entityName}DTO.getOperateUserId());
            <#if eentityName == 'SystemUser'>
            if (${entityName}.getPassword() == null) {
                ${entityName}.setPassword("123456");
            }
            ${entityName}.setPassword(passwordEncoder.encode(${entityName}.getPassword()));
			</#if>
        }
        boolean result = saveOrUpdate(${entityName});
        long ${entityName}Id = ${entityName}.getId();
		<#if fromToList?? && (fromToList?size > 0) >
        // 需要级联保存的属性
		</#if>
		<#list fromToList as fromTo>
		<#if fromTo.relationType == "OneToMany">
        if (${entityName}DTO.get${fromTo.fromToEntityUName}List() != null) {
			// 新增或更新${fromTo.fromToComment}
			for (${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO : ${entityName}DTO.get${fromTo.fromToEntityUName}List()) {
				${fromTo.fromToEntityName}DTO.set${fromTo.toFromEntityUName}Id(${entityName}Id);
				${fromTo.fromToEntityName}DTO.setInsertUserId(nowUserId);
				${fromTo.fromToEntityName}DTO.setOperateUserId(nowUserId);
				${fromTo.fromToEntityName}DTO.setInsertTime(nowTime);
				${fromTo.fromToEntityName}DTO.setUpdateTime(nowTime);
				<#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>save(${fromTo.fromToEntityName}DTO);
			}
        }
		</#if>
		<#if fromTo.relationType == "OneToOne">
        if (${entityName}DTO.get${fromTo.fromToEntityUName}() != null) {
            // 新增或更新${fromTo.fromToComment}
            ${fromTo.fromToEntityType}DTO ${fromTo.fromToEntityName}DTO = ${entityName}DTO.get${fromTo.fromToEntityUName}();
            ${fromTo.fromToEntityName}DTO.set${fromTo.toFromEntityUName}Id(${entityName}Id);
            if (${fromTo.fromToEntityName}DTO.getId() == null) {
                // 新增
                ${fromTo.fromToEntityName}DTO.setInsertUserId(nowUserId);
            }
            ${fromTo.fromToEntityName}DTO.setOperateUserId(nowUserId);
            ${fromTo.fromToEntityName}DTO.setInsertTime(nowTime);
            ${fromTo.fromToEntityName}DTO.setUpdateTime(nowTime);
			<#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>save(${fromTo.fromToEntityName}DTO);
        }
		</#if>
		</#list>
        ${entityName}DTO.setId(${entityName}Id);
        return result ? new ReturnCommonDTO(${entityName}Id) : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 新增修改验证
     * @param ${entityName}DTO 新增或修改的数据
     * @return
     * @throws CommonException 不能操作的异常
     */
    private void saveValidator(${eentityName}DTO ${entityName}DTO) {
        // TODO: 新增修改验证写在这里
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事务的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除${eentityName}DTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除${eentityName}DTO {}", idList);
        idList.forEach(id -> {
            ReturnCommonDTO returnCommonDTO = deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonException(returnCommonDTO.getErrMsg());
            }
        });
        return new ReturnCommonDTO();
    }

    /**
     * 删除不在ID列表中的数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdListNot(List<Long> idList) {
        log.debug("Service ==> 删除不在ID列表中的${eentityName}DTO {}", idList);
        Optional.ofNullable(baseMapper.selectList(new QueryWrapper<${eentityName}>().select("id").notIn("id", idList)))
                .get().stream().forEach(${entityName} -> {
            ReturnCommonDTO returnCommonDTO = deleteByMapCascade(new HashMap<String, Object>() {{put("id", ${entityName}.getId());}});
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonException(returnCommonDTO.getErrMsg());
            }
        });
        return new ReturnCommonDTO();
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事务的一致性
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除${eentityName}DTO {}", columnMap);
		<#if (fromToList)?? && (fromToList?size > 0) >
        // 删除级联实体或置空关联字段
        listByMap(columnMap).forEach(${entityName} -> {
			<#if eentityName == 'BaseDictionary' && (useDictionaryList)?? && (useDictionaryList?size > 0) >
            // 判断是否有使用该数据字典的数据
			<#list useDictionaryList as useDictionary>
			<#list useDictionary.fieldList as useDictionaryField>
            if ("${useDictionaryField.dictionaryType}".equals(baseDictionary.getDicType())) {
                int ${useDictionary.entityName}Count = ${useDictionary.entityName}Mapper.selectCount(
                        new QueryWrapper<${useDictionary.eentityName}>().eq("${useDictionaryField.camelNameUnderline}", baseDictionary.getDicCode()));
                if (${useDictionary.entityName}Count > 0) {
                    throw new CommonException("有使用该数据字典的${useDictionary.entityComment}信息，无法删除。");
                }
            }
			</#list>
			</#list>
			</#if>
			<#list fromToList as fromTo>
			<#if fromTo.fromToDeleteType == 'DELETE'>
            // 删除级联的${fromTo.fromToComment}
            <#if fromTo.fromToEntityType != eentityName>${fromTo.fromToEntityName}Service.</#if>deleteByMapCascade(new HashMap<String, Object>() {{put("${fromTo.fromColumnName}", ${entityName}.getId());}});
			</#if>
			<#if fromTo.fromToDeleteType == 'NULL'>
            // ${fromTo.fromToComment}的${fromTo.fromColumnName}列级联置空
            ${fromTo.fromToEntityName}Mapper.${fromTo.toFromEntityName}IdCascadeToNull(${entityName}.getId());
			</#if>
			<#if fromTo.fromToDeleteType == 'FORBIDDEN'>
            // 有级联的${fromTo.fromToComment}则不允许删除
            int ${fromTo.fromToEntityName}Count = ${fromTo.fromToEntityName}Mapper.selectCount(
                    new QueryWrapper<${fromTo.fromToEntityType}>().eq("${fromTo.fromColumnName}", ${entityName}.getId()));
            if (${fromTo.fromToEntityName}Count > 0) {
                throw new CommonException("有存在的${fromTo.fromToComment}，无法删除。");
            }
			</#if>
			</#list>
        });
		</#if>
        // 根据指定条件删除${entityComment}数据
        removeByMap(columnMap);
        return new ReturnCommonDTO();
    }

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @param appendParamMap 附加参数
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public ReturnCommonDTO<${eentityName}DTO> findOne(Long id, BaseCriteria criteria,
            Map<String, Object> appendParamMap) {
        log.debug("Service ==> 根据ID查询${eentityName}DTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<${eentityName}> wrapper = idEqualsPrepare(DOMAIN_NAME, id, criteria);
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
    public ReturnCommonDTO<List<${eentityName}DTO>> findAll(${eentityName}Criteria criteria,
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
        preOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = getDataQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        Wrapper<${eentityName}> wrapper = getWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
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
    public ReturnCommonDTO<IPage<${eentityName}DTO>> findPage(${eentityName}Criteria criteria, MbpPage pageable,
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
        preOrderBy(criteria, tableIndexMap);
        // 获取查询SQL（select和join）
        String dataQuerySql = getDataQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        String countQuerySql = getCountQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        Wrapper<${eentityName}> wrapper = getWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
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
    public ReturnCommonDTO<Integer> findCount(${eentityName}Criteria criteria) {
        log.debug("Service ==> 查询个数${eentityName}DTO {}", criteria);
        // 级联查询参数（直到字段）与表序号表类型（下划线隔开）的Map
        Map<String, String> tableIndexMap = new HashMap<>();
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(criteria);
        if (!dataFilterPass) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "没有该条件的查询权限");
        }
        // 获取查询SQL（select和join）
        String countQuerySql = getCountQuerySql(DOMAIN_NAME, criteria, tableIndexMap);
        // 处理where条件
        Wrapper<${eentityName}> wrapper = getWrapper(DOMAIN_NAME, null, criteria, null, tableIndexMap, null);
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
		getAssociations(DOMAIN_NAME, ${entityName}DTO, criteria, appendParamMap);
        // TODO: 处理关联属性（自定义）
        
        return ${entityName}DTO;
    }

}
