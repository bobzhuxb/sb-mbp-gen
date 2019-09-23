package ${packageName}.dto.help;

import ${packageName}.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 微信JSAPI获取
 * @author Bob
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
