package com.example.demovaadin.authService;

import java.util.*;

public class Authentication {
    private String username;
    private String password;
    private Set<String> roles;
    private boolean isAuthorized;

    public static AuthenticationBuilder builder(){
        return new AuthenticationBuilder();
    }

    public static AuthenticationBuilder builder(Authentication authentication){
        return new AuthenticationBuilder(authentication);
    }

    public static Authentication emptyAuthentication(){
        return new Authentication(null,null,Collections.emptySet(),false);
    }

    public Authentication(String username, String password, Collection<String> roles, boolean isAuthorized) {
        this.username = username;
        this.password = password;
        this.roles = Collections.unmodifiableSet(new HashSet<>(roles));
        this.isAuthorized = isAuthorized;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public boolean checkPassword(String password){
        return Objects.equals(password,this.password);
    }
}

class AuthenticationBuilder {
    private String username;
    private String password;
    private Set<String> roles;
    private boolean isAuthorized;

    public AuthenticationBuilder(Authentication authentication) {
        username=authentication.getUsername();
        password=authentication.getPassword();
        roles = authentication.getRoles();
        isAuthorized = authentication.isAuthorized();
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

    public AuthenticationBuilder roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public AuthenticationBuilder authorized(boolean authorized) {
        isAuthorized = authorized;
        return this;
    }

    public Authentication build(){
        return new Authentication(username,password,roles,isAuthorized);
    }
}