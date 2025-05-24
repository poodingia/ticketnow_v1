package com.ticketnow.account;

import com.ticketnow.account.config.EmailPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@EnableConfigurationProperties({EmailPropertiesConfig.class})
@Controller
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@RequestMapping("/actuator/health")
	@ResponseBody
	public String health() {
		return "Good";
	}

}
