package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议嘉宾照片 条件过滤器
 */
@ApiModel(description = "会议嘉宾照片")
public class SmMeetingGuestPhotoCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "照片相对路径")
    private StringFilter relativePath;    // 照片相对路径

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "会议嘉宾ID")
    private LongFilter smMeetingGuestId;    // 会议嘉宾ID

    private SmMeetingGuestCriteria smMeetingGuest;    // 会议嘉宾

    public SmMeetingGuestPhotoCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(StringFilter relativePath) {
        this.relativePath = relativePath;
    }
    
    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LongFilter getSmMeetingGuestId() {
        return smMeetingGuestId;
    }
	
	public void setSmMeetingGuestId(LongFilter smMeetingGuestId) {
        this.smMeetingGuestId = smMeetingGuestId;
    }

    public SmMeetingGuestCriteria getSmMeetingGuest() {
        return smMeetingGuest;
    }

    public void setSmMeetingGuest(SmMeetingGuestCriteria smMeetingGuest) {
        this.smMeetingGuest = smMeetingGuest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmMeetingGuestPhotoCriteria that = (SmMeetingGuestPhotoCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(relativePath, that.relativePath)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , relativePath
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmMeetingGuestPhotoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (relativePath != null ? "relativePath=" + relativePath + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
