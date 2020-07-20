package ${packageName}.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.Constants;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.*;
import ${packageName}.service.WxService;
import ${packageName}.util.MyStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 微信相关业务
 * @author Bob
 */
@Service
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
        // 获取请求URL
        String requestUrl = ymlConfig.getMsWxUrl() + Constants.MS_WXAPP_OPEN_ID_URL + "?js_code=" + wxOpenIdParamDTO.getJsCode().trim();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("token", ymlConfig.getMsWxVerifyToken());
        String httpResultJSONStr = null;
        // 微服务调用
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 错误返回
            WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
            loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.WX_SERVER_NOT_CONNECT.getValue());
            loginStatusDTO.setErrMsg("无法连接服务器");
            return loginStatusDTO;
        }
        WxMsResultDTO wxMsResultDTO = JSON.parseObject(httpResultJSONStr, WxMsResultDTO.class);
        if (wxMsResultDTO.getCode() != 0) {
            // 错误返回
            log.error("微信（微服务）验证失败（code：" + wxMsResultDTO.getCode() + "，info：" + wxMsResultDTO.getMsg() + "）");
            WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
            loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.WX_SERVER_NOT_CONNECT.getValue());
            loginStatusDTO.setErrMsg("验证失败：" + wxMsResultDTO.getMsg());
            return loginStatusDTO;
        }
        WxOpenIdResultDTO wxResultDTO = ((JSONObject) wxMsResultDTO.getData()).toJavaObject(WxOpenIdResultDTO.class);
        String openId = wxResultDTO.getOpenid();

        String token = null;
        // 设置账号参数，查询密码
        String login = "$" + openId;
        if (wxResultDTO.getErrcode() == null) {
            // TODO: 微信验证成功，新增或更新用户
        } else {
            // 返回数据
            log.error("微信验证失败（code：" + wxResultDTO.getErrcode() + "，info：" + wxResultDTO.getErrmsg() + "）");
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
     * 根据当前的ACCESS_TOKEN获取ticket
     * @param publicPageUrl 当前网页URL
     * @return
     */
    @Override
    public ReturnCommonDTO<ReturnWxJsapiInfoDTO> getJsapiInfo(String publicPageUrl) {
        // 获取请求URL
        String requestUrl = ymlConfig.getMsWxUrl() + Constants.MS_WXAPP_JSAPI_URL;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("token", ymlConfig.getMsWxVerifyToken());
        String httpResultJSONStr = null;
        // 微服务调用
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取数据异常
            log.error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket异常");
        }
        WxMsResultDTO wxMsResultDTO = JSON.parseObject(httpResultJSONStr, WxMsResultDTO.class);
        if (wxMsResultDTO.getCode() != 0) {
            // 错误返回
            log.error("微信（微服务）获取ticket失败（code：" + wxMsResultDTO.getCode() + "，info：" + wxMsResultDTO.getMsg() + "）");
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket失败");
        }
        WxTicketResultDTO wxTicketResultDTO = ((JSONObject) wxMsResultDTO.getData()).toJavaObject(WxTicketResultDTO.class);
        if (wxTicketResultDTO.getErrcode() != null && !"0".equals(wxTicketResultDTO.getErrcode())) {
            log.error(wxTicketResultDTO.getErrmsg());
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取ticket失败");
        }
        String ticket = wxTicketResultDTO.getTicket();
        String nonceStr = MyStringUtil.generateNonceStr();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 签名
        String toBeSignatureStr = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp="
                + timestamp + "&url=" + publicPageUrl;
        String signature = MyStringUtil.sha1(toBeSignatureStr);
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
    @Override
    public ReturnCommonDTO<ReturnMapAddressDTO> getAddressByLogLat(ParamLogLatDTO logLatDTO) {
        // 获取请求URL
        String requestUrl = ymlConfig.getMsWxUrl() + Constants.MS_TXMAP_REVERSE_ADDRESS_URL
                + "?latitude=" + logLatDTO.getLatitude() + "&longitude=" + logLatDTO.getLongitude();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("token", ymlConfig.getMsWxVerifyToken());
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取数据异常
            log.error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取address异常");
        }
        WxMsResultDTO wxMsResultDTO = JSON.parseObject(httpResultJSONStr, WxMsResultDTO.class);
        if (wxMsResultDTO.getCode() != 0) {
            // 错误返回
            log.error("微信（微服务）逆地址解析失败（code：" + wxMsResultDTO.getCode() + "，info：" + wxMsResultDTO.getMsg() + "）");
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败");
        }
        TxMapAddressReturnDTO txMapAddressReturnDTO = ((JSONObject) wxMsResultDTO.getData()).toJavaObject(TxMapAddressReturnDTO.class);
        if (txMapAddressReturnDTO.getStatus() == null || txMapAddressReturnDTO.getStatus() != 0) {
            log.error("腾讯逆地址解析失败：code=" + txMapAddressReturnDTO.getStatus() + "，message="
                    + txMapAddressReturnDTO.getMessage());
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败");
        }
        if (txMapAddressReturnDTO.getResult() == null || txMapAddressReturnDTO.getResult().getAddress() == null) {
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败，没有该地址");
        }
        ReturnMapAddressDTO returnMapAddress = new ReturnMapAddressDTO();
        returnMapAddress.setAddress(txMapAddressReturnDTO.getResult().getAddress());
        return new ReturnCommonDTO<>(returnMapAddress);
    }

    /**
     * 根据地点关键字在指定区域搜索
     * @param mapKeywordSearchDTO
     * @return
     */
    @Override
    public ReturnCommonDTO<IPage<ReturnMapSearchResultDTO>> getAddressByKeyword(
            ParamMapKeywordSearchDTO mapKeywordSearchDTO) {
        // 默认在苏州查找
        if (mapKeywordSearchDTO.getRegion() == null) {
            mapKeywordSearchDTO.setRegion("苏州");
        }
        // 获取请求URL
        String requestUrl = ymlConfig.getMsWxUrl() + Constants.MS_TXMAP_KEYWORD_SEARCH_URL
                + "?keyword=" + mapKeywordSearchDTO.getKeyword() + "&region=" + mapKeywordSearchDTO.getRegion()
                + "&page_index=" + mapKeywordSearchDTO.getCurrent() + "&page_size=" + mapKeywordSearchDTO.getSize();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/json");
        headerMap.put("token", ymlConfig.getMsWxVerifyToken());
        String httpResultJSONStr = null;
        try {
            httpResultJSONStr = HttpUtil.doGet(requestUrl, headerMap);
        } catch (Exception e) {
            // 获取数据异常
            log.error(e.getMessage(), e);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "获取经纬度异常");
        }
        WxMsResultDTO wxMsResultDTO = JSON.parseObject(httpResultJSONStr, WxMsResultDTO.class);
        if (wxMsResultDTO.getCode() != 0) {
            // 错误返回
            log.error("微信（微服务）地址解析失败（code：" + wxMsResultDTO.getCode() + "，info：" + wxMsResultDTO.getMsg() + "）");
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败");
        }
        TxMapSearchReturnDTO txMapSearchReturnDTO = ((JSONObject) wxMsResultDTO.getData()).toJavaObject(TxMapSearchReturnDTO.class);
        if (txMapSearchReturnDTO.getStatus() == null || txMapSearchReturnDTO.getStatus() != 0) {
            log.error("腾讯地图关键字查询失败：code=" + txMapSearchReturnDTO.getStatus() + "，message="
                    + txMapSearchReturnDTO.getMessage());
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败");
        }
        if (txMapSearchReturnDTO.getCount() == null || txMapSearchReturnDTO.getData() == null) {
            log.error("腾讯地图关键字查询失败：数据错误：" + httpResultJSONStr);
            return new ReturnCommonDTO<>(Constants.commonReturnStatus.FAIL.getValue(), "解析失败，数据有误");
        }
        List<ReturnMapSearchResultDTO> returnMapSearchResultList = new ArrayList<>();
        for (TxMapSearchDataDTO txMapSearchDataDTO : txMapSearchReturnDTO.getData()) {
            ReturnMapSearchResultDTO returnMapSearchResultDTO = new ReturnMapSearchResultDTO();
            returnMapSearchResultDTO.setTitle(txMapSearchDataDTO.getTitle());
            returnMapSearchResultDTO.setAddress(txMapSearchDataDTO.getAddress());
            returnMapSearchResultDTO.setProvince(txMapSearchDataDTO.getProvince());
            returnMapSearchResultDTO.setCity(txMapSearchDataDTO.getCity());
            returnMapSearchResultDTO.setLongitude(txMapSearchDataDTO.getLocation().getLng());
            returnMapSearchResultDTO.setLatitude(txMapSearchDataDTO.getLocation().getLat());
            returnMapSearchResultList.add(returnMapSearchResultDTO);
        }
        // 组织分页数据
        IPage<ReturnMapSearchResultDTO> page = new IPage<ReturnMapSearchResultDTO>() {
            @Override
            public List<ReturnMapSearchResultDTO> getRecords() {
                return returnMapSearchResultList;
            }
            @Override
            public IPage<ReturnMapSearchResultDTO> setRecords(List<ReturnMapSearchResultDTO> records) {
                return null;
            }
            @Override
            public long getTotal() {
                return txMapSearchReturnDTO.getCount();
            }
            @Override
            public IPage<ReturnMapSearchResultDTO> setTotal(long total) {
                return null;
            }
            @Override
            public long getSize() {
                return mapKeywordSearchDTO.getSize();
            }
            @Override
            public IPage<ReturnMapSearchResultDTO> setSize(long size) {
                return null;
            }
            @Override
            public long getCurrent() {
                return mapKeywordSearchDTO.getCurrent();
            }
            @Override
            public IPage<ReturnMapSearchResultDTO> setCurrent(long current) {
                return null;
            }
        };
        return new ReturnCommonDTO<>(page);
    }

}
