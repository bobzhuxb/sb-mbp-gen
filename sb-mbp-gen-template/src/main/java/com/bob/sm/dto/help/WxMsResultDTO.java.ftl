package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 远程微服务（封装腾讯服务）的返回
 * @author Bob
 */
public class WxMsResultDTO<T> {

    @GenComment("状态（0：成功  1：失败）")
    private Integer code;

    @GenComment("信息")
    private String msg;

    @GenComment("微信的实际返回")
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
