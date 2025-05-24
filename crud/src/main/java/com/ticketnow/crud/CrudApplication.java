package com.ticketnow.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@EnableScheduling
public class CrudApplication {

	@GetMapping("/actuator/health")
	@ResponseBody
	public String health() {
		return "Good";
	}

	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}

}
