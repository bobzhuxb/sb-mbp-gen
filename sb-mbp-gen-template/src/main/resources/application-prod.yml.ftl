logging:
    level:
        ROOT: INFO
        ${packageName}: INFO
        com.ts.wxpay: INFO

spring:
    datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/sbmbp?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false
        username: root
        password: 123456
    http:
        multipart:
            location: /data/sbmbp

server:
    port: 8080

jhipster:
    http:
        version: V_1_1 # To use HTTP/2 you will need to activate TLS (see application-tls.yml)
    cache: # Cache configuration
        ehcache: # Ehcache configuration
            time-to-live-seconds: 3600 # By default objects stay 1 hour in the cache
            max-entries: 100 # Number of objects in each cache entry
    # CORS is only enabled by default with the "dev" profile, so BrowserSync can access the API
    cors:
        allowed-origins: "*"
        allowed-methods: "*"
        allowed-headers: "*"
        exposed-headers: "Authorization,Link,X-Total-Count"
        allow-credentials: true
        max-age: 1800
    security:
        authentication:
            jwt:
                # This token must be encoded using Base64 (you can type `echo 'secret-key'|base64` on your command line)
                base64-secret: MDYwNWY4MjVmZDM4ZGU4NWVlMTIxOGNlMTk5NzA1ZjhkM2I4NDliMzYwMzkxM2IxNTQzOGYwYzI4ZDhiOTMwNjVlMjQzZWFhMjNjOGZmNjhkYzY1M2M1ZjcwYzFhMjQ3NTQ0ZGMwZWE4MmNhYWY3MzQxY2RhMzBlNGJjOTViNjQ=
                # Token is valid 24 hours
                token-validity-in-seconds: 86400
                token-validity-in-seconds-for-remember-me: 2592000
    mail: # specific JHipster mail property, for standard properties see MailProperties
        from: 1234567890@qq.com
        base-url: http://127.0.0.1:8080
    metrics: # DropWizard Metrics configuration, used by MetricsConfiguration
        jmx:
            enabled: true
        prometheus:
            enabled: false #expose metrics via prometheus
        logs: # Reports Dropwizard metrics in the logs
            enabled: false
            report-frequency: 60 # in seconds
    logging:
        logstash: # Forward logs to logstash over a socket, used by LoggingConfiguration
            enabled: false
            host: localhost
            port: 5000
            queue-size: 512

permission:
    do-init: false

microservice:
    remote-authorization: false
    remote-protocol-prefix: http://
    authorization-ip: localhost
    authorization-port: 8088
