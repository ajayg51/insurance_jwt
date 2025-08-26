package com.example.insurance.mis.mvp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class MvpApplication {	
	private static final Logger log = LoggerFactory.getLogger(MvpApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MvpApplication.class, args);
		
	}	


}
