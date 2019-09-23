package ${packageName}.dto.help;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class ReturnMapAddressDTO {

    private String address;             // 详细地址

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
