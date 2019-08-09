package com.bob.sm.dto.help;

import com.bob.sm.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * A DTO for the ParamWxJsApiDTO entity.
 */
public class ParamWxJsApiDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
