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

public interface SmMeetingService extends IService<SmMeeting> {

    ReturnCommonDTO save(SmMeetingDTO smMeetingDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingDTO> findAll(SmMeetingCriteria criteria);
    IPage<SmMeetingDTO> findPage(SmMeetingCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingCriteria criteria);

    SmMeetingDTO getAssociations(SmMeetingDTO smMeetingDTO, BaseCriteria criteria);

}
