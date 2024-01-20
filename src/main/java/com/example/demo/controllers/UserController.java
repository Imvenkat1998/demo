package com.example.demo.controllers;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/submituser")
	public String createUser(HttpServletRequest req,HttpServletResponse res)throws Exception {
		User user=new User();
		user.setUsername(req.getParameter("username"));
		//user.setPassword(req.getParameter("password"));
		user.setPassword(BCrypt.hashpw(req.getParameter("password"), BCrypt.gensalt()));
		user.setEmail(req.getParameter("email"));
		user.setRole(req.getParameter("role"));
		String message=userService.createUser(user);
		return message;
		
	}

}
