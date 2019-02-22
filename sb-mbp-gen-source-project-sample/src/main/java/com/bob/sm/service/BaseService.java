package com.bob.sm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bob.sm.dto.help.NormalCriteriaDTO;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    /**
     * 附加的条件查询增强方法，实现类可覆盖该方法，写自己的条件查询增强方法
     * @param wrapper 增强前的Wrapper条件
     * @param normalCriteriaList 普通的查询条件
     * @param revertTableIndexMap 根据表别名反向查条件名的Map
     * @return 增强后的Wrapper条件
     */
    default Wrapper<T> wrapperEnhance(QueryWrapper<T> wrapper, List<NormalCriteriaDTO> normalCriteriaList,
                                      Map<String, String> revertTableIndexMap) {
        return wrapper;
    }

}
