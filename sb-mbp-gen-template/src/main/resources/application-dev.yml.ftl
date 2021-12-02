logging:
    level:
        ROOT: INFO
        ${packageName}: INFO
    config: classpath:logback-spring-dev.xml

spring:
    datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: "jdbc:mysql://${dbIp}:${dbPort}/${dbName}?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8\
            &useSSL=false&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true"
        username: ${dbUsername}
        password: ${dbPassword}
    http:
        multipart:
            location: D:/localFile/${projectName}
    redis:
        database: 0   # Redis数据库索引（默认为0）
        host: 192.168.50.150    # Redis服务器地址
        port: 6379    # Redis服务器连接端口
        password:     # Redis服务器连接密码（默认为空）
        jedis:
            pool:
                max-active: 1024  # 连接池最大连接数（使用负值表示没有限制）
                max-wait: -1s    # 连接池最大阻塞等待时间（使用负值表示没有限制）
                max-idle: 8   # 连接池中的最大空闲连接
                min-idle: 0  # 连接池中的最小空闲连接
        timeout: 1s  # 连接超时时间
    cache:
        type: ehcache	# ehcache/redis

scheduling:
    enabled: true   # 定时任务开关

server:
    port: 8080

tools:
    security:
        authentication:
            jwt:
                # This token must be encoded using Base64 (you can type `echo 'secret-key'|base64` on your command line)
                base64-secret: MDYwNWY4MjVmZDM4ZGU4NWVlMTIxOGNlMTk5NzA1ZjhkM2I4NDliMzYwMzkxM2IxNTQzOGYwYzI4ZDhiOTMwNjVlMjQzZWFhMjNjOGZmNjhkYzY1M2M1ZjcwYzFhMjQ3NTQ0ZGMwZWE4MmNhYWY3MzQxY2RhMzBlNGJjOTViNjQ=
                # Token is valid 24 hours
                token-validity-in-seconds: 86400
                token-validity-in-seconds-for-remember-me: 2592000

api-pdf:
    generate: false

ms-wx:
    verify-token: hychG#$vd!qB8T@RZ5Z5sKCa5Xmqz7QY
    url: http://api.12465.top
