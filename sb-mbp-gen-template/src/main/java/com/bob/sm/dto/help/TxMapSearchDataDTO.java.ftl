package ${packageName}.dto.help;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 */
public class TxMapSearchDataDTO {

    private String id;                  // POI唯一标识

    private String title;               // 提示文字

    private String address;             // 地址

    private String province;            // 省

    private String city;                // 市

    private String adcode;              // 行政区划代码

    private Integer type;               // POI类型，值说明：0:普通POI / 1:公交车站 / 2:地铁站 / 3:公交线路 / 4:行政区划

    @JSONField(name = "_distance")
    private String distance;            // 传入location（定位坐标）参数时，返回定位坐标到各POI的距离

    private TxMapLocationDTO location;  // 提示所述位置坐标

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public TxMapLocationDTO getLocation() {
        return location;
    }

    public void setLocation(TxMapLocationDTO location) {
        this.location = location;
    }
}
