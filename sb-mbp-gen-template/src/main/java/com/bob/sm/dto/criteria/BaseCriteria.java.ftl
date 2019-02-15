package ${packageName}.dto.criteria;

import ${packageName}.dto.criteria.filter.*;

import java.util.List;

public class BaseCriteria {

    private NothingFilter nothing;    // 无任何操作

    private LongFilter insertUserId;    // 创建者用户ID

    private LongFilter operateUserId;    // 操作者用户ID

    private StringFilter insertTime;    // 插入时间

    private StringFilter updateTime;    // 更新时间

    private String orderBy;     // 排序属性（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）

    private List<String> associationNameList;   // 关联属性获取（列出的属性都会关联查询）

    private List<String> dictionaryNameList;    // 关联数据字典值获取（列出的属性都会关联查询）

    private SystemUserCriteria insertUser;    // 创建者用户

    private SystemUserCriteria operateUser;    // 操作者用户

    public NothingFilter getNothing() {
        return nothing;
    }

    public void setNothing(NothingFilter nothing) {
        this.nothing = nothing;
    }

    public LongFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(LongFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LongFilter getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(LongFilter operateUserId) {
        this.operateUserId = operateUserId;
    }

    public StringFilter getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(StringFilter insertTime) {
        this.insertTime = insertTime;
    }

    public StringFilter getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(StringFilter updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<String> getAssociationNameList() {
        return associationNameList;
    }

    public void setAssociationNameList(List<String> associationNameList) {
        this.associationNameList = associationNameList;
    }

    public List<String> getDictionaryNameList() {
        return dictionaryNameList;
    }

    public void setDictionaryNameList(List<String> dictionaryNameList) {
        this.dictionaryNameList = dictionaryNameList;
    }

    public SystemUserCriteria getInsertUser() {
        return insertUser;
    }

    public void setInsertUser(SystemUserCriteria insertUser) {
        this.insertUser = insertUser;
    }

    public SystemUserCriteria getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(SystemUserCriteria operateUser) {
        this.operateUser = operateUser;
    }
}
