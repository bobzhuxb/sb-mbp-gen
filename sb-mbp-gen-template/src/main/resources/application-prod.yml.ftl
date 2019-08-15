logging:
    level:
        ROOT: INFO
        ${packageName}: INFO

spring:
    datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://${dbIp}:${dbPort}/${dbName}?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false
        username: ${dbUsername}
        password: ${dbPassword}
    http:
        multipart:
            location: /data/${projectName}

server:
    port: 8080

jhipster:
    http:
        version: V_1_1 # To use HTTP/2 you will need to activate TLS (see application-tls.yml)
    cache: # Cache configuration
        ehcache: # Ehcache configuration
            time-to-live-seconds: 3600 # By default objects stay 1 hour in the cache
            max-entries: 100 # Number of objects in each cache entry
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

api-pdf:
    generate: false

wx:
    app-id: xxxxxxx
    app-secret: yyyyyyy
    access-token-switch: close
    tx-map-key: HKHBZ-7SWKD-PSW4N-HOSX2-AKQA3-ICBNR
