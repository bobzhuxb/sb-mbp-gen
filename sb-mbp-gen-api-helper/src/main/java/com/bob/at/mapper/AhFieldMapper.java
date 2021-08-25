package com.bob.at.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bob.at.domain.AhClassCode;
import com.bob.at.domain.AhField;
import org.apache.ibatis.annotations.Param;

/**
 * 可用实体类 Mapper
 * @author Bob
 */
public interface AhFieldMapper extends BaseMapper<AhField> {

    /**
     * 根据projectId删除数据
     * @param projectId
     */
    void deleteByProjectId(@Param("projectId")String projectId);

}
