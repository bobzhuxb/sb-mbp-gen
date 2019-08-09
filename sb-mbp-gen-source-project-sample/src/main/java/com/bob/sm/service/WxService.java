package com.bob.sm.service;

import com.bob.sm.dto.help.ReturnCommonDTO;
import com.bob.sm.dto.help.ReturnWxJsapiInfoDTO;
import com.bob.sm.dto.help.WxLoginStatusDTO;
import com.bob.sm.dto.help.ParamWxOpenIdDTO;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    boolean refreshAccessTokenSingle();

    boolean refreshAccessToken(int times);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String currentSubUrl);

}
