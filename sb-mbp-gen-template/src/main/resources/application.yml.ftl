spring:
    application:
        name: myproject
    profiles:
        active: dev
    resources:
        static-locations: classpath:/META-INF/resources/,classpath:/resources/, classpath:/static/, classpath:/public/, file:${r'${spring.http.multipart.location}'}
    servlet:
        multipart:
            max-request-size: -1
            max-file-size: -1

management:
    endpoints:
        web:
            base-path: /management
            exposure:
                include: ["configprops", "env", "health", "info", "threaddump", "logfile" ]
    endpoint:
        health:
            show-details: when_authorized
    info:
        git:
            mode: full
    health:
        mail:
            enabled: false
    metrics:
        enabled: false

# mybatis plus配置
mybatis-plus:
    global-config:
        db-config:
            id-type: auto
    mapper-locations: classpath:/mapper/*Mapper.xml

jhipster:
    async:
        core-pool-size: 2
        max-pool-size: 50
        queue-capacity: 10000
    # By default CORS is disabled. Uncomment to enable.
    #cors:
        #allowed-origins: "*"
        #allowed-methods: "*"
        #allowed-headers: "*"
        #exposed-headers: "Authorization,Link,X-Total-Count"
        #allow-credentials: true
        #max-age: 1800
    mail:
        from: base@localhost
    swagger:
        default-include-pattern: /api/.*
        title: base API
        description: API接口
        version: 0.0.1
        terms-of-service-url:
        contact-name:
        contact-url:
        contact-email:
        license:
        license-url:

microservice:
    remote-authenticate-url: /baseapi/authenticate
    remote-authorization-url: /baseapi/remote/authorization
    remote-register-permission-url: /baseapi/remote/register-permission
    remote-load-current-user-url: /baseapi/remote/load-current-user
    remote-delete-user-url: /baseapi/remote/delete-user
    remote-add-or-update-user-url: /baseapi/remote/add-or-update-user
    remote-load-dictionary-url: /baseapi/remote/load-dictionary

app:
    pic-compress-switch: close  # 启动或关闭图片压缩
    pic-compress-max-size: 51200  # 图片压缩后size最大值（字节）
    pic-compress-max-times: 1  # 图片压缩最大次数（暂不循环压缩）
    pic-compress-scale: 1  # 每次图片压缩图片的大小（长宽），范围0到1，1f就是原图大小，0.5就是原图的一半大小
    pic-compress-quality: 0.5  # 每次图片压缩的质量，范围0到1，越接近于1质量越好，越接近于0质量越差
