package com.example.demovaadin.service.auth;

import java.util.*;

public class Authentication {
    private String username;
    private String password;
    private Set<Role> roles;

    public static AuthenticationBuilder builder(){
        return new AuthenticationBuilder();
    }

    public static AuthenticationBuilder builder(Authentication authentication){
        return new AuthenticationBuilder(authentication);
    }

    public static Authentication emptyAuthentication(){
        return new Authentication(null,null,Collections.emptySet());
    }

    public Authentication(String username, String password, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = Collections.unmodifiableSet(new HashSet<>(roles));
    }

    public String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean checkPassword(String password){
        return Objects.equals(password,this.password);
    }
}

class AuthenticationBuilder {
    private String username;
    private String password;
    private Set<Role> roles;

    public AuthenticationBuilder(Authentication authentication) {
        username=authentication.getUsername();
        password=authentication.getPassword();
        roles = authentication.getRoles();
    }

    public AuthenticationBuilder() {
        this(Authentication.emptyAuthentication());
    }

    public AuthenticationBuilder username(String username) {
        this.username = username;
        return this;
    }

    public AuthenticationBuilder password(String password) {
        this.password = password;
        return this;
    }

    public AuthenticationBuilder roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }


    public Authentication build(){
        return new Authentication(username,password,roles);
    }
}