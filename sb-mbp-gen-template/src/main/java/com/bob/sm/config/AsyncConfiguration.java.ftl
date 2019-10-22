package ${packageName}.config;

import ${packageName}.config.help.ExceptionHandlingAsyncTaskExecutor;
import ${packageName}.config.help.PropertiesHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 异步配置
 * @author Bob
 */
@Configuration
@EnableConfigurationProperties(PropertiesHelp.class)
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    private final PropertiesHelp propertiesHelp;

    public AsyncConfiguration(PropertiesHelp propertiesHelp) {
        this.propertiesHelp = propertiesHelp;
    }

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(propertiesHelp.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(propertiesHelp.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(propertiesHelp.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix("base-Executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledTaskExecutor());
    }

    @Bean
    public Executor scheduledTaskExecutor() {
        return Executors.newScheduledThreadPool(propertiesHelp.getAsync().getCorePoolSize());
    }
}
