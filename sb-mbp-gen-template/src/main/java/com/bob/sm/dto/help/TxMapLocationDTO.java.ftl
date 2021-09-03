package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapLocationDTO {

    @GenComment("纬度")
    private String lat;

    @GenComment("经度")
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
