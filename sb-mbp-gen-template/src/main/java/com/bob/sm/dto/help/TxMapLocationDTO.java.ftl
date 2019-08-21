package ${packageName}.dto.help;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 */
public class TxMapLocationDTO {

    private String lat;             // 纬度

    private String lng;             // 	经度

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
