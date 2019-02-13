package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;

/**
 * 管理员重置密码用的DTO.
 */
public class PasswordResetDTO {

    @NotBlank
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
