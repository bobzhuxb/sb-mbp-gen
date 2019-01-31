package ${packageName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${packageName}.domain.*;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import ${packageName}.dto.help.MbpPage;
import ${packageName}.dto.help.ReturnCommonDTO;

import java.util.List;
import java.util.Optional;

public interface SmMeetingScheduleService extends IService<SmMeetingSchedule> {

    ReturnCommonDTO save(SmMeetingScheduleDTO smMeetingScheduleDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingScheduleDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingScheduleDTO> findAll(SmMeetingScheduleCriteria criteria);
    IPage<SmMeetingScheduleDTO> findPage(SmMeetingScheduleCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingScheduleCriteria criteria);

    SmMeetingScheduleDTO getAssociations(SmMeetingScheduleDTO smMeetingScheduleDTO, BaseCriteria criteria);

}
