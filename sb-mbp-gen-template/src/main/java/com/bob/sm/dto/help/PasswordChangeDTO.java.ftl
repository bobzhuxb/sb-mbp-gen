package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 当前用户修改密码用的DTO.
 * @author Bob
 */
public class PasswordChangeDTO {

    @NotBlank
    @Size(min = 4, max = 100)
    private String currentPassword;

    @NotBlank
    @Size(min = 4, max = 100)
    private String newPassword;

    public String getCurrentPassword() {

        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
