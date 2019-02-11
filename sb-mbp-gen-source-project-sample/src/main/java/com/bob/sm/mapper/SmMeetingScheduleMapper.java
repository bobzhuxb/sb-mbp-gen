package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 会议日程 Mapper
 */
public interface SmMeetingScheduleMapper extends BaseCommonMapper<SmMeetingSchedule> {

    // 级联置空sm_meeting_id字段
    void smMeetingIdCascadeToNull(@Param("smMeetingId")long smMeetingId);

}
