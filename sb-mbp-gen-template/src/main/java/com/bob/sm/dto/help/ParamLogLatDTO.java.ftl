package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import ${packageName}.annotation.validation.ValidLonLat;
import ${packageName}.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 经纬度
 * @author Bob
 */
public class ParamLogLatDTO {

    @GenComment("经度")
    @NotBlank
    @ValidLonLat
    private String longitude;

    @GenComment("纬度")
    @NotBlank
    @ValidLonLat
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
