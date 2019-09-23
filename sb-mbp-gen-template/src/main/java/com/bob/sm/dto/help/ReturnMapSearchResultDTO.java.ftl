package ${packageName}.dto.help;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class ReturnMapSearchResultDTO {

    private String title;               // 提示文字

    private String address;             // 地址

    private String province;            // 省

    private String city;                // 市

    private String longitude;           // 经度

    private String latitude;            // 纬度

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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
