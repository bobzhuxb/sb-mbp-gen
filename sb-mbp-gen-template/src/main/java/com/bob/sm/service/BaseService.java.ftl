package ${packageName}.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.dto.help.NormalCriteriaDTO;

import java.util.List;
import java.util.Map;

public interface BaseService<T, C> {

    /**
     * 附加的条件查询增强方法，实现类可覆盖该方法，写自己的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param criteria 原始的查询条件
     * @param normalCriteriaList 普通的查询条件
     * @param revertTableIndexMap 根据表别名反向查条件名的Map
     * @return 增强后的Wrapper条件
     */
    default <C> Wrapper<T> wrapperEnhance(QueryWrapper<T> wrapper, C criteria, List<NormalCriteriaDTO> normalCriteriaList,
                                      Map<String, String> revertTableIndexMap) {
        return wrapper;
    }

}
