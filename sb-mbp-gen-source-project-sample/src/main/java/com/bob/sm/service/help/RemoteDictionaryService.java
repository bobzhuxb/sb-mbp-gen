package com.bob.sm.service.help;

import com.bob.sm.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bob.sm.config.YmlConfig;
import com.bob.sm.dto.help.RemoteDictionaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据远程获取
 */
@Service
public class RemoteDictionaryService {

    @Autowired
    private YmlConfig ymlConfig;

    public List<RemoteDictionaryDTO> loadDictionaryData() {
        String protocolPrefix = ymlConfig.getRemoteProtocolPrefix();
        String authorizationIp = ymlConfig.getRemoteAuthorizationIp();
        String authorizationPort = ymlConfig.getRemoteAuthorizationPort();
        String loadCurrentUserUrl = protocolPrefix + authorizationIp
                + (authorizationPort == null || "".equals(authorizationPort) || "80".equals(authorizationPort) ? "" : ":" + authorizationPort)
                + ymlConfig.getLoadDictionaryUrl();
        String resultJson = HttpUtil.doPost(loadCurrentUserUrl, "", null);
        if (resultJson == null) {
            // 远程加载失败
            throw new UsernameNotFoundException("数据获取失败");
        } else {
            // 远程加载成功
            List<RemoteDictionaryDTO> dictionaryDTOList = JSON.parseArray(resultJson, RemoteDictionaryDTO.class);
            return dictionaryDTOList;
        }
    }

}
