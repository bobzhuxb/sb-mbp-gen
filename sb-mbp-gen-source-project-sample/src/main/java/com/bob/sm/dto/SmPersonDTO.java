package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

/**
 * 工作人员
 */
public class SmPersonDTO extends BaseDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;    // 姓名

    @NotBlank
    @ValidCell
    private String cell;    // 手机号

    @NotBlank
    @Size(min = 1, max = 255)
    private String nationCode;    // 民族代码

    private String nationValue;    // 民族值（数据字典值）

    @NotBlank
    @Size(min = 1, max = 255)
    private String countryCode;    // 国家代码

    private String countryValue;    // 国家值（数据字典值）

    private Long insertUserId;    // 创建者用户ID
    
	///////////////////////// 附加关联属性 /////////////////////////

    private List<SmMeetingDTO> asmMeetingList;    // 会议列表

    private List<SmMeetingDTO> bsmMeetingList;    // 会议列表

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
	
    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }
	
    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }
    
    public String getNationValue() {
        return nationValue;
    }

    public void setNationValue(String nationValue) {
        this.nationValue = nationValue;
    }
	
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public String getCountryValue() {
        return countryValue;
    }

    public void setCountryValue(String countryValue) {
        this.countryValue = countryValue;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public List<SmMeetingDTO> getAsmMeetingList() {
        return asmMeetingList;
    }

    public void setAsmMeetingList(List<SmMeetingDTO> asmMeetingList) {
        this.asmMeetingList = asmMeetingList;
    }

    public List<SmMeetingDTO> getBsmMeetingList() {
        return bsmMeetingList;
    }

    public void setBsmMeetingList(List<SmMeetingDTO> bsmMeetingList) {
        this.bsmMeetingList = bsmMeetingList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmPersonDTO smPersonDTO = (SmPersonDTO) o;
        if (smPersonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smPersonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmPersonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cell='" + getCell() + "'" +
            ", nationCode='" + getNationCode() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
