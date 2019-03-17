package ${packageName}.domain;

import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

public class BaseDomain implements Serializable {

    @ApiModelProperty(value = "创建者用户ID")
    private Long insertUserId;    // 创建者用户ID

    @ApiModelProperty(value = "操作者用户ID")
    private Long operateUserId;    // 操作者用户ID

    @ApiModelProperty(value = "插入时间")
    private String insertTime;    // 插入时间

    @ApiModelProperty(value = "更新时间")
    private String updateTime;    // 更新时间

    public Long getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Long insertUserId) {
        this.insertUserId = insertUserId;
    }

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    // 数据库列名
    public static final String _insertUserId = "insert_user_id";    // 创建者用户ID
    public static final String _operateUserId = "operate_user_id";    // 最后更新者用户ID
}
