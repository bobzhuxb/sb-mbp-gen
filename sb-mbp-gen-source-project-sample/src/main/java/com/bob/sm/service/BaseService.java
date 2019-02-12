package com.bob.sm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface BaseService<T> {

    Wrapper<T> wrapperEnhance(QueryWrapper<T> wrapper, String tableAliasName, String paramName, Object paramValue);

}
