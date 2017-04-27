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
@RequestMapping(value = "/admin")
public class AdminController {
	@RequestMapping(value = "", method = RequestMethod.GET)
	//@ResponseStatus(HttpStatus.OK)	
	public ModelAndView login(){
		 return new ModelAndView("index", "message", "Avi");  
	}
	
	@RequestMapping(value = "/fine", method = RequestMethod.GET, produces = {
			"application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody User loginProcess(){
		User user=null;
		System.out.println("admin************************************");
		try{System.out.println("Controler Redirect");
		
		user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		return user;}
		catch(Exception e){
		return user;
		}
	}

}