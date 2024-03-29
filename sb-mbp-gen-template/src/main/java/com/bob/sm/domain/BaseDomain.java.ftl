package ${packageName}.domain;

import ${packageName}.annotation.GenComment;
import java.io.Serializable;

/**
 * 基础Domain类
 * @author Bob
 */
public class BaseDomain implements Serializable {

    @GenComment("主键ID")
    private String id;

    @GenComment("创建者用户ID")
    private String insertUserId;

    @GenComment("操作者用户ID")
    private String operateUserId;

    @GenComment("插入时间")
    private String insertTime;

    @GenComment("更新时间")
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
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
    public static final String _insertTime = "insert_time";    // 创建时间
    public static final String _updateTime = "update_time";    // 最后更新时间
}
