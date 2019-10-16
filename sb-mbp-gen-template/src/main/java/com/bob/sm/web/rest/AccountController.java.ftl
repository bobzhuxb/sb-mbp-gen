package ${packageName}.web.rest;

import ${packageName}.config.Constants;
import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.SystemUserDTO;
import ${packageName}.dto.help.*;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.AccountService;
import ${packageName}.service.CommonService;
import ${packageName}.util.ParamValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 注意规范：新增的方法以create开头，修改的方法以update开头，删除的方法以delete开头，查询的方法以get开头
 * 账户Controller
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private AccountService accountService;

    /**
     * 当前请求是否已登录
     * @return 当前请求是否已登录
     */
    @GetMapping("/authenticate")
    public ResponseEntity<String> isAuthenticated(HttpServletRequest request) {
        log.debug("Controller ==> 判断当前请求是否已登录 : {}", request);
        return ResponseEntity.ok().headers(null).body(request.getRemoteUser());
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    @GetMapping("/account")
    public ResponseEntity<EnhanceUserDTO> getAccount() {
        Optional<String> loginOptional = SecurityUtils.getCurrentUserLogin();
        if (!loginOptional.isPresent()) {
            return null;
        }
        EnhanceUserDTO userInfo = accountService.getFullUserInfoByLogin(loginOptional.get());
        return ResponseEntity.ok().headers(null).body(userInfo);
    }

    /**
     * 当前账号修改自己的密码
     * @param passwordChangeDto
     */
    @PostMapping("/change-password")
    public ResponseEntity<ReturnCommonDTO> changePassword(
            @RequestBody @Validated PasswordChangeDTO passwordChangeDto, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> accountService.changePassword(passwordChangeDto.getCurrentPassword(),
                        passwordChangeDto.getNewPassword()), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 当前账号修改自己的信息
     * @param systemUserDTO
     */
    @PostMapping("/change-self-info")
    public ResponseEntity<ReturnCommonDTO> changeSelfInfo(
            @RequestBody @Validated SystemUserDTO systemUserDTO, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> accountService.changeSelfInfo(systemUserDTO), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 当前账号验证密码
     * @param passwordValidateDto
     */
    @PostMapping("/validate-password")
    public ResponseEntity<ReturnCommonDTO> validatePassword(
            @RequestBody @Validated PasswordValidateDTO passwordValidateDto, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> accountService.validatePassword(passwordValidateDto.getCurrentPassword()), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 管理员重置别人的密码
     * @param passwordResetDTO
     */
    @PostMapping("/admin-reset-password")
    @PreAuthorize("hasUpdate('account')")
    public ResponseEntity<ReturnCommonDTO> resetPassword(
            @RequestBody PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = commonService.doWithExceptionHandle(
                () -> accountService.resetPassword(passwordResetDTO.getUserId()), log);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
