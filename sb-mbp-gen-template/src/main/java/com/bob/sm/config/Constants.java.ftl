package ${packageName}.config;

/**
 * 常量类
 * @author Bob
 */
public final class Constants {

    // 环境配置
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /**
     * 默认的URL前缀
     */
    public static final String URL_DEFAULT_PREFIX = "${urlPrefix}";

    /**
     * 上传的文件指定相对路径
     */
    public static final String FILE_UPLOAD_RELATIVE_PATH = "upload";

    /**
     * HTTP请求的附带属性（请求到达时间）
     */
    public static final String REQUEST_ATTR_START_TIME = "startMillis";

    /**
     * HTTP请求时延过高的阈值（单位：毫秒，用于记录WARN日志）
     */
    public static final long REQUEST_DELAY_THRESHOLD_MILLIS = 10000;
	
    /**
     * 密码有效天数
     */
    public static final long PASSWORD_VALID_DAYS = 180;

    // ================ 角色 start ========================
    /**
     * 总角色起始标识
     */
    public static final String ROLE_FATHER_START = "ROLE_FATHER";
    /**
     * 子角色起始标识
     */
    public static final String ROLE_CHILD_START = "ROLE_CHILD";
    /**
     * 其他角色标识
     */
    public static final String ROLE_OTHER = "ROLE_OTHER";
    /**
     * 无角色（未登录用户）标识
     */
    public static final String ROLE_NONE = "ROLE_NONE";
	
    // ================ 缓存 start ========================
    /**
     * 缓存实现的Service名
     * 取值范围：cacheRedisService/cacheLocalService
     * 不使用cacheRedisService时，请将CacheRedisServiceImpl.java的@Service注解去掉
     */
    public static final String SERVICE_CACHE_NAME = "cacheRedisService";
    /**
     * 用户信息缓存
     */
    public static final String CACHE_USER_INFO = "userInfo";

    /************ 微信登录配置 *************/
    /**
     * 微服务：小程序获取openId的URL
     */
    public static final String MS_WXAPP_OPEN_ID_URL = "/api/v1/wx/get-openid";
    /**
     * 微服务：小程序获取jsapi的URL
     */
    public static final String MS_WXAPP_JSAPI_URL = "/api/v1/wx/get-jsapi";
    /**
     * 微服务：经纬度转地址描述
     */
    public static final String MS_TXMAP_REVERSE_ADDRESS_URL = "/api/v1/wx/get-address-detail";
    /**
     * 微服务：地点关键字搜索
     */
    public static final String MS_TXMAP_KEYWORD_SEARCH_URL = "/api/v1/wx/search-area";

    /**
     * 整数的正则
     */
    public static final String REGEX_INTEGER_ALL = "^[-\\+]?[\\d]*$";

    // ================ 其他常量 start ========================

    /**
     * 通用Enum接口
     */
    public interface EnumInter {
        Object getValue();
        String getName();
    }

    /**
     * 是否
     */
    public enum yesNo implements EnumInter {
        YES(1, "是"), NO(2, "否");
        private Integer value;
        private String name;
        private yesNo(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public Integer getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 系统日志类别
     */
    public enum systemLogType implements EnumInter {
        LOGIN("LOGIN", "登录"), LOGOUT("LOGOUT", "登出"), CHANGE_PASSWORD("CHANGE_PASSWORD", "修改密码");
        private String value;
        private String name;
        private systemLogType(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
        public String getNameByValue(String value) {
            if (value == null) {
                return "";
            }
            for (systemLogType data : systemLogType.values()) {
                if (value.equals(data.getValue())) {
                    return data.name;
                }
            }
            return "";
        }
    }

    /**
     * 系统中的角色
     */
    public enum role implements EnumInter {
        ROLE_ADMIN("ROLE_ADMIN", "管理员"), ROLE_FATHER("ROLE_FATHER", "父角色"), ROLE_CHILD("ROLE_CHILD", "子角色");
        private String value;
        private String name;
        private role(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 系统通用返回状态
     */
    public enum commonReturnStatus implements EnumInter {
        SUCCESS("1", "操作成功"), FAIL("2", "操作失败");
        private String value;
        private String name;
        private commonReturnStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 返回的数据类型
     */
    public enum returnDataType implements EnumInter {
        OBJECT("object", "普通对象"), LIST("list", "数组对象"), PAGE("page", "分页对象"),
        INTEGER("integer", "整数");
        private String value;
        private String name;
        private returnDataType(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 微信登录认证返回状态
     */
    public enum wxLoginResultStatus implements EnumInter {
        SUCCESS("1", "认证成功"), WX_SERVER_NOT_CONNECT("2", "无法连接微信服务器"),
        VERIFY_FAIL("3", "验证失败"), USER_SAVE_FAIL("4", "用户信息录入失败"), OTHER_ERROR("5", "其他错误");
        private String value;
        private String name;
        private wxLoginResultStatus(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 级联删除类型
     */
    public enum cascadeDeleteType implements EnumInter {
        DELETE("DELETE", "级联删除"), FORBIDDEN("FORBIDDEN", "禁止删除"), NULL("NULL", "级联置空");
        private String value;
        private String name;
        private cascadeDeleteType(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 实际数据类型（BaseService中使用）
     */
    public enum baseRealFieldType implements EnumInter {
        DOUBLE("Double", "double型"), FLOAT("Float", "float型"), INTEGER("Integer", "int型"), LONG("Long", "long型");
        private String value;
        private String name;
        private baseRealFieldType(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 启用状态
     */
    public enum enabled implements EnumInter {
        ENABLED(1, "启用"), DISABLED(2, "停用");
        private Integer value;
        private String name;
        private enabled(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public Integer getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * 用户操作码
     */
    public enum operationCode implements EnumInter {
        ADD("ADD", "新增"), MODIFY("MODIFY", "修改"), DELETE("DELETE", "删除");
        private String value;
        private String name;
        private operationCode(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    /**
     * appendParamMap的Key
     */
    public enum appendParamMapKey implements EnumInter {
        USER_INFO_DETAIL("userInfoDetailDTO", "当前用户明细信息"), ROLE_NAME("roleName", "角色标识");
        private String value;
        private String name;
        private appendParamMapKey(String value, String name) {
            this.value = value;
            this.name = name;
        }
        @Override
        public String getValue(){
            return value;
        }
        @Override
        public String getName(){
            return name;
        }
    }

    private Constants() {
    }
}
