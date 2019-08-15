package ${packageName}.service;

import ${packageName}.dto.help.*;

public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    boolean refreshAccessTokenSingle();

    boolean refreshAccessToken(int times);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String publicPageUrl);

    ReturnCommonDTO<ReturnMapAddress> getAddressByLogLat(ParamLogLatDTO logLatDTO);

}
