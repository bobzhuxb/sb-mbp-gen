package ${packageName}.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of spring boot configuration in yml file
 * @author Bob
 */
@Component
@ConfigurationProperties
public class YmlConfig {

    /**
     * 本地文件存放绝对路径
     */
    @Value("${r'${spring.http.multipart.location}'}")
    private String location;

    /**
     * 是否生成PDF格式的API文档
     */
    @Value("${r'${api-pdf.generate}'}")
    private String apiPdfGenerate;

    /**
     * 启动或关闭图片压缩
     */
    @Value("${r'${app.pic-compress-switch}'}")
    private String picCompressSwitch;

    /**
     * 图片压缩后限制的最大大小
     */
    @Value("${r'${app.pic-compress-max-size}'}")
    private String picCompressMaxSize;

    /**
     * 图片压缩的最大次数限制
     */
    @Value("${r'${app.pic-compress-max-times}'}")
    private String picCompressMaxTimes;

    /**
     * 每次图片压缩图片的大小（长宽），范围0到1，1f就是原图大小，0.5就是原图的一半大小
     */
    @Value("${r'${app.pic-compress-scale}'}")
    private String picCompressScale;

    /**
     * 图片压缩的质量，范围0到1，越接近于1质量越好，越接近于0质量越差
     */
    @Value("${r'${app.pic-compress-quality}'}")
    private String picCompressQuality;

    /**
     * 微服务：微信服务的token验证
     */
    @Value("${r'${ms-wx.verify-token}'}")
    private String msWxVerifyToken;

    /**
     * 微服务：微信服务的URL
     */
    @Value("${r'${ms-wx.url}'}")
    private String msWxUrl;

    public String getLocation() {
        return location + File.separator + Constants.FILE_UPLOAD_RELATIVE_PATH;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getApiPdfGenerate() {
        return apiPdfGenerate;
    }

    public void setApiPdfGenerate(String apiPdfGenerate) {
        this.apiPdfGenerate = apiPdfGenerate;
    }

    public String getPicCompressSwitch() {
        return picCompressSwitch;
    }

    public void setPicCompressSwitch(String picCompressSwitch) {
        this.picCompressSwitch = picCompressSwitch;
    }

    public String getPicCompressMaxSize() {
        return picCompressMaxSize;
    }

    public void setPicCompressMaxSize(String picCompressMaxSize) {
        this.picCompressMaxSize = picCompressMaxSize;
    }

    public String getPicCompressMaxTimes() {
        return picCompressMaxTimes;
    }

    public void setPicCompressMaxTimes(String picCompressMaxTimes) {
        this.picCompressMaxTimes = picCompressMaxTimes;
    }

    public String getPicCompressScale() {
        return picCompressScale;
    }

    public void setPicCompressScale(String picCompressScale) {
        this.picCompressScale = picCompressScale;
    }

    public String getPicCompressQuality() {
        return picCompressQuality;
    }

    public void setPicCompressQuality(String picCompressQuality) {
        this.picCompressQuality = picCompressQuality;
    }

    public String getMsWxVerifyToken() {
        return msWxVerifyToken;
    }

    public void setMsWxVerifyToken(String msWxVerifyToken) {
        this.msWxVerifyToken = msWxVerifyToken;
    }

    public String getMsWxUrl() {
        return msWxUrl;
    }

    public void setMsWxUrl(String msWxUrl) {
        this.msWxUrl = msWxUrl;
    }
}
