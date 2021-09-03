package ${packageName}.dto.criteria;

import ${packageName}.annotation.GenComment;
import ${packageName}.annotation.RestFieldAllow;
import ${packageName}.dto.criteria.filter.*;

import java.util.List;

/**
 * 基础查询类
 * @author Bob
 */
public class BaseCriteria {

    @GenComment("主键ID")
    private StringFilter id;

    @GenComment("无任何操作")
    private NothingFilter nothing;

    @GenComment("创建者用户ID")
    private StringFilter insertUserId;

    @GenComment("操作者用户ID")
    private StringFilter operateUserId;

    @GenComment("插入时间")
    private StringFilter insertTime;

    @GenComment("更新时间")
    private StringFilter updateTime;

    @GenComment("直接使用的排序")
    @RestFieldAllow(allowSet = false)
    private Integer useDirectOrderBy;

    @GenComment("另外关联使用的表的属性")
    private String appendRelated;

    @GenComment("排序")
    private String orderBy;

    @GenComment("限制的查询数量")
    private Integer limit;

    @GenComment("关联属性")
    private List<String> associationNameList;

    @GenComment("关联数据字典")
    private List<String> dictionaryNameList;

    @GenComment("需要查询的表的列")
    private List<String> sqlColumnList;

    @GenComment("创建者用户")
    private SystemUserCriteria insertUser;

    @GenComment("操作者用户")
    private SystemUserCriteria operateUser;

    @GenComment("权限是否已验证过（1：是  2或不填：否）")
    @RestFieldAllow(allowSet = false)
    private Integer authorityPass;

    @GenComment("是否从缓存查（1：是  2或不填：否）")
    private Integer findFromCache;

    @GenComment("查询的interNo条件")
    private String interNo;

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

    public List<String> getSqlColumnList() {
        return sqlColumnList;
    }

    public void setSqlColumnList(List<String> sqlColumnList) {
        this.sqlColumnList = sqlColumnList;
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

    public Integer getFindFromCache() {
        return findFromCache;
    }

    public void setFindFromCache(Integer findFromCache) {
        this.findFromCache = findFromCache;
    }

    public String getInterNo() {
        return interNo;
    }

    public void setInterNo(String interNo) {
        this.interNo = interNo;
    }
}
