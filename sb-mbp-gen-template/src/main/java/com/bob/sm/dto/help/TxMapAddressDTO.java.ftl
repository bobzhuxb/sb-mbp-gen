package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapAddressDTO {

    @GenComment("地址")
    private String address;

    @GenComment("位置描述")
    @JSONField(name = "formatted_addresses")
    private TxMapFormattedAddressDTO formattedAddress;

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
