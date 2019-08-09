package ${packageName}.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of spring boot configuration in yml file
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
     * 本机URL前缀
     */
    @Value("${r'${self.url-prefix}'}")
    private String selfUrlPrefix;

    /**
     * 微信公众号APP_ID
     */
    @Value("${r'${wx.app-id}'}")
    private String wxAppId;

    /**
     * 微信公众号APP_SECRET
     */
    @Value("${r'${wx.app-secret}'}")
    private String wxAppSecret;

    /**
     * 微信ACCESS_TOKEN获取是否打开
     */
    @Value("${r'${wx.access-token-switch}'}")
    private String wxAccessTokenSwitch;

    public String getLocation() {
        return location;
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

    public String getSelfUrlPrefix() {
        return selfUrlPrefix;
    }

    public void setSelfUrlPrefix(String selfUrlPrefix) {
        this.selfUrlPrefix = selfUrlPrefix;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public String getWxAccessTokenSwitch() {
        return wxAccessTokenSwitch;
    }

    public void setWxAccessTokenSwitch(String wxAccessTokenSwitch) {
        this.wxAccessTokenSwitch = wxAccessTokenSwitch;
    }
}
