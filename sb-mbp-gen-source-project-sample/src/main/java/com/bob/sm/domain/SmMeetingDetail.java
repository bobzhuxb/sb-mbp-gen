package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 会议详细
 */
@Data
@TableName(value = "sm_meeting_detail")
public class SmMeetingDetail extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String detailInfo;    // 详细描述

    private Integer detailViews;    // 详情浏览次数

    private Long insertUserId;    // 创建者用户ID

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

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getSmMeetingId() {
        return smMeetingId;
    }

    public void setSmMeetingId(Long smMeetingId) {
        this.smMeetingId = smMeetingId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SmMeetingDetail.class.getAnnotation(TableName.class)).value();
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
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
