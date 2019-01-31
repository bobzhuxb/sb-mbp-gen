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

public interface SmMeetingDetailService extends IService<SmMeetingDetail> {

    ReturnCommonDTO save(SmMeetingDetailDTO smMeetingDetailDTO);
    ReturnCommonDTO deleteById(Long id);
    Optional<SmMeetingDetailDTO> findOne(Long id, BaseCriteria criteria);
    List<SmMeetingDetailDTO> findAll(SmMeetingDetailCriteria criteria);
    IPage<SmMeetingDetailDTO> findPage(SmMeetingDetailCriteria criteria, MbpPage pageable);
    int findCount(SmMeetingDetailCriteria criteria);

    SmMeetingDetailDTO getAssociations(SmMeetingDetailDTO smMeetingDetailDTO, BaseCriteria criteria);

}
