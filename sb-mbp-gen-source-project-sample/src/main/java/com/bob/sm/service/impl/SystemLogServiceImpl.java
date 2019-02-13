package com.bob.sm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bob.sm.config.Constants;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.domain.*;
import com.bob.sm.dto.*;
import com.bob.sm.dto.criteria.*;
import com.bob.sm.dto.criteria.filter.*;
import com.bob.sm.dto.help.MbpPage;
import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.mapper.*;
import com.bob.sm.security.SecurityUtils;
import com.bob.sm.service.*;
import com.bob.sm.util.MbpUtil;
import com.bob.sm.util.MyBeanUtil;
import com.bob.sm.web.rest.errors.CommonException;
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
@EnableAspectJAutoProxy
@Transactional
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService,
        BaseService<SystemLog> {

    private final Logger log = LoggerFactory.getLogger(SystemLogServiceImpl.class);

    /**
     * 新增或更新
     * @param systemLogDTO 新增或更新的内容
	 * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO save(SystemLogDTO systemLogDTO) {
        log.debug("Service ==> 新增或修改SystemLog {}", systemLogDTO);
		if (systemLogDTO.getId() != null) {
			// 修改
			long systemLogId = systemLogDTO.getId();
		}
        // 新增或更新操作日志（当前实体）
        SystemLog systemLog = new SystemLog();
        MyBeanUtil.copyNonNullProperties(systemLogDTO, systemLog);
        if (systemLog.getId() == null) {
            // 新增
            systemLog.setInsertUserId(systemLogDTO.getOperateUserId());
        }
        boolean result = saveOrUpdate(systemLog);
        long systemLogId = systemLog.getId();
        systemLogDTO.setId(systemLogId);
        return result ? new ReturnCommonDTO() : new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "保存失败");
    }

    /**
     * 根据ID删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param id 主键ID
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteById(Long id) {
        log.debug("Service ==> 根据ID删除SystemLogDTO {}", id);
		return deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
    }

    /**
     * 根据ID列表删除数据（同时级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param idList 主键ID列表
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByIdList(List<Long> idList) {
        log.debug("Service ==> 根据ID列表删除SystemLogDTO {}", idList);
        for (long id : idList) {
		    ReturnCommonDTO returnCommonDTO = deleteByMapCascade(new HashMap<String, Object>() {{put("id", id);}});
            if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
                throw new CommonException(returnCommonDTO.getErrMsg());
            }
        }
        return new ReturnCommonDTO();
    }

    /**
     * 根据指定条件删除数据（级联删除或置空关联字段，其中级联删除类似于JPA的CascadeType.REMOVE）
     * @param columnMap 表字段map对象
     * @return 结果返回码和消息
	 * 注意：此处不要抛出声明式异常，请封装后抛出CommonException异常或其子异常，以保证事物的一致性
     */
    public ReturnCommonDTO deleteByMapCascade(Map<String, Object> columnMap) {
        log.debug("Service ==> 根据指定Map删除SystemLogDTO {}", columnMap);
        // 根据指定条件删除操作日志数据
        removeByMap(columnMap);
        return new ReturnCommonDTO();
    }

    /**
     * 查询单条数据
	 * @param id 主键ID
	 * @param criteria 附加条件
     * @return 单条数据内容
     */
    @Transactional(readOnly = true)
    public Optional<SystemLogDTO> findOne(Long id, BaseCriteria criteria) {
        log.debug("Service ==> 根据ID查询SystemLogDTO {}, {}", id, criteria);
        // ID条件设定
        Wrapper<SystemLog> wrapper = idEqualsPrepare(id, criteria);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return Optional.empty();
        }
        return Optional.ofNullable(getOne(wrapper)).map(systemLog -> doConvert(systemLog, criteria));
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 数据列表
     */
    @Transactional(readOnly = true)
    public List<SystemLogDTO> findAll(SystemLogCriteria criteria) {
        log.debug("Service ==> 查询所有SystemLogDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        Wrapper<SystemLog> wrapper = MbpUtil.getWrapper(null, criteria, SystemLog.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return new ArrayList<>();
        }
        return baseMapper.joinSelectList(dataQuerySql, wrapper).stream()
                .map(systemLog -> doConvert(systemLog, criteria)).collect(Collectors.toList());
    }

    /**
     * 分页查询
	 * @param criteria 查询条件
	 * @param pageable 分页条件
     * @return 分页列表
     */
    @Transactional(readOnly = true)
    public IPage<SystemLogDTO> findPage(SystemLogCriteria criteria, MbpPage pageable) {
        log.debug("Service ==> 分页查询SystemLogDTO {}, {}", criteria, pageable);
        Page<SystemLog> pageQuery = new Page<>(pageable.getCurrent(), pageable.getSize());
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String dataQuerySql = getDataQuerySql(criteria, tableIndexMap);
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemLog> wrapper = MbpUtil.getWrapper(null, criteria, SystemLog.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return MbpPage.empty();
        }
        IPage<SystemLogDTO> pageResult = baseMapper.joinSelectPage(pageQuery, dataQuerySql, wrapper)
                    .convert(systemLog -> doConvert(systemLog, criteria));
        int totalCount = baseMapper.joinSelectCount(countQuerySql, wrapper);
        pageResult.setTotal((long)totalCount);
        return pageResult;
    }

    /**
     * 查询个数
     * @param criteria 查询条件
     * @return 个数
     */
    @Transactional(readOnly = true)
    public int findCount(SystemLogCriteria criteria) {
        log.debug("Service ==> 查询个数SystemLogDTO {}", criteria);
        // 表对应的序号Map
        Map<String, Integer> tableIndexMap = new HashMap<>();
        String countQuerySql = getCountQuerySql(criteria, tableIndexMap);
        Wrapper<SystemLog> wrapper = MbpUtil.getWrapper(null, criteria, SystemLog.class, null, tableIndexMap, this);
        // 数据权限过滤
        boolean dataFilterPass = dataAuthorityFilter(wrapper, criteria);
        if (!dataFilterPass) {
            return 0;
        }
        return baseMapper.joinSelectCount(countQuerySql, wrapper);
    }

    /**
     * 附加的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param tableAliasName 表名的别名
     * @param paramName 参数名
     * @param paramValue 参数值
     * @return 增强后的Wrapper条件
     */
    public Wrapper<SystemLog> wrapperEnhance(QueryWrapper<SystemLog> wrapper, String tableAliasName,
                                             String paramName, Object paramValue) {
        // TODO: 增强的条件查询写在这里
        return wrapper;
    }

    /**
     * 根据ID查询的条件准备
     * @param id 主键ID
     * @param criteria 附加条件
     * @return 查询通用Wrapper
     */
    private Wrapper<SystemLog> idEqualsPrepare(Long id, BaseCriteria criteria) {
        SystemLogCriteria systemLogCriteria = new SystemLogCriteria();
        MyBeanUtil.copyNonNullProperties(criteria, systemLogCriteria);
        Wrapper<SystemLog> wrapper = MbpUtil.getWrapper(null, systemLogCriteria, SystemLog.class, null, null, this);
        ((QueryWrapper<SystemLog>)wrapper).eq("id", id);
        return wrapper;
    }

    /**
     * 数据权限过滤器
     * @param wrapper 查询通用Wrapper
     * @param criteria 附加条件
	 * @return 是否有权限（true：有权限  false：无权限）
     */
    private boolean dataAuthorityFilter(Wrapper<SystemLog> wrapper, BaseCriteria criteria) {
        // TODO: 数据权限的过滤写在这里
		return true;
    }
	
    /**
     * 获取查询数据的SQL
     * @return
     */
    private String getDataQuerySql(SystemLogCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinDataSql = "SELECT " + SystemLog.getTableName() + "_" + tableCount + ".*";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        joinDataSql += getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinDataSql;
    }

    /**
     * 获取查询数量的SQL
     * @return
     */
    private String getCountQuerySql(SystemLogCriteria criteria, Map<String, Integer> tableIndexMap) {
        int tableCount = 0;
        final int fromTableCount = tableCount;
        String joinCountSql = "SELECT COUNT(0)" + getFromAndJoinSql(criteria, tableCount, fromTableCount, tableIndexMap);
        return joinCountSql;
    }

    /**
     * 获取from和级联SQL
     * @return
     */
    private String getFromAndJoinSql(SystemLogCriteria criteria, int tableCount, int fromTableCount,
                                     Map<String, Integer> tableIndexMap) {
        String joinSubSql = " FROM " + SystemLog.getTableName() + " AS " + SystemLog.getTableName() + "_" + tableCount;
        joinSubSql += getJoinSql(criteria, tableCount, fromTableCount, null, tableIndexMap);
        return joinSubSql;
    }

    /**
     * 获取级联SQL
     * @return
     */
    public String getJoinSql(SystemLogCriteria criteria, int tableCount, int fromTableCount, String lastFieldName,
                             Map<String, Integer> tableIndexMap) {
        String joinSubSql = "";
        // 处理关联数据字典值
        List<String> dictionaryNameList = criteria.getDictionaryNameList();
        if (dictionaryNameList != null) {
            // 此处处理数据字典的JOIN
        }
        return joinSubSql;
    }

    /**
     * 处理Domain到DTO的转换
     * @param systemLog 原始Domain
     * @param criteria 查询条件
     * @return 转换后的DTO
     */
    private SystemLogDTO doConvert(SystemLog systemLog, BaseCriteria criteria) {
        SystemLogDTO systemLogDTO = new SystemLogDTO();
        // TODO:在此处对每条数据做些处理
        MyBeanUtil.copyNonNullProperties(systemLog, systemLogDTO);
        getAssociations(systemLogDTO, criteria);
        return systemLogDTO;
    }

    /**
     * 获取关联属性
     * @param systemLogDTO 主实体
     * @param criteria 关联属性的条件
     * @return 带关联属性的主实体
     */
    @Transactional(readOnly = true)
    public SystemLogDTO getAssociations(SystemLogDTO systemLogDTO, BaseCriteria criteria) {
        if (systemLogDTO.getId() == null) {
            return systemLogDTO;
        }
        // 处理关联属性
        List<String> associationNameList = criteria.getAssociationNameList();
        if (associationNameList != null) {
        }
        return systemLogDTO;
    }

}
