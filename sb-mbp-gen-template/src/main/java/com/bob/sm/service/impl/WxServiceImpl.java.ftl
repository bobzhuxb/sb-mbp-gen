package ${packageName}.service.impl;

import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.*;
import ${packageName}.service.WxService;
import ${packageName}.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class WxServiceImpl implements WxService {

    private final Logger log = LoggerFactory.getLogger(WxServiceImpl.class);

    @Autowired
    private YmlConfig ymlConfig;

    /**
     * 微信登录验证及系统登录
     * @param wxOpenIdParamDTO 微信验证标识
     */
    @Override
    public WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO) {
        log.debug("微信登录验证 : {}", wxOpenIdParamDTO);
        String nowTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 获取请求URL
        String requestUrl = Constants.WXAPP_OPEN_ID_URL + "?appid=" + ymlConfig.getWxAppId()
                + "&secret=" + ymlConfig.getWxAppSecret()
                + "&js_code=" + wxOpenIdParamDTO.getJsCode().trim() + "&grant_type=" + Constants.WXAPP_GRANT_TYPE;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 返回数据
            WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
            loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.WX_SERVER_NOT_CONNECT.getValue());
            loginStatusDTO.setErrMsg("无法连接微信服务器");
            return loginStatusDTO;
        }
        WxOpenIdResultDTO wxResultDTO = JSON.parseObject(httpResultJSONStr, WxOpenIdResultDTO.class);
        String sessionKey = wxResultDTO.getSession_key();
        String openId = wxResultDTO.getOpenid();

        String token = null;
        // 设置账号参数，查询密码
        String login = "$" + openId;
        if (wxResultDTO.getErrcode() == null) {
            // TODO: 微信验证成功，新增或更新用户
        } else {
            // 返回数据
            log.info("微信验证失败（code：" + wxResultDTO.getErrcode() + "，info：" + wxResultDTO.getErrmsg() + "）");
            WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
            loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.VERIFY_FAIL.getValue());
            loginStatusDTO.setErrMsg("验证失败");
            return loginStatusDTO;
        }

        // 返回数据
        WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
        loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.SUCCESS.getValue());
        loginStatusDTO.setErrMsg(wxResultDTO.getErrmsg());
        loginStatusDTO.setToken(token);
        loginStatusDTO.setOpenId(openId);
        return loginStatusDTO;
    }

    /**
     * 单次获取并刷新ACCESS_TOKEN
     * @return
     */
    @Override
    public boolean refreshAccessTokenSingle() {
        // 获取请求URL
        String requestUrl = Constants.WXAPP_GET_TOKEN_URL + "?grant_type=client_credential&appid="
                + ymlConfig.getWxAppId() + "&secret=" + ymlConfig.getWxAppSecret();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取ACCESS_TOKEN异常
            log.error(e.getMessage(), e);
            return false;
        }
        WxAccessTokenResultDTO wxAccessTokenDTO = JSON.parseObject(httpResultJSONStr, WxAccessTokenResultDTO.class);
        if (wxAccessTokenDTO.getErrcode() != null && !"0".equals(wxAccessTokenDTO.getErrcode())) {
            log.error(wxAccessTokenDTO.getErrmsg());
            return false;
        }
        String accessToken = wxAccessTokenDTO.getAccess_token();
        String expiresIn = wxAccessTokenDTO.getExpires_in();
        log.info("ACCESS_TOKEN获取成功：" + accessToken + "。过期时间：" + expiresIn);
        // 更新ACCESS_TOKEN
        Constants.WX_ACCESS_TOKEN_NOW = accessToken;
        return true;
    }

    /**
     * 多次刷新ACCESS_TOKEN（获取失败时才再次获取）
     * @param totalTimes 总次数
     * @return
     */
    public boolean refreshAccessToken(int totalTimes) {
        boolean result = false;
        int times = 0;
        while (true) {
            times++;
            try {
                // 微信公众号的ACCESS_TOKEN刷新任务
                result = refreshAccessTokenSingle();
            } catch (Exception e) {
                result = false;
            }
            if (!result && times < totalTimes) {
                // 1分钟之后重新获取（最多10次）
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    // 不做任何处理
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 根据当前的ACCESS_TOKEN获取ticket
     * @param publicPageUrl 当前网页URL
     * @return
     */
    @Override
    public ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfoByCurrentAccessToken(String publicPageUrl) {
        if ("".equals(Constants.WX_ACCESS_TOKEN_NOW)) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "等待启动");
        }
        // 获取请求URL
        String requestUrl = Constants.WXAPP_GET_TICKET_URL + "?access_token=" + Constants.WX_ACCESS_TOKEN_NOW
                + "&type=jsapi";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取ticket异常
            log.error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket异常");
        }
        WxTicketResultDTO wxTicketResultDTO = JSON.parseObject(httpResultJSONStr, WxTicketResultDTO.class);
        if (wxTicketResultDTO.getErrcode() != null && !"0".equals(wxTicketResultDTO.getErrcode())) {
            log.error(wxTicketResultDTO.getErrmsg());
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket失败");
        }
        String ticket = wxTicketResultDTO.getTicket();
        String expiresIn = wxTicketResultDTO.getExpires_in();
        String nonceStr = StringUtil.generateNonceStr();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 签名
        String toBeSignatureStr = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp="
                + timestamp + "&url=" + publicPageUrl;
        String signature = StringUtil.sha1(toBeSignatureStr);
        // 设置返回数据
        ReturnWxJsapiInfoDTO wxJsapiInfoDTO = new ReturnWxJsapiInfoDTO();
        wxJsapiInfoDTO.setNonceStr(nonceStr);
        wxJsapiInfoDTO.setTicket(ticket);
        wxJsapiInfoDTO.setTimestamp(timestamp);
        wxJsapiInfoDTO.setUrl(publicPageUrl);
        wxJsapiInfoDTO.setSignature(signature);
        // 返回数据
        return new ReturnCommonDTO<>(wxJsapiInfoDTO);
    }

    /**
     * 根据经纬度获取详细地址
     * @param logLatDTO
     * @return
     */
    public ReturnCommonDTO<ReturnMapAddress> getAddressByLogLat(ParamLogLatDTO logLatDTO) {
        // 获取请求URL
        String requestUrl = Constants.TXMAP_REVERSE_ADDRESS_PARSE_URL + "?key=" + ymlConfig.getTxMapKey()
                + "&location=" + logLatDTO.getLatitude() + "," + logLatDTO.getLongitude();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取ticket异常
            log.error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket异常");
        }
        TxMapAddressReturnDTO txMapAddressReturnDTO = JSON.parseObject(httpResultJSONStr, TxMapAddressReturnDTO.class);
        if (txMapAddressReturnDTO.getStatus() == null || txMapAddressReturnDTO.getStatus() != 0) {
            log.error("腾讯逆地址解析失败：code=" + txMapAddressReturnDTO.getStatus() + "，message="
                    + txMapAddressReturnDTO.getMessage());
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败");
        }
        if (txMapAddressReturnDTO.getResult() == null || txMapAddressReturnDTO.getResult().getAddress() == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败，没有该地址");
        }
        ReturnMapAddress returnMapAddress = new ReturnMapAddress();
        returnMapAddress.setAddress(txMapAddressReturnDTO.getResult().getAddress());
        return new ReturnCommonDTO<>(returnMapAddress);
    }

}
