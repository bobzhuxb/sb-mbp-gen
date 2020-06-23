spring:
    application:
        name: ${projectName}
    profiles:
        active: dev
    resources:
        static-locations: classpath:/META-INF/resources/,classpath:/resources/, classpath:/static/, classpath:/inter/, classpath:/public/, file:${r'${spring.http.multipart.location}'}
    servlet:
        multipart:
            max-request-size: -1
            max-file-size: -1
    cache:
        type: ehcache   # （redis/ehcache）
        # 不使用Redis时，请将RedisConfig.java的@Configuration注解去掉
        ehcache:
            config: classpath:ehcache.xml

server:
    compression:
        enabled: true
        mime-types: text/html,text/xml,text/plain,text/css,text/javascript,application/javascript
        min-response-size: 2048

# mybatis plus配置
mybatis-plus:
    global-config:
        db-config:
            id-type: id_worker_str
    mapper-locations: classpath:/mapper/*Mapper.xml

tools:
    async:
        core-pool-size: 2
        max-pool-size: 50
        queue-capacity: 10000

app:
    pic-compress-switch: close  # 启动或关闭图片压缩
    pic-compress-max-size: 51200  # 图片压缩后size最大值（字节）
    pic-compress-max-times: 1  # 图片压缩最大次数（暂不循环压缩）
    pic-compress-scale: 1  # 每次图片压缩图片的大小（长宽），范围0到1，1f就是原图大小，0.5就是原图的一半大小
    pic-compress-quality: 0.5  # 每次图片压缩的质量，范围0到1，越接近于1质量越好，越接近于0质量越差
