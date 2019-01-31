package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议详细 条件过滤器
 */
public class SmMeetingDetailCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter detailInfo;    // 详细描述

    private IntegerFilter detailViews;    // 详情浏览次数

    private LongFilter smMeetingId;    // 会议ID

    public SmMeetingDetailCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(StringFilter detailInfo) {
        this.detailInfo = detailInfo;
    }
    
    public IntegerFilter getDetailViews() {
        return detailViews;
    }

    public void setDetailViews(IntegerFilter detailViews) {
        this.detailViews = detailViews;
    }

    public LongFilter getSmMeetingId() {
        return smMeetingId;
    }
	
	public void setSmMeetingId(LongFilter smMeetingId) {
        this.smMeetingId = smMeetingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmMeetingDetailCriteria that = (SmMeetingDetailCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(detailInfo, that.detailInfo) &&
            Objects.equals(detailViews, that.detailViews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        detailInfo,
        detailViews
        );
    }

    @Override
    public String toString() {
        return "SmMeetingDetailCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (detailInfo != null ? "detailInfo=" + detailInfo + ", " : "") +
                (detailViews != null ? "detailViews=" + detailViews + ", " : "") +
            "}";
    }

}
