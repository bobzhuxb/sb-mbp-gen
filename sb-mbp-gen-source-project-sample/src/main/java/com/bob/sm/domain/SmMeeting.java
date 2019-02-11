package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 会议
 */
@Data
@TableName(value = "sm_meeting")
public class SmMeeting extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String meetingNo;    // 会议编号

    private String name;    // 会议名称

    private String ksCode;    // 所属科室代码
    
    @TableField(exist = false)
    private String ksValue;    // 所属科室值（数据字典值）

    private Long insertUserId;    // 创建者用户ID

    private Long organizerId;    // 组织者ID

    private Long contractorId;    // 联系人ID

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SmMeeting.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmMeeting smMeeting = (SmMeeting) o;
        if (smMeeting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smMeeting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmMeeting{" +
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
