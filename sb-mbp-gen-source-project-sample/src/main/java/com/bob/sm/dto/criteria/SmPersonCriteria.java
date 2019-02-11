package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 工作人员 条件过滤器
 */
@ApiModel(description = "工作人员")
public class SmPersonCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "姓名")
    private StringFilter name;    // 姓名

    @ApiModelProperty(value = "手机号")
    private StringFilter cell;    // 手机号

    @ApiModelProperty(value = "民族代码")
    private StringFilter nationCode;    // 民族代码

    @ApiModelProperty(value = "国家代码")
    private StringFilter countryCode;    // 国家代码

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    public SmPersonCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }
    
    public StringFilter getCell() {
        return cell;
    }

    public void setCell(StringFilter cell) {
        this.cell = cell;
    }
    
    public StringFilter getNationCode() {
        return nationCode;
    }

    public void setNationCode(StringFilter nationCode) {
        this.nationCode = nationCode;
    }
    
    public StringFilter getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(StringFilter countryCode) {
        this.countryCode = countryCode;
    }
    
    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmPersonCriteria that = (SmPersonCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(cell, that.cell)
            && Objects.equals(nationCode, that.nationCode)
            && Objects.equals(countryCode, that.countryCode)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , name
        , cell
        , nationCode
        , countryCode
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmPersonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (cell != null ? "cell=" + cell + ", " : "") +
                (nationCode != null ? "nationCode=" + nationCode + ", " : "") +
                (countryCode != null ? "countryCode=" + countryCode + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
