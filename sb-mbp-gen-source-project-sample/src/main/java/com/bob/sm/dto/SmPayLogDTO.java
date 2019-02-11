package com.bob.sm.dto;

import com.bob.sm.annotation.*;
import com.bob.sm.annotation.validation.*;
import com.bob.sm.domain.*;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * 支付日志
 */
public class SmPayLogDTO extends BaseDTO {

    private Long id;

    private String interType;    // 接口类型

    private String param;    // 传入参数

    private String result;    // 返回结果

    private Long insertUserId;    // 创建者用户ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public String getInterType() {
        return interType;
    }

    public void setInterType(String interType) {
        this.interType = interType;
    }
	
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
	
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
	
    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
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

        SmPayLogDTO smPayLogDTO = (SmPayLogDTO) o;
        if (smPayLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smPayLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmPayLogDTO{" +
            "id=" + getId() +
            ", interType='" + getInterType() + "'" +
            ", param='" + getParam() + "'" +
            ", result='" + getResult() + "'" +
            ", insertUserId=" + getInsertUserId() +
            ", operateUserId=" + getOperateUserId() +
            ", insertTime='" + getInsertTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
