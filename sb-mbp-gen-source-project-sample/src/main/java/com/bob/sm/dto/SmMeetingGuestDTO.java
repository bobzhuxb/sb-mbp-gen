package com.bob.sm.dto;

import java.util.Objects;

/**
 * 会议嘉宾
 */
public class SmMeetingGuestDTO extends BaseDTO {

    private Long id;

    private String name;    // 姓名

    private String desc;    // 描述

    private Long smMeetingId;    // 会议ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmMeetingDTO smMeeting;    // 会议

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

        SmMeetingGuestDTO smMeetingGuestDTO = (SmMeetingGuestDTO) o;
        if (smMeetingGuestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingGuestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingGuestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
			", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
