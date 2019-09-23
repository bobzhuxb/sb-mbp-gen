package ${packageName}.dto.help;

/**
 * 登录状态
 * @author Bob
 */
public class WxLoginStatusDTO extends ReturnCommonDTO {

    // 登录账号
    private String loginName;
    // openId用于识别用户
    private String openId;
    // 微信名
    private String wxName;
    // token信息
    private String token;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOpenId() { return openId; }

    public void setOpenId(String openId) { this.openId = openId; }

    public String getWxName() { return wxName; }

    public void setWxName(String wxName) { this.wxName = wxName; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
