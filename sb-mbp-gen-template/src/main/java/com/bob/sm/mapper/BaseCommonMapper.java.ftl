package ${packageName}.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BaseCommonMapper<T> extends BaseMapper<T> {

    @Update("UPDATE ${r'${tableName}'} SET ${r'${relationColumnName}'} = null WHERE ${r'${relationColumnName}'} = ${r'#{'}relationId}")
    void cascadeToNull(@Param("tableName") String tableName, @Param("relationColumnName") String relationColumnName,
                       @Param("relationId") String relationId);

    @Select("<script>"
            + "${r'${queryMain}'} ${r'${ew.customSqlSegment}'}"
            + "<if test='limit != null'>"
            + "LIMIT ${r'${limit}'}"
            + "</if>"
            + "</script>")
    List<T> joinSelectList(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper,
                           @Param("limit") Integer limit);

    @Select("${r'${queryMain}'} ${r'${ew.customSqlSegment}'}")
    IPage<T> joinSelectPage(Page<T> page, @Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Select("${r'${queryMain}'} ${r'${ew.customSqlSegment}'}")
    Integer joinSelectCount(@Param("queryMain") String queryMain, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}
