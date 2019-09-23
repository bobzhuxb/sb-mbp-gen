package ${packageName}.dto.help;

/**
 * 腾讯地图详细地址（根据经纬度解析）
 * @author Bob
 */
public class TxMapFormattedAddressDTO {

    private String recommend;               // 经过腾讯地图优化过的描述方式，更具人性化特点

    private String rough;                   // 大致位置，可用于对位置的粗略描述

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getRough() {
        return rough;
    }

    public void setRough(String rough) {
        this.rough = rough;
    }
}
