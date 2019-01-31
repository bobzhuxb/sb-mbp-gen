package com.bob.sm.dto;

import java.util.Objects;

/**
 * 会议详细
 */
public class SmMeetingDetailDTO extends BaseDTO {

    private Long id;

    private String detailInfo;    // 详细描述

    private Integer detailViews;    // 详情浏览次数

    private Long smMeetingId;    // 会议ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmMeetingDTO smMeeting;    // 会议

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
	
    public Integer getDetailViews() {
        return detailViews;
    }

    public void setDetailViews(Integer detailViews) {
        this.detailViews = detailViews;
    }

    public Long getSmMeetingId() {
        return smMeetingId;
    }

    public void setSmMeetingId(Long smMeetingId) {
        this.smMeetingId = smMeetingId;
    }

    public SmMeetingDTO getSmMeeting() {
        return smMeeting;
    }

    public void setSmMeeting(SmMeetingDTO smMeeting) {
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

        SmMeetingDetailDTO smMeetingDetailDTO = (SmMeetingDetailDTO) o;
        if (smMeetingDetailDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingDetailDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingDetailDTO{" +
            "id=" + getId() +
            ", detailInfo='" + getDetailInfo() + "'" +
            ", detailViews=" + getDetailViews() +
			", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
