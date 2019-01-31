package com.bob.sm.service;

import com.bob.sm.dto.help.WxLoginStatusDTO;
import com.bob.sm.dto.help.ParamWxOpenIdDTO;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

}
