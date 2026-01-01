package com.example.EmployeeManagementSystemAdvance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJms
@EnableScheduling
public class EmployeeManagementSystemAdvanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementSystemAdvanceApplication.class, args);
		System.out.println("************************************");
		System.out.println("JMS MESSAGING");
		
	}

}
