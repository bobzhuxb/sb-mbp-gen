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

public interface SmMeetingGuestService extends IService<SmMeetingGuest> {

    ReturnCommonDTO save(SmMeetingGuestDTO smMeetingGuestDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingGuestDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingGuestDTO> findAll(SmMeetingGuestCriteria criteria);
    IPage<SmMeetingGuestDTO> findPage(SmMeetingGuestCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingGuestCriteria criteria);

    SmMeetingGuestDTO getAssociations(SmMeetingGuestDTO smMeetingGuestDTO, BaseCriteria criteria);

}
