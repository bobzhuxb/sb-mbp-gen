package ${packageName}.domain;

import ${packageName}.domain.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议日程
 */
@ApiModel(description = "会议日程")
@Data
public class SmMeetingSchedule extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "开始日期")
    private String startTime;    // 开始日期

    @ApiModelProperty(value = "结束日期")
    private String entTime;    // 结束日期

    @ApiModelProperty(value = "内容")
    private String content;    // 内容

    private Long smMeetingId;    // 会议ID

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeetingSchedule smMeetingSchedule = (SmMeetingSchedule) o;
        if (smMeetingSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingSchedule{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", entTime='" + getEntTime() + "'" +
            ", content='" + getContent() + "'" +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
