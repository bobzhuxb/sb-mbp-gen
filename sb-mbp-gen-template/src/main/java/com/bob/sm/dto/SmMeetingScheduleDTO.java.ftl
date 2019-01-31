package ${packageName}.dto;

import java.util.Objects;

/**
 * 会议日程
 */
public class SmMeetingScheduleDTO extends BaseDTO {

    private Long id;

    private String startTime;    // 开始日期

    private String entTime;    // 结束日期

    private String content;    // 内容

    private Long smMeetingId;    // 会议ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmMeetingDTO smMeeting;    // 会议

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
	
    public String getEntTime() {
        return entTime;
    }

    public void setEntTime(String entTime) {
        this.entTime = entTime;
    }
	
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

        SmMeetingScheduleDTO smMeetingScheduleDTO = (SmMeetingScheduleDTO) o;
        if (smMeetingScheduleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingScheduleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingScheduleDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", entTime='" + getEntTime() + "'" +
            ", content='" + getContent() + "'" +
			", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
