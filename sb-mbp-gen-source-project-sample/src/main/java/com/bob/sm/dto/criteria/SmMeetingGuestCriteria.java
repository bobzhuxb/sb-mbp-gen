package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议嘉宾 条件过滤器
 */
@ApiModel(description = "会议嘉宾")
public class SmMeetingGuestCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "姓名")
    private StringFilter name;    // 姓名

    @ApiModelProperty(value = "描述")
    private StringFilter descr;    // 描述

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "会议ID")
    private LongFilter smMeetingId;    // 会议ID

    private SmMeetingCriteria smMeeting;    // 会议

    public SmMeetingGuestCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }
    
    public StringFilter getDescr() {
        return descr;
    }

    public void setDescr(StringFilter descr) {
        this.descr = descr;
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
        final SmMeetingGuestCriteria that = (SmMeetingGuestCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(descr, that.descr)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , name
        , descr
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmMeetingGuestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (descr != null ? "descr=" + descr + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
