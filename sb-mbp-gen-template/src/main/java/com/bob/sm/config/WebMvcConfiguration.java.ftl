package ${packageName}.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.net.HttpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Web Mvc配置
 * @author Bob
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/favicon.png")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(90, TimeUnit.DAYS));
        registry
                .addResourceHandler("/*.js", "/*.css")
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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 调用父类的配置
        WebMvcConfigurer.super.configureMessageConverters(converters);
        // 创建FastJson的消息转换器
        FastJsonHttpMessageConverter convert = new FastJsonHttpMessageConverter();
        convert.setSupportedMediaTypes(getSupportedMediaTypes());
        converters.add(convert);
    }

    private List<MediaType> getSupportedMediaTypes() {
        // 创建消息转换器
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        supportedMediaTypes.add(MediaType.ALL);
        return supportedMediaTypes;
    }

}
