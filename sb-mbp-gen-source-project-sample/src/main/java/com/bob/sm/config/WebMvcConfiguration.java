package com.bob.sm.config;

import com.google.common.net.HttpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/favicon.png")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(90, TimeUnit.DAYS));
        registry
                .addResourceHandler("/*.js", "/*.css", "/static/*.png", "/icons/*.png")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许向该服务器提交请求的URI，*表示全部允许
                .allowedOrigins("*")
                // 允许提交请求的方法，*表示全部允许
                .allowedMethods("*")
                // 允许向该服务器提交请求的URI，*表示全部允许
                .allowedHeaders("*")
                // 允许Cookie跨域，在做登录校验的时候有用
                .allowCredentials(true)
                // 允许访问的头信息
                .exposedHeaders(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)
                // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
                .maxAge(3600);
    }

}
