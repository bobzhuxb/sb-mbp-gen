package com.bob.freemarker.util;

public class StringUtil {

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

    /**
     * 驼峰标识转全小写标识
     * @param camelName 驼峰标识
     * @return 全小写标识
     */
    public static String camelToLower(String camelName) {
        if (null == camelName || "".equals(camelName)) {
            return camelName;
        }
        String lowerName = "";
        char[] camelChars = camelName.toCharArray();
        for (int i = 0; i < camelChars.length; i++) {
            char c = camelChars[i];
            if (c >= 'A' && c <= 'Z') {
                lowerName += (char)(c + 32);
            } else {
                lowerName += c;
            }
        }
        return lowerName;
    }

    /**
     * 驼峰标识转中划线标识
     * @param camelName 驼峰标识
     * @return 中划线标识
     */
    public static String camelToCenterline(String camelName) {
        if (null == camelName || "".equals(camelName)) {
            return camelName;
        }
        String underlineName = "";
        char[] camelChars = camelName.toCharArray();
        for (int i = 0; i < camelChars.length; i++) {
            char c = camelChars[i];
            if (c >= 'A' && c <= 'Z') {
                underlineName += "-" + (char)(c + 32);
            } else {
                underlineName += c;
            }
        }
        if (underlineName.startsWith("-")) {
            underlineName = underlineName.substring(1);
        }
        return underlineName;
    }

    /**
     * 驼峰标识转首字母大写
     * @param camelName 驼峰标识
     * @return 首字母大写的驼峰标识
     */
    public static String camelToUpperFirst(String camelName) {
        if (null == camelName || "".equals(camelName)) {
            return camelName;
        }
        return camelName.substring(0, 1).toUpperCase() + camelName.substring(1);
    }

}
