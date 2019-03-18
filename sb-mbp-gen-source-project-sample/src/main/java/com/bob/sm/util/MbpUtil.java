package com.bob.sm.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

public class MbpUtil {

    public static <T> IPage<T> selectPage(BaseMapper<T> baseMapper, IPage<T> pageQuery, Wrapper<T> wrapper) {
        IPage<T> resultPage = baseMapper.selectPage(pageQuery, wrapper);
        int totalCount = baseMapper.selectCount(wrapper);
        resultPage.setTotal((long)totalCount);
        return resultPage;
    }

}
