package ${packageName}.util;

/**
 * 字符串工具类
 */
public class MyStringUtil {

    /**
     * 首字母转小写
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 驼峰标识转下划线标识
     * @param camelName 驼峰标识
     * @return 下划线标识
     */
    public static String camelToUnderline(String camelName) {
        if (null == camelName || "".equals(camelName)) {
            return camelName;
        }
        String underlineName = "";
        char[] camelChars = camelName.toCharArray();
        for (int i = 0; i < camelChars.length; i++) {
            char c = camelChars[i];
            if (c >= 'A' && c <= 'Z') {
                underlineName += "_" + (char)(c + 32);
            } else {
                underlineName += c;
            }
        }
        if (underlineName.startsWith("_")) {
            underlineName = underlineName.substring(1);
        }
        return underlineName;
    }

}
