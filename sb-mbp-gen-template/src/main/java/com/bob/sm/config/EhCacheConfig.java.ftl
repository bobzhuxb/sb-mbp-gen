package ${packageName}.config;

import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 * @author Bob
 */
@Configuration
public class EhCacheConfig {

    // 此处交由Spring管理
//    @Bean
//    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
//        return new EhCacheCacheManager(bean.getObject());
//    }
}
