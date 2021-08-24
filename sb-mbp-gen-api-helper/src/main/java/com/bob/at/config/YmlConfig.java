package com.bob.at.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration of spring boot configuration in yml file
 * @author Bob
 */
@Component
@ConfigurationProperties
public class YmlConfig {

    /**
     * 本地文件存放绝对路径
     */
    @Value("${spring.http.multipart.location}")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
