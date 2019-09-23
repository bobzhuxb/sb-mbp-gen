package ${packageName}.web.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.config.Constants;
import ${packageName}.domain.SystemUser;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.mapper.SystemUserMapper;
import ${packageName}.security.jwt.JWTFilter;
import ${packageName}.security.jwt.TokenProvider;
import ${packageName}.util.ParamValidatorUtil;
import ${packageName}.web.rest.vm.LoginVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户登录认证Controller.
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private SystemUserMapper systemUserMapper;

    public UserJWTController(TokenProvider tokenProvider,
                             AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ReturnCommonDTO<JWTToken>> authorize(@Valid @RequestBody LoginVM loginVM, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        // 验证用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        ReturnCommonDTO resultDTO = null;
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            SystemUser systemUser = systemUserMapper.selectList(
                    new QueryWrapper<SystemUser>().eq("login", loginVM.getUsername())).get(0);
            resultDTO = new ReturnCommonDTO(new JWTToken(jwt, systemUser.getLogin(), systemUser.getName()));
        } catch (AuthenticationException ex) {
            resultDTO = new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "用户名或密码错误");
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        private String id_token;
        private String login;
        private String name;
        public JWTToken() {}
        public JWTToken(String id_token, String login, String name) {
            this.id_token = id_token;
            this.login = login;
            this.name = name;
        }
        public String getId_token() {
            return id_token;
        }
        public void setId_token(String id_token) {
            this.id_token = id_token;
        }
        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
