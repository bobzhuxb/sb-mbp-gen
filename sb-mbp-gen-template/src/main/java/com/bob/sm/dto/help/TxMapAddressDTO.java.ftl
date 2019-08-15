package ${packageName}.dto.help;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 */
public class TxMapAddressDTO {

    private String address;             // 地址

    @JSONField(name = "formatted_addresses")
    private TxMapFormattedAddressDTO formattedAddress;  // 位置描述

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TxMapFormattedAddressDTO getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(TxMapFormattedAddressDTO formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
