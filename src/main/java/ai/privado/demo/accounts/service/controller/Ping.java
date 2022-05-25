package ai.privado.demo.accounts.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/ping")
public class Ping {

	@GetMapping
	public String version() {
		return "1.0";
	}
}
