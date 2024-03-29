package ${packageName}.dto.help;

import ${packageName}.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 微信OpenId获取
 * @author Bob
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
