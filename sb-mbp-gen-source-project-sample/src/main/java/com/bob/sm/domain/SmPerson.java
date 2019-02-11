package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 工作人员
 */
@Data
@TableName(value = "sm_person")
public class SmPerson extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;    // 姓名

    private String cell;    // 手机号

    private String nationCode;    // 民族代码
    
    @TableField(exist = false)
    private String nationValue;    // 民族值（数据字典值）

    private String countryCode;    // 国家代码
    
    @TableField(exist = false)
    private String countryValue;    // 国家值（数据字典值）

    private Long insertUserId;    // 创建者用户ID

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SmPerson.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmPerson smPerson = (SmPerson) o;
        if (smPerson.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smPerson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmPerson{" +
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
