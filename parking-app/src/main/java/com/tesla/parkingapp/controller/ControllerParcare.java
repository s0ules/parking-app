package com.tesla.parkingapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerParcare {
	@RequestMapping("/welcome")
	public String hello() {
		return "hi";
	}
}
