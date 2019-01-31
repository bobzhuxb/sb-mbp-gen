package com.bob.sm.dto;

import java.util.List;
import com.bob.sm.domain.SmMeetingDetail;
import com.bob.sm.domain.SmMeetingSchedule;
import com.bob.sm.domain.SmMeetingGuest;
import java.util.Objects;

/**
 * 会议
 */
public class SmMeetingDTO extends BaseDTO {

    private Long id;

    private String meetingNo;    // 会议编号

    private String name;    // 会议名称
    
	///////////////////////// 附加关联属性 /////////////////////////

    private List<SmMeetingDetail> smMeetingDetailList;    // 会议详情列表

    private List<SmMeetingSchedule> smMeetingScheduleList;    // 会议日程列表

    private List<SmMeetingGuest> smMeetingGuestList;    // 会议嘉宾列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(String meetingNo) {
        this.meetingNo = meetingNo;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SmMeetingDetail> getSmMeetingDetailList() {
        return smMeetingDetailList;
    }

    public void setSmMeetingDetailList(List<SmMeetingDetail> smMeetingDetailList) {
        this.smMeetingDetailList = smMeetingDetailList;
    }

    public List<SmMeetingSchedule> getSmMeetingScheduleList() {
        return smMeetingScheduleList;
    }

    public void setSmMeetingScheduleList(List<SmMeetingSchedule> smMeetingScheduleList) {
        this.smMeetingScheduleList = smMeetingScheduleList;
    }

    public List<SmMeetingGuest> getSmMeetingGuestList() {
        return smMeetingGuestList;
    }

    public void setSmMeetingGuestList(List<SmMeetingGuest> smMeetingGuestList) {
        this.smMeetingGuestList = smMeetingGuestList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmMeetingDTO smMeetingDTO = (SmMeetingDTO) o;
        if (smMeetingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingDTO{" +
            "id=" + getId() +
            ", meetingNo='" + getMeetingNo() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
