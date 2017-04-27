package com.proj.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable {
USER("USER"),
DBA("DBA"),
ADMIN("ADMIN");
	
	private String userProfileType;
    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }
     
    public String getUserProfileType(){
        return userProfileType;
    }
}
