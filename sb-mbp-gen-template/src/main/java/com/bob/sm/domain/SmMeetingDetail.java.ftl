package ${packageName}.domain;

import ${packageName}.domain.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议详细
 */
@ApiModel(description = "会议详细")
@Data
public class SmMeetingDetail extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "详细描述")
    private String detailInfo;    // 详细描述

    @ApiModelProperty(value = "详情浏览次数")
    private Integer detailViews;    // 详情浏览次数

    private Long smMeetingId;    // 会议ID

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeetingDetail smMeetingDetail = (SmMeetingDetail) o;
        if (smMeetingDetail.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingDetail.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingDetail{" +
            "id=" + getId() +
            ", detailInfo='" + getDetailInfo() + "'" +
            ", detailViews=" + getDetailViews() +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
