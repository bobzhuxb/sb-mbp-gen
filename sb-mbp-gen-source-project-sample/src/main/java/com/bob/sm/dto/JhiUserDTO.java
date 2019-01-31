package com.bob.sm.dto;

import com.bob.sm.annotation.RestClassAllow;

import java.util.Objects;

/**
 * A DTO for the BmMessage entity.
 */
@RestClassAllow(allowSet = false)
public class JhiUserDTO extends BaseDTO {

    private Long id;

    private String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JhiUserDTO bmMessageDTO = (JhiUserDTO) o;
        if (bmMessageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bmMessageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BmMessageDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
