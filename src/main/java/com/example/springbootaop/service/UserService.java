package com.example.springbootaop.service;

import com.example.springbootaop.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	/**
	 * 简易打印用户信息
	 */
	void printUserInfo(User user);

}