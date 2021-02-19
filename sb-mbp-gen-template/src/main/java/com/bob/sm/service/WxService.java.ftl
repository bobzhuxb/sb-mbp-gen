package ${packageName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.dto.help.*;

/**
 * 微信相关业务
 * @author Bob
 */
public interface WxService {

    WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO);

    ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfo(String publicPageUrl);

    ReturnCommonDTO<ReturnMapAddressDTO> getAddressByLogLat(ParamLogLatDTO logLatDTO);

    ReturnCommonDTO<IPage<ReturnMapSearchResultDTO>> getAddressByKeyword(ParamMapKeywordSearchDTO mapKeywordSearchDTO);

}
