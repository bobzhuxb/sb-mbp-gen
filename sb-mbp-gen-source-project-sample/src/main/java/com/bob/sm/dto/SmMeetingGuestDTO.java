package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 会议嘉宾
 */
public class SmMeetingGuestDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 姓名

    @Size(min = 1, max = 255)
    private String descr;    // 描述

    private Long insertUserId;    // 创建者用户ID

    private Long smMeetingId;    // 会议ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmMeetingDTO smMeeting;    // 会议

    private List<SmMeetingGuestPhotoDTO> smMeetingGuestPhotoList;    // 嘉宾照片列表

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

    public SmMeetingDTO getSmMeeting() {
        return smMeeting;
    }

    public void setSmMeeting(SmMeetingDTO smMeeting) {
        this.smMeeting = smMeeting;
    }

    public List<SmMeetingGuestPhotoDTO> getSmMeetingGuestPhotoList() {
        return smMeetingGuestPhotoList;
    }

    public void setSmMeetingGuestPhotoList(List<SmMeetingGuestPhotoDTO> smMeetingGuestPhotoList) {
        this.smMeetingGuestPhotoList = smMeetingGuestPhotoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmMeetingGuestDTO smMeetingGuestDTO = (SmMeetingGuestDTO) o;
        if (smMeetingGuestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingGuestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingGuestDTO{" +
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
