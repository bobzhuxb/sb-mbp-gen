package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.Filter;
import com.bob.sm.dto.criteria.filter.LongFilter;
import com.bob.sm.dto.criteria.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the JhiUserAuthority entity. This class is used in JhiUserAuthorityResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bm-messages?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JhiUserAuthorityCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter userId;

    private StringFilter authorityName;

    public JhiUserAuthorityCriteria() {
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(StringFilter authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JhiUserAuthorityCriteria that = (JhiUserAuthorityCriteria) o;
        return
            Objects.equals(userId, that.userId) &&
            Objects.equals(authorityName, that.authorityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        userId,
        authorityName
        );
    }

    @Override
    public String toString() {
        return "JhiUserCriteria{" +
                (userId != null ? "userId=" + userId + ", " : "") +
                (authorityName != null ? "authorityName=" + authorityName + ", " : "") +
            "}";
    }

}
