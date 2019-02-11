package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 会议嘉宾 Mapper
 */
public interface SmMeetingGuestMapper extends BaseCommonMapper<SmMeetingGuest> {

    // 级联置空sm_meeting_id字段
    void smMeetingIdCascadeToNull(@Param("smMeetingId")long smMeetingId);

}
