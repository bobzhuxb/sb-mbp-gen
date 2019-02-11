package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 会议嘉宾照片 Mapper
 */
public interface SmMeetingGuestPhotoMapper extends BaseCommonMapper<SmMeetingGuestPhoto> {

    // 级联置空sm_meeting_guest_id字段
    void smMeetingGuestIdCascadeToNull(@Param("smMeetingGuestId")long smMeetingGuestId);

}
