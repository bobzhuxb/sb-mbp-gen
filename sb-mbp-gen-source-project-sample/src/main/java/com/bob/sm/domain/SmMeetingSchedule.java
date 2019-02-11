package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 会议日程
 */
@Data
@TableName(value = "sm_meeting_schedule")
public class SmMeetingSchedule extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String startTime;    // ValidDate

    private String entTime;    // ValidDate

    private String content;    // 内容

    private Long insertUserId;    // 创建者用户ID

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
        return (SmMeetingSchedule.class.getAnnotation(TableName.class)).value();
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
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
