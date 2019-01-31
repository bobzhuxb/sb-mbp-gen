package ${packageName}.service;

import ${packageName}.dto.help.WxLoginStatusDTO;
import ${packageName}.dto.help.ParamWxOpenIdDTO;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

}
