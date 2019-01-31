package com.bob.sm.dto.help;

import com.bob.sm.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the FsFileHistory entity.
 */
public class ParamWxOpenIdDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String jsCode;

    @NotBlank
    @Size(min = 1, max = 255)
    private String wxName;

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }

    public String getWxName() { return wxName; }

    public void setWxName(String wxName) { this.wxName = wxName; }

    @Override
    public String toString() {
        return "ParamWxOpenIdDTO{" +
            "jsCode=" + getJsCode() +
            ", wxName='" + getWxName() + "'" +
            "}";
    }
}
