package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;

/**
 * 管理员重置密码用的DTO.
 */
public class PasswordResetDTO {

    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
