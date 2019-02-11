package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议详细 条件过滤器
 */
@ApiModel(description = "会议详细")
public class SmMeetingDetailCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "详细描述")
    private StringFilter detailInfo;    // 详细描述

    @ApiModelProperty(value = "详情浏览次数")
    private IntegerFilter detailViews;    // 详情浏览次数

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "会议ID")
    private LongFilter smMeetingId;    // 会议ID

    private SmMeetingCriteria smMeeting;    // 会议

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
    
    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LongFilter getSmMeetingId() {
        return smMeetingId;
    }
	
	public void setSmMeetingId(LongFilter smMeetingId) {
        this.smMeetingId = smMeetingId;
    }

    public SmMeetingCriteria getSmMeeting() {
        return smMeeting;
    }

    public void setSmMeeting(SmMeetingCriteria smMeeting) {
        this.smMeeting = smMeeting;
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
            Objects.equals(id, that.id)
            && Objects.equals(detailInfo, that.detailInfo)
            && Objects.equals(detailViews, that.detailViews)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , detailInfo
        , detailViews
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmMeetingDetailCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (detailInfo != null ? "detailInfo=" + detailInfo + ", " : "") +
                (detailViews != null ? "detailViews=" + detailViews + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
