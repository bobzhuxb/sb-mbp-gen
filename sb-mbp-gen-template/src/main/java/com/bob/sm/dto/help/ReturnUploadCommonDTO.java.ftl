package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;
import ${packageName}.config.Constants;

/**
 * 文件上传返回的共通类
 * @author Bob
 */
public class ReturnUploadCommonDTO<T> extends ReturnCommonDTO<T> {

    @GenComment("返回状态")
    private String code;

    public ReturnUploadCommonDTO(String code) {
        this.code = String.valueOf(Integer.parseInt(code) - 1);
        super.setResultCode(code);
    }

    public ReturnUploadCommonDTO(String code, String errMsg) {
        this(code);
        super.setErrMsg(errMsg);
    }

    public ReturnUploadCommonDTO(String code, String errMsg, T data) {
        this(code, errMsg);
        super.setData(data);
    }

    public ReturnUploadCommonDTO(T data) {
        this(Constants.commonReturnStatus.SUCCESS.getValue(), null, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
