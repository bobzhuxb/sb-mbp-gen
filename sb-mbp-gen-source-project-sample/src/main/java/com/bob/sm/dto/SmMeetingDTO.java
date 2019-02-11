package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 会议
 */
public class SmMeetingDTO extends BaseDTO {

    private Long id;

    @RestFieldAllow(allowSet = false)
    private String meetingNo;    // 会议编号

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 会议名称

    @NotBlank
    @Size(min = 1, max = 255)
    private String ksCode;    // 所属科室代码

    private String ksValue;    // 所属科室值（数据字典值）

    private Long insertUserId;    // 创建者用户ID

    private Long organizerId;    // 组织者ID

    private Long contractorId;    // 联系人ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private SmPersonDTO organizer;    // 组织者

    private SmPersonDTO contractor;    // 联系人

    private SmMeetingDetailDTO smMeetingDetail;    // 会议详情

    private List<SmMeetingScheduleDTO> smMeetingScheduleList;    // 会议日程列表

    private List<SmMeetingGuestDTO> smMeetingGuestList;    // 会议嘉宾列表

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
	
    public String getKsCode() {
        return ksCode;
    }

    public void setKsCode(String ksCode) {
        this.ksCode = ksCode;
    }
    
    public String getKsValue() {
        return ksValue;
    }

    public void setKsValue(String ksValue) {
        this.ksValue = ksValue;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public SmPersonDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(SmPersonDTO organizer) {
        this.organizer = organizer;
    }

    public SmPersonDTO getContractor() {
        return contractor;
    }

    public void setContractor(SmPersonDTO contractor) {
        this.contractor = contractor;
    }

    public SmMeetingDetailDTO getSmMeetingDetail() {
        return smMeetingDetail;
    }

    public void setSmMeetingDetail(SmMeetingDetailDTO smMeetingDetail) {
        this.smMeetingDetail = smMeetingDetail;
    }

    public List<SmMeetingScheduleDTO> getSmMeetingScheduleList() {
        return smMeetingScheduleList;
    }

    public void setSmMeetingScheduleList(List<SmMeetingScheduleDTO> smMeetingScheduleList) {
        this.smMeetingScheduleList = smMeetingScheduleList;
    }

    public List<SmMeetingGuestDTO> getSmMeetingGuestList() {
        return smMeetingGuestList;
    }

    public void setSmMeetingGuestList(List<SmMeetingGuestDTO> smMeetingGuestList) {
        this.smMeetingGuestList = smMeetingGuestList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmMeetingDTO smMeetingDTO = (SmMeetingDTO) o;
        if (smMeetingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeetingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeetingDTO{" +
            "id=" + getId() +
            ", meetingNo='" + getMeetingNo() + "'" +
            ", name='" + getName() + "'" +
            ", ksCode='" + getKsCode() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
			", organizerId=" + getOrganizerId() +
			", contractorId=" + getContractorId() +
            "}";
    }
}
