package ${packageName}.service.micro.impl;

import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.YmlConfig;
import ${packageName}.dto.help.BasePermissionDTO;
import ${packageName}.dto.help.RemotePermissionListDTO;
import ${packageName}.service.micro.BasePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BasePermissionServiceImpl implements BasePermissionService {

    @Autowired
    private YmlConfig ymlConfig;

    @Override
    public void savePermissionsWithChildren(List<BasePermissionDTO> permissionList) {
        String protocolPrefix = ymlConfig.getRemoteProtocolPrefix();
        String authorizationIp = ymlConfig.getRemoteAuthorizationIp();
        String authorizationPort = ymlConfig.getRemoteAuthorizationPort();
        String permissionRegisterUrl = protocolPrefix + authorizationIp
                + (authorizationPort == null || "".equals(authorizationPort) || "80".equals(authorizationPort) ? "" : ":" + authorizationPort)
                + ymlConfig.getRegisterPermissionUrl();
        RemotePermissionListDTO remotePermissionListDTO = new RemotePermissionListDTO();
        remotePermissionListDTO.setPermissionDTOMoreList(permissionList);
        String result = HttpUtil.doPost(permissionRegisterUrl, JSON.toJSONString(remotePermissionListDTO), null);
        if ("success".equals(result)) {
            System.out.println("================远程注册成功==================");
        }
    }

}
