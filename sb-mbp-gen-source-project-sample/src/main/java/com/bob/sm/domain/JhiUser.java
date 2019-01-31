package com.bob.sm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户
 */
@ApiModel(description = "用户")
@Data
public class JhiUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JhiUser jhiUser = (JhiUser) o;
        if (jhiUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jhiUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JhiUser{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
