package ${packageName}.dto.help;

import ${packageName}.annotation.validation.ValidLonLat;
import ${packageName}.dto.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 经纬度
 */
public class ParamLogLatDTO {

    @NotBlank
    @ValidLonLat
    private String longitude;       // 经度

    @NotBlank
    @ValidLonLat
    private String latitude;        // 纬度

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
