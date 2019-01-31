package ${packageName}.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCommonMapper<T> extends BaseMapper<T> {

    List<T> joinSelectList(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    IPage<T> joinSelectPage(Page<T> page, @Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    Integer joinSelectCount(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
