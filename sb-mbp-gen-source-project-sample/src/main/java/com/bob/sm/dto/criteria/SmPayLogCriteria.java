package com.bob.sm.dto.criteria;

import com.bob.sm.dto.criteria.filter.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 支付日志 条件过滤器
 */
@ApiModel(description = "支付日志")
public class SmPayLogCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    @ApiModelProperty(value = "接口类型")
    private StringFilter interType;    // 接口类型

    @ApiModelProperty(value = "传入参数")
    private StringFilter param;    // 传入参数

    @ApiModelProperty(value = "返回结果")
    private StringFilter result;    // 返回结果

    @ApiModelProperty(value = "创建者用户ID")
    private LongFilter insertUserId;    // 创建者用户ID

    public SmPayLogCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public StringFilter getInterType() {
        return interType;
    }

    public void setInterType(StringFilter interType) {
        this.interType = interType;
    }
    
    public StringFilter getParam() {
        return param;
    }

    public void setParam(StringFilter param) {
        this.param = param;
    }
    
    public StringFilter getResult() {
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
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
        final SmPayLogCriteria that = (SmPayLogCriteria) o;
        return
            Objects.equals(id, that.id)
            && Objects.equals(interType, that.interType)
            && Objects.equals(param, that.param)
            && Objects.equals(result, that.result)
            && Objects.equals(insertUserId, that.insertUserId)
            ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id
        , interType
        , param
        , result
        , insertUserId
        );
    }

    @Override
    public String toString() {
        return "SmPayLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (interType != null ? "interType=" + interType + ", " : "") +
                (param != null ? "param=" + param + ", " : "") +
                (result != null ? "result=" + result + ", " : "") +
                (insertUserId != null ? "insertUserId=" + insertUserId + ", " : "") +
                "}";
    }

}
