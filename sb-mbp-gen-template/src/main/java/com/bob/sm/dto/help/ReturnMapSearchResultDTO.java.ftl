package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class ReturnMapSearchResultDTO {

    @GenComment("提示文字")
    private String title;

    @GenComment("地址")
    private String address;

    @GenComment("省")
    private String province;

    @GenComment("市")
    private String city;

    @GenComment("经度")
    private String longitude;

    @GenComment("纬度")
    private String latitude;

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
