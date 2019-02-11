package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 会议嘉宾照片
 */
public class SmMeetingGuestPhotoDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String relativePath;    // 照片相对路径

    private Long insertUserId;    // 创建者用户ID

    private Long smMeetingGuestId;    // 会议嘉宾ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmMeetingGuestDTO smMeetingGuest;    // 会议嘉宾

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

    public SmMeetingGuestDTO getSmMeetingGuest() {
        return smMeetingGuest;
    }

    public void setSmMeetingGuest(SmMeetingGuestDTO smMeetingGuest) {
        this.smMeetingGuest = smMeetingGuest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmMeetingGuestPhotoDTO smMeetingGuestPhotoDTO = (SmMeetingGuestPhotoDTO) o;
        if (smMeetingGuestPhotoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingGuestPhotoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingGuestPhotoDTO{" +
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
