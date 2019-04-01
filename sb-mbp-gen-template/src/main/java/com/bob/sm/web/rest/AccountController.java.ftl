package ${packageName}.web.rest;

import ${packageName}.config.Constants;
import ${packageName}.dto.EnhanceUserDTO;
import ${packageName}.dto.help.PasswordChangeDTO;
import ${packageName}.dto.help.PasswordResetDTO;
import ${packageName}.dto.help.ReturnCommonDTO;
import ${packageName}.security.SecurityUtils;
import ${packageName}.service.AccountService;
import ${packageName}.util.ParamValidatorUtil;
import ${packageName}.web.rest.errors.CommonException;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 注意规范：新增的方法以create开头，修改的方法以update开头，删除的方法以delete开头，查询的方法以get开头
 */
@Api(description="当前用户")
@RestController
@RequestMapping("/api")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * 当前请求是否已登录
     * @return 当前请求是否已登录
     */
    @ApiOperation(value="当前请求是否已登录")
    @GetMapping("/authenticate")
    public ResponseEntity<String> isAuthenticated(HttpServletRequest request) {
        log.debug("Controller ==> 判断当前请求是否已登录 : {}", request);
        return ResponseEntity.ok().headers(null).body(request.getRemoteUser());
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    @ApiOperation(value="获取当前登录用户信息")
    @GetMapping("/account")
    @Timed
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
    @ApiOperation(value="当前账号修改自己的密码")
    @PostMapping("/change-password")
    public ResponseEntity<ReturnCommonDTO> changePassword(
            @ApiParam("{\n" +
                    "  \"currentPassword\": \"当前密码\",\n" +
                    "  \"newPassword\": \"新密码\",\n" +
                    "}")
            @RequestBody @Validated PasswordChangeDTO passwordChangeDto, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = accountService.changePassword(passwordChangeDto.getCurrentPassword(),
                    passwordChangeDto.getNewPassword());
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 管理员重置别人的密码
     * @param passwordResetDTO
     */
    @ApiOperation(value="管理员重置别人的密码")
    @PostMapping("/admin-reset-password")
    public ResponseEntity<ReturnCommonDTO> resetPassword(
            @ApiParam("{\n" +
                    "  \"userId\": \"被重置密码的用户ID\",\n" +
                    "}")
            @RequestBody PasswordResetDTO passwordResetDTO, BindingResult bindingResult) {
        // 参数验证
        ReturnCommonDTO returnCommonDTO = ParamValidatorUtil.validateFields(bindingResult);
        if (!Constants.commonReturnStatus.SUCCESS.getValue().equals(returnCommonDTO.getResultCode())) {
            return ResponseEntity.ok().headers(null).body(returnCommonDTO);
        }
        ReturnCommonDTO resultDTO = null;
        try {
            resultDTO = accountService.resetPassword(passwordResetDTO.getUserId());
        } catch (CommonException e) {
            log.error(e.getMessage(), e);
            resultDTO = new ReturnCommonDTO(e.getCode(), e.getMessage());
        }
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
