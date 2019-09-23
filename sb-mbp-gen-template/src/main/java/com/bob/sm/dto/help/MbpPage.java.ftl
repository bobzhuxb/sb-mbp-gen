package ${packageName}.dto.help;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页参数配置DTO
 * @author Bob
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MbpPage<T> extends Page<T> {

    private long page = 1;

    private String sort;

    public MbpPage() {
        super(1, 10);
    }

    public MbpPage(long current, long size) {
        super(current, size);
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        MbpPage<U> resultPageNew = new MbpPage<>();
        List<U> recordsNew = getRecords().stream().map(converter).collect(Collectors.toList());
        resultPageNew.setRecords(recordsNew);
        resultPageNew.setSize(getSize());
        resultPageNew.setTotal(getTotal());
        resultPageNew.setCurrent(getCurrent());
        return resultPageNew;
    }

    public static <T> Page<T> empty() {
        return new MbpPage<>();
    }

}
