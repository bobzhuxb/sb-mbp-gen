package ${packageName}.dto.criteria;

import ${packageName}.dto.criteria.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议 条件过滤器
 */
public class SmMeetingCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter meetingNo;    // 会议编号

    private StringFilter name;    // 会议名称

    private LongFilter smMeetingDetailId;    // 会议详情ID

    private LongFilter smMeetingScheduleId;    // 会议日程ID

    private LongFilter smMeetingGuestId;    // 会议嘉宾ID

    public SmMeetingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(StringFilter meetingNo) {
        this.meetingNo = meetingNo;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getSmMeetingDetailId() {
        return smMeetingDetailId;
    }
	
	public void setSmMeetingDetailId(LongFilter smMeetingDetailId) {
        this.smMeetingDetailId = smMeetingDetailId;
    }

    public LongFilter getSmMeetingScheduleId() {
        return smMeetingScheduleId;
    }
	
	public void setSmMeetingScheduleId(LongFilter smMeetingScheduleId) {
        this.smMeetingScheduleId = smMeetingScheduleId;
    }

    public LongFilter getSmMeetingGuestId() {
        return smMeetingGuestId;
    }
	
	public void setSmMeetingGuestId(LongFilter smMeetingGuestId) {
        this.smMeetingGuestId = smMeetingGuestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmMeetingCriteria that = (SmMeetingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(meetingNo, that.meetingNo) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        meetingNo,
        name
        );
    }

    @Override
    public String toString() {
        return "SmMeetingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (meetingNo != null ? "meetingNo=" + meetingNo + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
            "}";
    }

}
