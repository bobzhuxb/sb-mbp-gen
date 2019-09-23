package ${packageName}.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Bean操作工具类
 * @author Bob
 */
public class StringUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

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

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * SHA签名
     * @param data 待签名字符串
     * @return
     * @throws Exception
     */
    public static String sha1(String data) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(data.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
