package ${packageName}.web.rest;

import ${packageName}.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import ${packageName}.config.YmlConfig;
import ${packageName}.security.jwt.JWTFilter;
import ${packageName}.web.rest.vm.LoginVM;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@Api(description="登录认证")
@RestController
@RequestMapping("/api")
public class UserJWTController {

    @Autowired
    private YmlConfig ymlConfig;

    @ApiOperation(value="用户登录")
    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        String protocolPrefix = ymlConfig.getRemoteProtocolPrefix();
        String authorizationIp = ymlConfig.getRemoteAuthorizationIp();
        String authorizationPort = ymlConfig.getRemoteAuthorizationPort();
        String authenticateUrl = protocolPrefix + authorizationIp
                + (authorizationPort == null || "".equals(authorizationPort) || "80".equals(authorizationPort) ? "" : ":" + authorizationPort)
                + ymlConfig.getAuthenticateUrl();
        String resultJson = HttpUtil.doPost(authenticateUrl, JSON.toJSONString(loginVM), null);
        if (resultJson != null) {
            JWTToken jwtToken = JSON.parseObject(resultJson, JWTToken.class);
            String jwt = jwtToken.getId_token();
            if (jwt != null) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
            }
        }
        throw new BadCredentialsException("用户名或密码错误");
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        private String id_token;
        public JWTToken() {}
        public JWTToken(String id_token) {
            this.id_token = id_token;
        }
        public String getId_token() {
            return id_token;
        }
        public void setId_token(String id_token) {
            this.id_token = id_token;
        }
    }

}
