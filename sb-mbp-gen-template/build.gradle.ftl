buildscript {
	ext {
		springBootVersion = '2.1.1.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${r'${springBootVersion}'}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'war'
apply plugin: 'io.spring.dependency-management'

group = '${packageName}'
version = '0.1'
sourceCompatibility = 1.8

configurations {
	providedRuntime
	compile.exclude module: "spring-boot-starter-tomcat"
}

repositories {
	mavenCentral()
}


dependencies {
	// Spring加载器工具
	compile "org.springframework.boot:spring-boot-loader-tools"
	// Spring邮件模块
	compile "org.springframework.boot:spring-boot-starter-mail"
	// Spring AOP模块
	compile "org.springframework.boot:spring-boot-starter-aop"

	// ===================================Servlet容器=====================================
//	implementation('org.springframework.boot:spring-boot-starter-web')
	compile ("org.springframework.boot:spring-boot-starter-web") {
		exclude module: 'spring-boot-starter-tomcat'
	}
	testImplementation('org.springframework.boot:spring-boot-starter-test')
	// Spring内嵌容器Undertow（功能类似于Tomcat）
	compile "org.springframework.boot:spring-boot-starter-undertow"

	// ===================================日志=====================================
	// Spring日志模块、logstash模块
	compile "org.springframework.boot:spring-boot-starter-logging"
	compile "net.logstash.logback:logstash-logback-encoder:5.2"

	// ===================================安全=====================================
	// Spring安全模块
	compile "org.springframework.boot:spring-boot-starter-security"
	compile "org.springframework.security:spring-security-config"
	compile "org.springframework.security:spring-security-web"
	// JWT
	compile "io.jsonwebtoken:jjwt-api:0.10.5"
	runtime "io.jsonwebtoken:jjwt-impl:0.10.5"
	runtime "io.jsonwebtoken:jjwt-jackson:0.10.5"

	// ===================================数据有效性验证=====================================
	// hibernate的validator插件
//	compile "org.hibernate.validator:hibernate-validator:6.0.14.Final"

	// ===================================数据库=====================================
	// Mysql
	compile "mysql:mysql-connector-java"
	// MyBatis Plus
	compile "com.baomidou:mybatis-plus-boot-starter:3.1.0"
	compile "com.baomidou:mybatis-plus:3.1.0"
	compile "com.baomidou:mybatis-plus-extension:3.1.0"
	compile "com.baomidou:mybatis-plus-generator:3.1.0"
	// 事务处理API
	compile "javax.transaction:javax.transaction-api"

	// ===================================缓存=====================================
	// Spring缓存模块
	compile "org.springframework.boot:spring-boot-starter-cache"
	// 缓存API
	compile "javax.cache:cache-api"
	// Ehcache
	compile "org.ehcache:ehcache"

	// ===================================接口文档=====================================
	// Swagger
	compile "io.springfox:springfox-swagger2:2.5.0"
	compile "io.springfox:springfox-swagger-ui:2.5.0"

	// ===================================异常=====================================
	// 错误处理
	compile "org.zalando:problem-spring-web:0.24.0-RC.0"

	// ===================================工具=====================================
	// apache-commons工具包
	compile "org.apache.commons:commons-lang3"
	// commons-io工具包
	compile "commons-io:commons-io:2.6"
	// commons-beanutils工具包
	compile "commons-beanutils:commons-beanutils:1.9.3"
	// 注解生成get/set方法
	compile "org.projectlombok:lombok"
	// CGLIB
	compile "commons-beanutils:commons-beanutils:1.9.3"
	compile "cglib:cglib-nodep:3.2.4"
	// apache的http客户端
	compile "org.apache.httpcomponents:httpclient"
	// Alibaba的FastJson
	compile "com.alibaba:fastjson:1.2.47"
	// POI操作Office
	compile "org.apache.poi:poi:3.17"
	compile "org.apache.poi:poi-ooxml:3.17"
	compile "org.apache.poi:poi-ooxml-schemas:3.17"
	// iText操作PDF
	compile "com.itextpdf:itextpdf:5.5.13"
	// 快速SAX模式的XML解析器
	compile "com.fasterxml.jackson.datatype:jackson-datatype-hppc"
	compile "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
	compile "com.fasterxml.jackson.datatype:jackson-datatype-hibernate5"
	compile "com.fasterxml.jackson.core:jackson-annotations"
	compile "com.fasterxml.jackson.core:jackson-databind"
	compile "com.fasterxml.jackson.module:jackson-module-afterburner"
	// 图片压缩
	compile "net.coobird:thumbnailator:0.4.8"

	// ===================================监控=====================================
	// Spring监控模块
	compile "org.springframework.boot:spring-boot-starter-actuator"
	// 健康监测Metrics报告
//	compile "io.dropwizard.metrics:metrics-core:3.2.6"
//	compile "io.dropwizard.metrics:metrics-jcache:3.2.6"
//	compile "io.dropwizard.metrics:metrics-json:3.2.6"
//	compile "io.dropwizard.metrics:metrics-jvm:3.2.6"
//	compile "io.dropwizard.metrics:metrics-servlet:3.2.6"
//	compile "io.dropwizard.metrics:metrics-servlets:3.2.6"
//	compile "com.ryantenney.metrics:metrics-spring:3.1.3"
	// Prometheus监控
	compile "io.prometheus:simpleclient:0.6.0"
	compile "io.prometheus:simpleclient_dropwizard:0.6.0"
	compile "io.prometheus:simpleclient_servlet:0.6.0"

	// ===================================微信支付=====================================

}
