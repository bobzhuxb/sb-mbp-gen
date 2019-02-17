package com.bob.sm.dto.criteria.filter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Filter<FIELD_TYPE> implements Serializable {

    private static final long serialVersionUID = 1L;
    private FIELD_TYPE equals;      // 等于
    private FIELD_TYPE notEquals;   // 不等于
    private Boolean nullable;       // 是否为null
    private List<FIELD_TYPE> in;    // SQL中的in
    private List<FIELD_TYPE> notIn; // SQL中的not in

    private FIELD_TYPE greaterThan;
    private FIELD_TYPE lessThan;
    private FIELD_TYPE greaterOrEqualThan;
    private FIELD_TYPE lessOrEqualThan;
    private FIELD_TYPE betweenFrom;
    private FIELD_TYPE betweenTo;
    private FIELD_TYPE notBetweenFrom;
    private FIELD_TYPE notBetweenTo;

    public FIELD_TYPE getEquals() {
        return equals;
    }

    public Filter<FIELD_TYPE> setEquals(FIELD_TYPE equals) {
        this.equals = equals;
        return this;
    }

    public FIELD_TYPE getNotEquals() {
        return notEquals;
    }

    public Filter<FIELD_TYPE> setNotEquals(FIELD_TYPE notEquals) {
        this.notEquals = notEquals;
        return this;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public Filter<FIELD_TYPE> setNullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public List<FIELD_TYPE> getIn() {
        return in;
    }

    public Filter<FIELD_TYPE> setIn(List<FIELD_TYPE> in) {
        this.in = in;
        return this;
    }

    public List<FIELD_TYPE> getNotIn() {
        return notIn;
    }

    public Filter<FIELD_TYPE> setNotIn(List<FIELD_TYPE> notIn) {
        this.notIn = notIn;
        return this;
    }

    public FIELD_TYPE getGreaterThan() {
        return greaterThan;
    }

    public Filter<FIELD_TYPE> setGreaterThan(FIELD_TYPE greaterThan) {
        this.greaterThan = greaterThan;
        return this;
    }

    public FIELD_TYPE getGreaterOrEqualThan() {
        return greaterOrEqualThan;
    }

    public Filter<FIELD_TYPE> setGreaterOrEqualThan(FIELD_TYPE greaterOrEqualThan) {
        this.greaterOrEqualThan = greaterOrEqualThan;
        return this;
    }

    public FIELD_TYPE getLessThan() {
        return lessThan;
    }

    public Filter<FIELD_TYPE> setLessThan(FIELD_TYPE lessThan) {
        this.lessThan = lessThan;
        return this;
    }

    public FIELD_TYPE getLessOrEqualThan() {
        return lessOrEqualThan;
    }

    public Filter<FIELD_TYPE> setLessOrEqualThan(FIELD_TYPE lessOrEqualThan) {
        this.lessOrEqualThan = lessOrEqualThan;
        return this;
    }

    public FIELD_TYPE getBetweenFrom() {
        return betweenFrom;
    }

    public Filter<FIELD_TYPE> setBetweenFrom(FIELD_TYPE betweenFrom) {
        this.betweenFrom = betweenFrom;
        return this;
    }

    public FIELD_TYPE getBetweenTo() {
        return betweenTo;
    }

    public Filter<FIELD_TYPE> setBetweenTo(FIELD_TYPE betweenTo) {
        this.betweenTo = betweenTo;
        return this;
    }

    public FIELD_TYPE getNotBetweenFrom() {
        return notBetweenFrom;
    }

    public Filter<FIELD_TYPE> setNotBetweenFrom(FIELD_TYPE notBetweenFrom) {
        this.notBetweenFrom = notBetweenFrom;
        return this;
    }

    public FIELD_TYPE getNotBetweenTo() {
        return notBetweenTo;
    }

    public Filter<FIELD_TYPE> setNotBetweenTo(FIELD_TYPE notBetweenTo) {
        this.notBetweenTo = notBetweenTo;
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
        final Filter<?> filter = (Filter<?>) o;
        return Objects.equals(equals, filter.equals) &&
                Objects.equals(notEquals, filter.notEquals) &&
                Objects.equals(nullable, filter.nullable) &&
                Objects.equals(in, filter.in) &&
                Objects.equals(notIn, filter.notIn) &&
                Objects.equals(greaterThan, filter.greaterThan) &&
                Objects.equals(lessThan, filter.lessThan) &&
                Objects.equals(greaterOrEqualThan, filter.greaterOrEqualThan) &&
                Objects.equals(lessOrEqualThan, filter.lessOrEqualThan) &&
                Objects.equals(betweenFrom, filter.betweenFrom) &&
                Objects.equals(betweenTo, filter.betweenTo) &&
                Objects.equals(notBetweenFrom, filter.notBetweenFrom) &&
                Objects.equals(notBetweenTo, filter.notBetweenTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equals, notEquals, nullable, in, notIn, greaterThan, lessThan,
                greaterOrEqualThan, lessOrEqualThan, betweenFrom, betweenTo, notBetweenFrom, notBetweenTo);
    }

    @Override
    public String toString() {
        return getFilterName() + " ["
                + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
                + (getNotEquals() != null ? "notEquals=" + getNotEquals() + ", " : "")
                + (getNullable() != null ? "nullable=" + getNullable() : "")
                + (getIn() != null ? "in=" + getIn() + ", " : "")
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

    protected String getFilterName() {
        return this.getClass().getSimpleName();
    }

}
