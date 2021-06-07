<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration>

<configuration scan="true">

    <!-- The FILE and ASYNC appenders are here as examples for a production configuration -->
    <!--
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern>
                <maxHistory>90</maxHistory>
            </rollingPolicy>
            <encoder>
                <charset>utf-8</charset>
                <Pattern>%d %-5level [%thread] %logger{0}: %msg%n</Pattern>
            </encoder>
        </appender>

        <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
            <queueSize>512</queueSize>
            <appender-ref ref="FILE"/>
        </appender>

        <root level="${r'${logging.level.root}'}">
            <appender-ref ref="ASYNC"/>
        </root>
    -->
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <property name="ROOT" value="logs/"/>
    <property name="FILESIZE" value="50MB"/>
    <property name="MAXHISTORY" value="100"/>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder charset="utf-8">
            <pattern>[%-5level] %d{yyyy/MM/dd HH:mm:ss} [%thread] %logger{36} - %m%n
            </pattern>
        </encoder>
    </appender>
    <!-- ERROR 输入到文件，按日期和文件大小 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder charset="utf-8">
            <pattern>[%-5level] %d{yyyy/MM/dd HH:mm:ss} [%thread] %logger{36} - %m%n
            </pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${r'${ROOT}'}%d.%i.log</fileNamePattern>
            <maxHistory>${r'${MAXHISTORY}'}</maxHistory>
            <timeBasedFileNamingAndTriggeringPolicy
                    class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>${r'${FILESIZE}'}</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
    </appender>

    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>512</queueSize>
        <appender-ref ref="FILE"/>
    </appender>

    <!-- <logger name="javax.activation" level="WARN"/>
    <logger name="javax.mail" level="WARN"/>
    <logger name="javax.management.remote" level="WARN"/>
    <logger name="javax.xml.bind" level="WARN"/>
    <logger name="ch.qos.logback" level="WARN"/>
    <logger name="com.codahale.metrics" level="WARN"/>
    <logger name="com.ryantenney" level="WARN"/>
    <logger name="com.sun" level="WARN"/>
    <logger name="com.zaxxer" level="WARN"/>
    <logger name="io.undertow" level="WARN"/>
    <logger name="io.undertow.websockets.jsr" level="ERROR"/>
    <logger name="org.ehcache" level="WARN"/>
    <logger name="org.apache" level="WARN"/>
    <logger name="org.apache.catalina.startup.DigesterFactory" level="OFF"/>
    <logger name="org.bson" level="WARN"/>
    <logger name="org.springframework" level="WARN"/>
    <logger name="org.springframework.web" level="WARN"/>
    <logger name="org.springframework.security" level="WARN"/>
    <logger name="org.springframework.cache" level="WARN"/>
    <logger name="org.xnio" level="WARN"/>
    <logger name="springfox" level="WARN"/>
    <logger name="sun.rmi" level="WARN"/>
    <logger name="sun.rmi.transport" level="WARN"/> -->


     <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="ASYNC"/>
    </root>

    <!-- https://logback.qos.ch/manual/configuration.html#shutdownHook and https://jira.qos.ch/browse/LOGBACK-1090 -->
    <shutdownHook class="ch.qos.logback.core.hook.DelayingShutdownHook"/>

    <contextListener class="ch.qos.logback.classic.jul.LevelChangePropagator">
        <resetJUL>true</resetJUL>
    </contextListener>

</configuration>
