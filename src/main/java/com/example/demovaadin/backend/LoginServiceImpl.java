package com.example.demovaadin.backend;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;

@Component
@ApplicationScope
public class LoginServiceImpl implements LoginService {
    private Map<String,String> usersPasswords = new HashMap<>();
    private HashMap<Long,Authorization> authorizations = new HashMap<>();

    //private final Scheduler scheduler = Schedulers.newThread();

    private BehaviorSubject<List<String>> usersList = BehaviorSubject.create();

    public LoginServiceImpl() {
        usersList.onNext(Collections.emptyList());
    }

    @Override
    public Observable<List<String>> usersList() {
        return usersList;
    }

    @Override
    public Authorization connect(Long token) {
        if (token!=null) {
            Authorization ans = authorizations.get(token);
            if (ans!=null) return ans;
        }
        Authorization newAuthorization = Authorization.create();
        while (authorizations.values().contains(newAuthorization)) newAuthorization = Authorization.create();
        authorizations.put(newAuthorization.getToken(),newAuthorization);
        return newAuthorization;
    }

    @Override
    public void disconnect(Authorization authorization) {
        ////Wait for a better times;
    }

    @Override
    public void login(Authorization authorization, String username, String password) {
        if (username!=null && password!=null){
            if (Objects.equals(usersPasswords.get(username),password)){
                authorization.setUser(username);
            } else authorization.sendError("Wrong login/password");
        } else authorization.sendError("Empty login/password");
    }

    @Override
    public void logout(Authorization authorization) {
        authorization.setUser("");
    }

    @Override
    public void register(Authorization authorization, String username, String password) {
        if (username!=null && password!=null){
            if (!usersPasswords.containsKey(username)){
                if (!(username.length()<4)){
                    usersPasswords.put(username,password);
                    authorization.setUser(username);
                    usersList.onNext(Collections.unmodifiableList(
                        Observable
                            .fromIterable(usersPasswords.keySet())
                            .sorted()
                            .toList()
                            .blockingGet()
                    ));
                } else authorization.sendError("Username too short");
            } else authorization.sendError("User already exists");
        } else authorization.sendError("Empty login/password");
    }
}
