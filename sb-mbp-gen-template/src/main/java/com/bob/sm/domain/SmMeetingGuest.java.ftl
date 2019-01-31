package ${packageName}.domain;

import ${packageName}.domain.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议嘉宾
 */
@ApiModel(description = "会议嘉宾")
@Data
public class SmMeetingGuest extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;    // 姓名

    @ApiModelProperty(value = "描述")
    private String desc;    // 描述

    private Long smMeetingId;    // 会议ID

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeetingGuest smMeetingGuest = (SmMeetingGuest) o;
        if (smMeetingGuest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingGuest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingGuest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
