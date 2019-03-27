package com.bob.sm.config;

/**
 * Application constants.
 */
public final class Constants {

    // 环境配置
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    // 缓存配置
    public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";

    /************ ehcache 配置 *************/
    public static final String EHCACHE_USER_PERMISSION = "systemUserPermission";  // 用户权限

    /************ 微信登录配置 *************/
    // 小程序获取openId的URL
    public static final String WXAPP_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";
    public static final String WXAPP_GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    public static final String WXAPP_GET_QRCODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";
    // 小程序APP ID
    public static final String WXAPP_ID = "wx2ba49b048f0aba0b";
    // 小程序APP密码
    public static final String WXAPP_SECRET = "b6242b7342e74e9b6595cf1e60c13ec6";
    // 小程序APP授权类型（获取认证码）
    public static final String WXAPP_GRANT_TYPE = "authorization_code";

    // 通用Enum接口
    public interface EnumInter {
        Object getValue();
        String getName();
    }

    // 是否
    public enum yesNo implements EnumInter {
        YES(1, "是"), NO(2, "否");
        private Integer value;
        private String name;
        private yesNo(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        public Integer getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 系统中的角色
    public enum role implements EnumInter {
        ROLE_ADMIN("ROLE_ADMIN", "管理员"), ROLE_USER("ROLE_USER", "普通用户");
        private String value;
        private String name;
        private role(String value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 操作许可名称是否已更改
    public enum permissionNameModified implements EnumInter {
        NO(1, "未更改"), YES(2, "已更改");
        private int value;
        private String name;
        private permissionNameModified(int value, String name) {
            this.value = value;
            this.name = name;
        }
        public Integer getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 操作许可是否允许被配置用于权限或用户
    public enum permissionAllowConfig implements EnumInter {
        YES(1, "允许配置"), NO(2, "不允许配置");
        private Integer value;
        private String name;
        private permissionAllowConfig(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        public Integer getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 系统通用返回状态
    public enum commonReturnStatus implements EnumInter {
        SUCCESS("1", "操作成功"), FAIL("2", "操作失败");
        private String value;
        private String name;
        private commonReturnStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 微信登录认证返回状态
    public enum wxLoginResultStatus implements EnumInter {
        SUCCESS("1", "认证成功"), WX_SERVER_NOT_CONNECT("2", "无法连接微信服务器"),
        VERIFY_FAIL("3", "验证失败"), USER_SAVE_FAIL("4", "用户信息录入失败"), OTHER_ERROR("5", "其他错误");
        private String value;
        private String name;
        private wxLoginResultStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    // 级联删除类型
    public enum cascadeDeleteType implements EnumInter {
        DELETE("DELETE", "级联删除"), FORBIDDEN("FORBIDDEN", "禁止删除"), NULL("NULL", "级联置空");
        private String value;
        private String name;
        private cascadeDeleteType(String value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getValue(){
            return value;
        }
        public String getName(){
            return name;
        }
    }

    private Constants() {
    }
}
