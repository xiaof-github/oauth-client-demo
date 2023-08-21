package com.example.springbootaop.api;

import com.example.springbootaop.model.User;
import com.example.springbootaop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAPI {

	@Autowired
	private UserService userService;

	@PostMapping("/user")
	public String printUser(@RequestBody User user) {
		userService.printUserInfo(user);
		return "已完成打印！";
	}

	@GetMapping("/hello")
	public String hello() {
		System.out.println("hello world");
		return "hello world";
	}
}