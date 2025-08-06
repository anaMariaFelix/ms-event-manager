package com.anamariafelix.ms_event_manager.jwt;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private final com.anamariafelix.ms_event_manager.model.User user;

    public JwtUserDetails(com.anamariafelix.ms_event_manager.model.User user){
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));
        this.user = user;
    }

    public String getId(){
        return this.user.getId();
    }

    public String getRole(){
        return this.user.getRole();
    }

}

