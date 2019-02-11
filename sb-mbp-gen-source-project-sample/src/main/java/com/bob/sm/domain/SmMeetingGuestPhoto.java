package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 会议嘉宾照片
 */
@Data
@TableName(value = "sm_meeting_guest_photo")
public class SmMeetingGuestPhoto extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String relativePath;    // 照片相对路径

    private Long insertUserId;    // 创建者用户ID

    private Long smMeetingGuestId;    // 会议嘉宾ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getSmMeetingGuestId() {
        return smMeetingGuestId;
    }

    public void setSmMeetingGuestId(Long smMeetingGuestId) {
        this.smMeetingGuestId = smMeetingGuestId;
    }
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SmMeetingGuestPhoto.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeetingGuestPhoto smMeetingGuestPhoto = (SmMeetingGuestPhoto) o;
        if (smMeetingGuestPhoto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingGuestPhoto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingGuestPhoto{" +
            "id=" + getId() +
            ", relativePath='" + getRelativePath() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", smMeetingGuestId=" + getSmMeetingGuestId() +
            "}";
    }
}
