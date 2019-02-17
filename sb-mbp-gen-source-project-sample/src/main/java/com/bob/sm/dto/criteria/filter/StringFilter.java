package com.bob.sm.dto.criteria.filter;

import java.util.Objects;

public class StringFilter extends Filter<String> {

    private static final long serialVersionUID = 1L;

    private String contains;    // SQL中的like

    private String notContains;    // SQL中的not like

    private String startWith;    // SQL中的like xx%

    private String endWith;    // SQL中的like %xx

    public String getContains() {
        return contains;
    }

    public StringFilter setContains(String contains) {
        this.contains = contains;
        return this;
    }

    public String getNotContains() {
        return notContains;
    }

    public StringFilter setNotContains(String notContains) {
        this.notContains = notContains;
        return this;
    }

    public String getStartWith() {
        return startWith;
    }

    public StringFilter setStartWith(String startWith) {
        this.startWith = startWith;
        return this;
    }

    public String getEndWith() {
        return endWith;
    }

    public StringFilter setEndWith(String endWith) {
        this.endWith = endWith;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final StringFilter that = (StringFilter) o;
        return Objects.equals(contains, that.contains) &&
                Objects.equals(notContains, that.notContains) &&
                Objects.equals(startWith, that.startWith) &&
                Objects.equals(endWith, that.endWith);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contains, notContains, startWith, endWith);
    }

    @Override
    public String toString() {
        return getFilterName() + " ["
                + (getContains() != null ? "contains=" + getContains() + ", " : "")
                + (getNotContains() != null ? "notContains=" + getNotContains() + ", " : "")
                + (getStartWith() != null ? "startWith=" + getStartWith() + ", " : "")
                + (getEndWith() != null ? "endWith=" + getEndWith() + ", " : "")
                + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
                + (getNotEquals() != null ? "notEquals=" + getNotEquals() + ", " : "")
                + (getNullable() != null ? "nullable=" + getNullable() : "")
                + (getIn() != null ? "in=" + getIn() : "")
                + (getNotIn() != null ? "notIn=" + getNotIn() + ", " : "")
                + (getGreaterThan() != null ? "greaterThan=" + getGreaterThan() + ", " : "")
                + (getLessThan() != null ? "lessThan=" + getLessThan() + ", " : "")
                + (getGreaterOrEqualThan() != null ? "greaterOrEqualThan=" + getGreaterOrEqualThan() + ", " : "")
                + (getLessOrEqualThan() != null ? "lessOrEqualThan=" + getLessOrEqualThan() + ", " : "")
                + (getBetweenFrom() != null ? "betweenFrom=" + getBetweenFrom() + ", " : "")
                + (getBetweenTo() != null ? "betweenTo=" + getBetweenTo() + ", " : "")
                + (getNotBetweenFrom() != null ? "notBetweenFrom=" + getNotBetweenFrom() + ", " : "")
                + (getNotBetweenTo() != null ? "notBetweenTo=" + getNotBetweenTo() + ", " : "")
                + "]";
    }

}
