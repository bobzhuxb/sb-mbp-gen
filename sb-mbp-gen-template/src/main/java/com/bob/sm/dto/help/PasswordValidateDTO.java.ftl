package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 当前用户验证密码用的DTO
 * @author Bob
 */
public class PasswordValidateDTO {

    @NotBlank
    @Size(min = 4, max = 100)
    private String currentPassword;

    public String getCurrentPassword() {

        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

}
