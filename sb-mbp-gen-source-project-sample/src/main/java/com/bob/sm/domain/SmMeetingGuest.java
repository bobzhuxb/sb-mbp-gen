package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 会议嘉宾
 */
@Data
@TableName(value = "sm_meeting_guest")
public class SmMeetingGuest extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;    // 姓名

    private String descr;    // 描述

    private Long insertUserId;    // 创建者用户ID

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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
        return (SmMeetingGuest.class.getAnnotation(TableName.class)).value();
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
            ", descr='" + getDescr() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", smMeetingId=" + getSmMeetingId() +
            "}";
    }
}
