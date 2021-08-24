package com.bob.at;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot启动类
 * @author Bob
 */
@SpringBootApplication
@MapperScan("com.bob.at.mapper")
public class ApihelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApihelperApplication.class, args);
	}

}
