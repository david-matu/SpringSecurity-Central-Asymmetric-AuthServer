package com.dave.spring.authserver.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRest {
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello User!";
	}
}
