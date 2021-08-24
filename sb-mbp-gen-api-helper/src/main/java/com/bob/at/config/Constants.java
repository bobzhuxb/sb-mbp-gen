package com.bob.at.config;

/**
 * 常量类
 * @author Bob
 */
public class Constants {

    /**
     * 系统通用返回状态
     */
    public enum commonReturnStatus {
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

}
