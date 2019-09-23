package ${packageName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.dto.help.*;

/**
 * 微信相关处理类
 * @author Bob
 */
public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    boolean refreshAccessTokenSingle();

    boolean refreshAccessToken(int times);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String publicPageUrl);

    ReturnCommonDTO<ReturnMapAddressDTO> getAddressByLogLat(ParamLogLatDTO logLatDTO);

    ReturnCommonDTO<IPage<ReturnMapSearchResultDTO>> getAddressByKeyword(ParamMapKeywordSearchDTO mapKeywordSearchDTO);

}
