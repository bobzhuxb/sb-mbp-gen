package com.bob.sm.dto.help;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MbpPage<T> extends Page<T> {

    private String orderBy;

    public MbpPage() {
        super(1, 10);
    }

    public MbpPage(long current, long size) {
        super(current, size);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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
