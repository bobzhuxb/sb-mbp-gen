package com.bob.sm.service.impl;

import com.bob.sm.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bob.sm.config.Constants;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.dto.help.*;
import com.bob.sm.service.WxService;
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
    public WxLoginStatusDTO getOpenIdAndLogin(ParamWxOpenIdDTO wxOpenIdParamDTO) {
        log.debug("微信登录验证 : {}", wxOpenIdParamDTO);
        String nowTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 获取请求URL
        String requestUrl = Constants.WXAPP_OPEN_ID_URL + "?appid=" + Constants.WXAPP_ID + "&secret=" + Constants.WXAPP_SECRET
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
            // 微信验证成功，远程新增或更新用户
            String protocolPrefix = ymlConfig.getRemoteProtocolPrefix();
            String authorizationIp = ymlConfig.getRemoteAuthorizationIp();
            String authorizationPort = ymlConfig.getRemoteAuthorizationPort();
            String addOrUpdateUserUrl = protocolPrefix + authorizationIp
                    + (authorizationPort == null || "".equals(authorizationPort) || "80".equals(authorizationPort) ? "" : ":" + authorizationPort)
                    + ymlConfig.getAddOrUpdateUserUrl();
            RemoteAuthorizationDTO remoteAuthorizationDTO = new RemoteAuthorizationDTO();
            remoteAuthorizationDTO.setLogin(login);
            remoteAuthorizationDTO.setName(wxOpenIdParamDTO.getWxName());
            remoteAuthorizationDTO.setAuthorities(Arrays.asList("ROLE_WX"));
            String resultJson = HttpUtil.doPost(addOrUpdateUserUrl, JSON.toJSONString(remoteAuthorizationDTO), null);
            if (resultJson != null) {
                RemoteAuthorizationDTO remoteAuthorizationResult = JSON.parseObject(resultJson, RemoteAuthorizationDTO.class);
                if (!"success".equals(remoteAuthorizationResult.getAuthorizationResult())) {
                    // 用户远程新增或修改失败
                    log.info("用户信息录入失败");
                    WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
                    loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.USER_SAVE_FAIL.getValue());
                    loginStatusDTO.setErrMsg("用户信息录入失败");
                    return loginStatusDTO;
                }
                token = remoteAuthorizationResult.getToken();
            } else {
                // 用户远程新增或修改失败
                log.info("用户信息录入失败，空返回");
                WxLoginStatusDTO loginStatusDTO = new WxLoginStatusDTO();
                loginStatusDTO.setResultCode(Constants.wxLoginResultStatus.USER_SAVE_FAIL.getValue());
                loginStatusDTO.setErrMsg("用户信息录入失败");
                return loginStatusDTO;
            }
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

}
