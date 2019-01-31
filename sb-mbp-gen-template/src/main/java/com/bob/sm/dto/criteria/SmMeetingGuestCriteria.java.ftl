package ${packageName}.dto.criteria;

import ${packageName}.dto.criteria.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会议嘉宾 条件过滤器
 */
public class SmMeetingGuestCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;    // 姓名

    private StringFilter desc;    // 描述

    private LongFilter smMeetingId;    // 会议ID

    public SmMeetingGuestCriteria() {
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
    
    public StringFilter getDesc() {
        return desc;
    }

    public void setDesc(StringFilter desc) {
        this.desc = desc;
    }

    public LongFilter getSmMeetingId() {
        return smMeetingId;
    }
	
	public void setSmMeetingId(LongFilter smMeetingId) {
        this.smMeetingId = smMeetingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmMeetingGuestCriteria that = (SmMeetingGuestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        desc
        );
    }

    @Override
    public String toString() {
        return "SmMeetingGuestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (desc != null ? "desc=" + desc + ", " : "") +
            "}";
    }

}
