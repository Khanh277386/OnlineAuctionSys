package com.auction.model;

import com.auction.exception.AuthenticationException ;
import com.auction.manager.UserManager;


public class Admin extends User{
    public Admin(String id, String name, String password){
        super(id, name, password);
    }

    public void removeUser(String targetId) throws AuthenticationException{
        UserManager.getInstance().removeUser(targetId, this);
    }

    public String getRole(){
        return ROLE_ADMIN;
    }

    public void printInfo(){
        System.out.println("[Admin] "+ getName());
    }
}