package com.demo.user.entity;

public class UserPrincipal {

    private String userId; 
    private String role; 

    public UserPrincipal(String userId, String role){
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

}
