package com.bob.sm.mapper;

import com.bob.sm.domain.*;
import org.apache.ibatis.annotations.Param;

/**
 * 会议 Mapper
 */
public interface SmMeetingMapper extends BaseCommonMapper<SmMeeting> {

    // 级联置空organizer_id字段
    void organizerIdCascadeToNull(@Param("organizerId")long organizerId);

    // 级联置空contractor_id字段
    void contractorIdCascadeToNull(@Param("contractorId")long contractorId);

}
