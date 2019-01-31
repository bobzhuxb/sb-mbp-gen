package com.bob.sm.dto.criteria.filter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 特殊Filter：无任何过滤的条件过滤器
 * @param <FIELD_TYPE>
 */
public class NothingFilter<FIELD_TYPE> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String none;    // 什么条件都没有（占位用）

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NothingFilter<?> filter = (NothingFilter<?>) o;
        return Objects.equals(none, filter.none);
    }

    @Override
    public int hashCode() {
        return Objects.hash(none);
    }

    @Override
    public String toString() {
        return getFilterName() + " ["
                + (getNone() != null ? "none=" + getNone() + ", " : "")
                + "]";
    }

    protected String getFilterName() {
        return this.getClass().getSimpleName();
    }

}
