package ${packageName}.service;

import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.dto.help.ReturnWxJsapiInfoDTO;
import ${packageName}.dto.help.WxLoginStatusDTO;
import ${packageName}.dto.help.ParamWxOpenIdDTO;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    boolean refreshAccessTokenSingle();

    boolean refreshAccessToken(int times);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String currentSubUrl);

}
