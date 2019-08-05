package ${packageName}.dto.criteria;

import ${packageName}.annotation.RestFieldAllow;
import ${packageName}.dto.criteria.filter.*;

import java.util.List;

public class BaseCriteria {

    private StringFilter id;

    private NothingFilter nothing;    // 无任何操作

    private StringFilter insertUserId;    // 创建者用户ID

    private StringFilter operateUserId;    // 操作者用户ID

    private StringFilter insertTime;    // 插入时间

    private StringFilter updateTime;    // 更新时间

    @RestFieldAllow(allowSet = false)
    private Integer useDirectOrderBy;   // 直接使用的排序

    private String appendRelated;       // 另外关联使用的表的属性

    private String orderBy;     // 排序属性（属性名+asc/desc的方式，逗号隔开，例如 realName, myAddress desc）

    private Integer limit;      // 限制的查询数量

    private List<String> associationNameList;   // 关联属性获取（列出的属性都会关联查询）

    private List<String> dictionaryNameList;    // 关联数据字典值获取（列出的属性都会关联查询）

    private SystemUserCriteria insertUser;    // 创建者用户

    private SystemUserCriteria operateUser;    // 操作者用户

    @RestFieldAllow(allowSet = false)
    private Integer authorityPass;      // 权限是否已验证过（1：是  2或不填：否）

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public NothingFilter getNothing() {
        return nothing;
    }

    public void setNothing(NothingFilter nothing) {
        this.nothing = nothing;
    }

    public StringFilter getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(StringFilter insertUserId) {
        this.insertUserId = insertUserId;
    }

    public StringFilter getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(StringFilter operateUserId) {
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

    public Integer getUseDirectOrderBy() {
        return useDirectOrderBy;
    }

    public void setUseDirectOrderBy(Integer useDirectOrderBy) {
        this.useDirectOrderBy = useDirectOrderBy;
    }

    public String getAppendRelated() {
        return appendRelated;
    }

    public void setAppendRelated(String appendRelated) {
        this.appendRelated = appendRelated;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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

    public Integer getAuthorityPass() {
        return authorityPass;
    }

    public void setAuthorityPass(Integer authorityPass) {
        this.authorityPass = authorityPass;
    }
}
