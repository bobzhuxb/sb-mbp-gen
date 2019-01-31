package ${packageName}.domain;

import ${packageName}.domain.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议
 */
@ApiModel(description = "会议")
@Data
public class SmMeeting extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "会议编号")
    private String meetingNo;    // 会议编号

    @ApiModelProperty(value = "会议名称")
    private String name;    // 会议名称

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeeting smMeeting = (SmMeeting) o;
        if (smMeeting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeeting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeeting{" +
            "id=" + getId() +
            ", meetingNo='" + getMeetingNo() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
