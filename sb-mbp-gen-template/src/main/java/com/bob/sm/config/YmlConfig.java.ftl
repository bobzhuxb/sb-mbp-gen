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

    //====================远程调用相关参数 start=======================//
    /**
     * 是否开启远程权限系统认证
     */
    @Value("${r'${microservice.remote-authorization}'}")
    private String remoteAuthorization;

    /**
     * 权限系统接口协议前缀
     */
    @Value("${r'${microservice.remote-protocol-prefix}'}")
    private String remoteProtocolPrefix;

    /**
     * 权限系统IP
     */
    @Value("${r'${microservice.authorization-ip}'}")
    private String remoteAuthorizationIp;

    /**
     * 权限系统端口号
     */
    @Value("${r'${microservice.authorization-port}'}")
    private String remoteAuthorizationPort;

    /**
     * 权限系统远程登录认证URL
     */
    @Value("${r'${microservice.remote-authenticate-url}'}")
    private String authenticateUrl;

    /**
     * 权限系统远程授权权限URL
     */
    @Value("${r'${microservice.remote-authorization-url}'}")
    private String authorizationUrl;

    /**
     * 权限系统远程注册权限许可信息URL
     */
    @Value("${r'${microservice.remote-register-permission-url}'}")
    private String registerPermissionUrl;

    /**
     * 权限系统远程加载当前用户数据URL
     */
    @Value("${r'${microservice.remote-load-current-user-url}'}")
    private String loadCurrentUserUrl;

    /**
     * 权限系统远程删除用户URL
     */
    @Value("${r'${microservice.remote-delete-user-url}'}")
    private String deleteUserUrl;

    /**
     * 权限系统远程新增或更新用户URL
     */
    @Value("${r'${microservice.remote-add-or-update-user-url}'}")
    private String addOrUpdateUserUrl;

    /**
     * 权限系统远程获取全量数据字典
     */
    @Value("${r'${microservice.remote-load-dictionary-url}'}")
    private String loadDictionaryUrl;
    //====================远程调用相关参数 end=======================//

    public String getRemoteAuthorization() {
        return remoteAuthorization;
    }

    public void setRemoteAuthorization(String remoteAuthorization) {
        this.remoteAuthorization = remoteAuthorization;
    }

    public String getRemoteProtocolPrefix() {
        return remoteProtocolPrefix;
    }

    public void setRemoteProtocolPrefix(String remoteProtocolPrefix) {
        this.remoteProtocolPrefix = remoteProtocolPrefix;
    }

    public String getRemoteAuthorizationIp() {
        return remoteAuthorizationIp;
    }

    public void setRemoteAuthorizationIp(String remoteAuthorizationIp) {
        this.remoteAuthorizationIp = remoteAuthorizationIp;
    }

    public String getRemoteAuthorizationPort() {
        return remoteAuthorizationPort;
    }

    public void setRemoteAuthorizationPort(String remoteAuthorizationPort) {
        this.remoteAuthorizationPort = remoteAuthorizationPort;
    }

    public String getAuthenticateUrl() {
        return authenticateUrl;
    }

    public void setAuthenticateUrl(String authenticateUrl) {
        this.authenticateUrl = authenticateUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getRegisterPermissionUrl() {
        return registerPermissionUrl;
    }

    public void setRegisterPermissionUrl(String registerPermissionUrl) {
        this.registerPermissionUrl = registerPermissionUrl;
    }

    public String getLoadCurrentUserUrl() {
        return loadCurrentUserUrl;
    }

    public void setLoadCurrentUserUrl(String loadCurrentUserUrl) {
        this.loadCurrentUserUrl = loadCurrentUserUrl;
    }

    public String getDeleteUserUrl() {
        return deleteUserUrl;
    }

    public void setDeleteUserUrl(String deleteUserUrl) {
        this.deleteUserUrl = deleteUserUrl;
    }

    public String getAddOrUpdateUserUrl() {
        return addOrUpdateUserUrl;
    }

    public void setAddOrUpdateUserUrl(String addOrUpdateUserUrl) {
        this.addOrUpdateUserUrl = addOrUpdateUserUrl;
    }

    public String getLoadDictionaryUrl() {
        return loadDictionaryUrl;
    }

    public void setLoadDictionaryUrl(String loadDictionaryUrl) {
        this.loadDictionaryUrl = loadDictionaryUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
