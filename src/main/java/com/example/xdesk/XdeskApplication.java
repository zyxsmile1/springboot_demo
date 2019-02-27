package com.example.xdesk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.xdesk.mapper")
public class XdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdeskApplication.class, args);
	}

}
