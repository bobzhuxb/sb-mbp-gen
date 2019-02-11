package com.bob.sm.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * 支付日志
 */
@Data
@TableName(value = "sm_pay_log")
public class SmPayLog extends BaseDomain {

    private static final long serialVersionUID = 1L;

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
	
    /**
     * 获取表名字
     */
    public static String getTableName() {
        return (SmPayLog.class.getAnnotation(TableName.class)).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmPayLog smPayLog = (SmPayLog) o;
        if (smPayLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smPayLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmPayLog{" +
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
