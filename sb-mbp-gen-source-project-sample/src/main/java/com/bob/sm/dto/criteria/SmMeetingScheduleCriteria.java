package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议日程 条件过滤器
 */
@ApiModel(description = "会议日程")
public class SmMeetingScheduleCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "ValidDate")
    private StringFilter startTime;    // ValidDate

    @ApiModelProperty(value = "ValidDate")
    private StringFilter entTime;    // ValidDate

    @ApiModelProperty(value = "内容")
    private StringFilter content;    // 内容

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "会议ID")
    private LongFilter smMeetingId;    // 会议ID

    private SmMeetingCriteria smMeeting;    // 会议

    public SmMeetingScheduleCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }
    
    public StringFilter getEntTime() {
        return entTime;
    }

    public void setEntTime(StringFilter entTime) {
        this.entTime = entTime;
    }
    
    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
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
        final SmMeetingScheduleCriteria that = (SmMeetingScheduleCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(entTime, that.entTime)
            && Objects.equals(content, that.content)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , startTime
        , entTime
        , content
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmMeetingScheduleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (entTime != null ? "entTime=" + entTime + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
