package com.ticketnow.order;

import com.ticketnow.order.config.PayosPropertiesConfig;
import com.ticketnow.order.config.VNPayPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@EnableConfigurationProperties({PayosPropertiesConfig.class, VNPayPropertiesConfig.class})
@EnableJpaAuditing
public class OrderApplication {
	@GetMapping("/actuator/health")
	@ResponseBody
	public String health() {
		return "Good";
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
