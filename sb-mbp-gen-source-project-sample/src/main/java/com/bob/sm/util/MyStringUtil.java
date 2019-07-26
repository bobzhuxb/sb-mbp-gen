package com.bob.sm.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Random;

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

    /**
     * java生成随机数字和字母组合10位数
     * @param length 生成随机数的长度
     * @return
     */
    public static String getRandomNickname(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 获取拼音首字母（大写）
     * @param name 汉字
     * @return 拼音首字母
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPinyinShouzimu(String name) throws BadHanyuPinyinOutputFormatCombination {
        if (name == null) {
            return null;
        }
        char[] charArray = name.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 设置大小写格式
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 设置声调格式：
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < charArray.length; i++) {
            // 匹配中文,非中文转换会转换成null
            if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                if (hanyuPinyinStringArray != null) {
                    pinyin.append(hanyuPinyinStringArray[0].charAt(0));
                }
            }
        }
        return pinyin.toString();
    }

}
