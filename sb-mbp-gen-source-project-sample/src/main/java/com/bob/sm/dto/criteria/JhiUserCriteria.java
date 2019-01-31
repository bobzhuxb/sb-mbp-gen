package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.Filter;
import com.bob.sm.dto.criteria.filter.LongFilter;
import com.bob.sm.dto.criteria.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the JhiUser entity. This class is used in JhiUserResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bm-messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JhiUserCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    public JhiUserCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JhiUserCriteria that = (JhiUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        login
        );
    }

    @Override
    public String toString() {
        return "JhiUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
            "}";
    }

}
