package ${packageName}.dto;

import ${packageName}.annotation.RestClassAllow;

import java.util.Objects;

/**
 * A DTO for the BmMessage entity.
 */
@RestClassAllow(allowSet = false)
public class JhiUserAuthorityDTO extends BaseDTO {

    private Long userId;

    private String authorityName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return "BmMessageDTO{" +
            "userId=" + getUserId() +
            ", authorityName='" + getAuthorityName() + "'" +
            "}";
    }
}
