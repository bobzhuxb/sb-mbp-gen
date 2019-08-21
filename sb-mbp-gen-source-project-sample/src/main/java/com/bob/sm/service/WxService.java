package com.bob.sm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bob.sm.dto.help.*;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    boolean refreshAccessTokenSingle();

    boolean refreshAccessToken(int times);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String publicPageUrl);

    ReturnCommonDTO<ReturnMapAddressDTO> getAddressByLogLat(ParamLogLatDTO logLatDTO);

    ReturnCommonDTO<IPage<ReturnMapSearchResultDTO>> getAddressByKeyword(ParamMapKeywordSearchDTO mapKeywordSearchDTO);

}
