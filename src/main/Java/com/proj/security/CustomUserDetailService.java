package com.proj.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proj.model.User;
import com.proj.model.UserProfile;
import com.proj.model.UserProfileType;
import com.proj.service.UserService;

@Service("customUserDetailsService")
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException {
		//User user = userService.findBySSO(ssoId);
//		if (user == null) {
//			throw new UsernameNotFoundException("Username not found");
//		}
		return new org.springframework.security.core.userdetails.User("sam", "$2a$10$4eqIF5s/ewJwHK1p8lqlFOEm2QIA0S8g6./Lok.pQxqcxaBZYChRm", true, true,
				true, true, getGrantedAuthorities());
	}
	 
    
    private List<GrantedAuthority> getGrantedAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         
//        for(UserProfile userProfile : user.getUserProfiles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+UserProfileType.ADMIN));
//        }
        return authorities;
    }
     
}
