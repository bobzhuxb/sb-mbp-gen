package com.bob.sm.dto.help;

import java.util.List;

public class RemoteAuthorizationDTO {

    private String login;       // 用户名
    private String name;        // 姓名
    private List<String> authorities;   // 角色
    private String userType;    // 用户类别（给多用户系统使用）
    private String resourceType;    // 要访问的资源类型（包括权限组、子系统、页面、按钮、文件等）
    private String httpType;    // HTTP请求类型
    private String httpUrl;     // HTTP请求URL
    private String systemCode;  // 应用（子系统）代码

    private String authorizationResult;  // 授权结果（PASS：已授权  FAIL：未授权）
    private String errInfo;     // 授权失败的返回消息
    private String token;       // 用户新增或修改得到的token信息

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getAuthorizationResult() {
        return authorizationResult;
    }

    public void setAuthorizationResult(String authorizationResult) {
        this.authorizationResult = authorizationResult;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
