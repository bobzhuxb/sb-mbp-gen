package ${packageName}.util;

public class StringUtil {

    /**
     * 验证字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        return input == null || input.equals("") || input.trim().equals("");
    }

    /**
     * 下划线标识转驼峰标识
     * @param underlineName 下划线标识
     * @return 驼峰标识
     */
    public static String underlineToCamel(String underlineName) {
        if (null == underlineName || "".equals(underlineName)) {
            return underlineName;
        }
        String camelName = "";
        char[] underlineChars = underlineName.toCharArray();
        for (int i = 0; i < underlineChars.length; i++) {
            char c = underlineChars[i];
            char cNextUpper = 0;
            if (i != underlineChars.length - 1) {
                // 当前不是最后一个字符
                char cNext = underlineChars[i + 1];
                if (cNext >= 'a' && cNext <= 'z') {
                    cNextUpper = (char)(cNext - 32);
                } else {
                    cNextUpper = cNext;
                }
            }
            if (c == '_') {
                // 当前是下划线
                if (cNextUpper != 0) {
                    camelName += cNextUpper;
                    i++;
                }
            } else {
                camelName += c;
            }
        }
        return camelName;
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
        while (underlineName.startsWith("_")) {
            underlineName = underlineName.substring(1);
        }
        return underlineName;
    }

}
