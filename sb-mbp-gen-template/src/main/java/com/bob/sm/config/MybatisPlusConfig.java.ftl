package ${packageName}.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis Plus配置
 * @author Bob
 */
@EnableTransactionManagement
@Configuration
@MapperScan("${packageName}.mapper")
public class MybatisPlusConfig {

    @Autowired
    private YmlConfig ymlConfig;

    @Bean
    public PerformanceInterceptor performanceInterceptor(){
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        if ("log".equals(ymlConfig.getMybatisPlusLogWay())) {
            // 设置写入日志（不设置此句则只会输出到控制台）
            interceptor.setWriteInLog(true);
        }
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
