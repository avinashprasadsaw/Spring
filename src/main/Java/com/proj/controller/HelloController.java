package com.proj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping(value = "/")
public class HelloController {
	@RequestMapping(value = "", method = RequestMethod.GET)
	//@ResponseStatus(HttpStatus.OK)	
	public ModelAndView index(){
		System.out.println("Controller*********************");
		return new ModelAndView("index", "message", "Avi");  
	}
}