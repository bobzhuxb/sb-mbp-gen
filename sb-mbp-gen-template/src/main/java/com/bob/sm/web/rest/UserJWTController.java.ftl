package ${packageName}.web.rest;

import ${packageName}.config.Constants;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.security.jwt.JWTFilter;
import ${packageName}.security.jwt.TokenProvider;
import ${packageName}.service.CommonUserService;
import ${packageName}.web.rest.errors.CommonAlertException;
import ${packageName}.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户登录认证Controller.
 * @author Bob
 */
@RestController
public class UserJWTController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private CommonUserService commonUserService;

    public UserJWTController(TokenProvider tokenProvider,
                             AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 登录认证
     * @param loginVM 用户名和密码
     * @param bindingResult 验证（AOP处理时需要用到该参数，不得删除）
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ReturnCommonDTO<JWTToken>> authorize(@Valid @RequestBody LoginVM loginVM, BindingResult bindingResult) {
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
            // 获取登录用户信息
            SystemUserDTO systemUserDTO = commonUserService.findForceCacheUserByLogin(loginVM.getUsername());
            // 设置返回数据
            resultDTO = new ReturnCommonDTO(new JWTToken(jwt, systemUserDTO.getLogin(), systemUserDTO.getName()));
            // 审计：登录
            systemLogMapper.insert(new SystemLog(Constants.systemLogType.LOGIN.getValue(), null, systemUserDTO.getId(), nowTimeStr));
        } catch (AuthenticationException ex) {
            resultDTO = new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "用户名或密码错误");
        } catch (CommonAlertException e) {
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(Constants.commonReturnStatus.FAIL.getValue(), "登录异常");
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 退出登录（前端操作，后端只记录日志）
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<ReturnCommonDTO> logout() {
        // 获取当前时间和日期
        LocalDateTime nowTime = LocalDateTime.now();
        String nowTimeStr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(nowTime);
        // 获取当前用户
        SystemUserDTO systemUserDTO = commonUserService.getCurrentUser();
        if (systemUserDTO == null) {
            // 没有登录
            throw new CommonAlertException("您尚未登录!");
        }
        // 审计：登出
        systemLogMapper.insert(new SystemLog(Constants.systemLogType.LOGOUT.getValue(), null, systemUserDTO.getId(), nowTimeStr));
        return ResponseEntity.ok().headers(null).body(null);
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
