package com.example.springbootaop.service.impl;

import com.example.springbootaop.model.User;
import com.example.springbootaop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

	/**
	 * 使用Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void printUserInfo(User user) {
		logger.info("用户id：" + user.getId());
		logger.info("用户名：" + user.getUsername());
		logger.info("用户昵称：" + user.getNickname());
	}

}