package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class ReturnMapAddressDTO {

    @GenComment("详细地址")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
