package com.example.demovaadin.backend;

import io.reactivex.Observable;

import java.util.List;

public interface LoginService {
    Observable<List<String>> usersList();

    Authorization connect(Long token);

    void disconnect(Authorization authorization);

    void login(Authorization authorization, String username, String password);
    void logout(Authorization authorization);
    void register(Authorization authorization, String username, String password);
}
