package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议 条件过滤器
 */
@ApiModel(description = "会议")
public class SmMeetingCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "会议编号")
    private StringFilter meetingNo;    // 会议编号

    @ApiModelProperty(value = "会议名称")
    private StringFilter name;    // 会议名称

    @ApiModelProperty(value = "所属科室代码")
    private StringFilter ksCode;    // 所属科室代码

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "组织者ID")
    private LongFilter organizerId;    // 组织者ID

    @ApiModelProperty(value = "联系人ID")
    private LongFilter contractorId;    // 联系人ID

    private SmPersonCriteria organizer;    // 组织者

    private SmPersonCriteria contractor;    // 联系人

    public SmMeetingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(StringFilter meetingNo) {
        this.meetingNo = meetingNo;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }
    
    public StringFilter getKsCode() {
        return ksCode;
    }

    public void setKsCode(StringFilter ksCode) {
        this.ksCode = ksCode;
    }
    
    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LongFilter getOrganizerId() {
        return organizerId;
    }
	
	public void setOrganizerId(LongFilter organizerId) {
        this.organizerId = organizerId;
    }

    public LongFilter getContractorId() {
        return contractorId;
    }
	
	public void setContractorId(LongFilter contractorId) {
        this.contractorId = contractorId;
    }

    public SmPersonCriteria getOrganizer() {
        return organizer;
    }

    public void setOrganizer(SmPersonCriteria organizer) {
        this.organizer = organizer;
    }

    public SmPersonCriteria getContractor() {
        return contractor;
    }

    public void setContractor(SmPersonCriteria contractor) {
        this.contractor = contractor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmMeetingCriteria that = (SmMeetingCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(meetingNo, that.meetingNo)
            && Objects.equals(name, that.name)
            && Objects.equals(ksCode, that.ksCode)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , meetingNo
        , name
        , ksCode
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmMeetingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (meetingNo != null ? "meetingNo=" + meetingNo + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (ksCode != null ? "ksCode=" + ksCode + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
