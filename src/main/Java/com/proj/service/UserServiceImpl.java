package com.proj.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proj.model.User;

@Service("UserService")
public class UserServiceImpl implements UserService {

	 public User findById(int id){
		 return null;
	 }
     
	 public   User findBySSO(String sso){
		 return null;
	 }
	     
	 public void saveUser(User user){
		 
	 }
	     
	 public void updateUser(User user){
		 
	 }
	     
	 public void deleteUserBySSO(String sso){
		 
	 }
	 
	 public List<User> findAllUsers(){
		 return null;
	 } 
	     
	 public boolean isUserSSOUnique(Integer id, String sso){
		 return true;
	 }
	 
}
