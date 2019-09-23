package ${packageName}.dto.help;

import java.io.Serializable;

/**
 * 微信OpenId获取的返回
 * @author Bob
 */
public class WxOpenIdResultDTO implements Serializable {

    private String errcode;
    private String errmsg;
    private String openid;
    private String session_key;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    @Override
    public String toString() {
        return "FsAppOpenIdResultDTO{" +
            "errcode=" + getErrcode() +
            "errmsg=" + getErrmsg() +
            "openid=" + getOpenid() +
            "session_key=" + getSession_key() +
            "}";
    }
}
