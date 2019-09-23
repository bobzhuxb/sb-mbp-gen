package ${packageName}.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * MyBatic Plus配置.
 * @author Bob
 */
@EnableTransactionManagement
@Configuration
@MapperScan("${packageName}.mapper")
public class MybatisPlusConfig {

    @Bean
    public PerformanceInterceptor performanceInterceptor(){
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
//        //格式化sql语句
//        Properties properties = new Properties();
//        properties.setProperty("format", "true");
//        interceptor.setProperties(properties);
        return interceptor;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
